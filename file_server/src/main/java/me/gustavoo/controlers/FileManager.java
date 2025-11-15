package me.gustavoo.controlers;

import jakarta.servlet.http.HttpServletRequest;
import me.gustavoo.controlers.body.FileManagerBody;
import me.gustavoo.controlers.models.*;
import me.gustavoo.services.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/fs")
public class FileManager {
    Auth auth;

    @Value("${storage.location}")
    String uploadDir;

    public FileManager(Auth auth) {
        this.auth = auth;
    }

    public ArrayList<String> removeAll(Path path) throws IOException {
        var removedFiles = new ArrayList<String>();
        if (Files.isDirectory(path)) {
            Stream<Path> filesStream = Files.list(path);
            List<Path> paths = filesStream.toList();
            ArrayList<ItemListResponse> items = new ArrayList<>(paths.size());
            for (Path file : paths) {
                var lista = removeAll(file);
                if (lista != null) {
                    for (String item: lista) {
                        if (item != null) {
                            removedFiles.add(item);
                        }
                    }
                }
            }
        }

        if (path.toString().startsWith(uploadDir)) {
            removedFiles.add(path.toString().split(uploadDir)[1]);
            Files.deleteIfExists(path);
            return removedFiles;
        }

        System.out.println("fail: "+path.toString());
        return null;
    }

    String move(String path, String newPath) throws IOException {
        Path currentLocation = Paths.get(uploadDir+path);
        Path newLocation = Paths.get(uploadDir+newPath);

        Files.move(currentLocation, newLocation);

        return newPath;
    }

    String copy(String path, String newPath) throws IOException {
        Path currentLocation = Paths.get(uploadDir+path);
        Path newLocation = Paths.get(uploadDir+newPath);

        Files.copy(currentLocation, newLocation);

        return newPath;
    }

    ArrayList remove(String path) throws IOException {
        Path currentLocation = Paths.get(uploadDir+path);
        return removeAll(currentLocation);
    }

    @PostMapping("/mv")
    public FSResponse handleMv(@NonNull HttpServletRequest request, @RequestBody FileManagerBody body) {
        if (!auth.checkAuth(request)) {
            return FSResponse.fail("fail");
        }

        var movedFiles = new ArrayList<String>(body.paths.length);

        try {
            for (int i = 0; i < body.paths.length; i+=2) {
                var currentPath = body.paths[i];
                var newPath = body.paths[i+1];
                var resp = move(currentPath, newPath);
                movedFiles.add(resp);
            }
        } catch (Exception e) {
            return FSResponse.fail(String.format("Fail to move: %s", e.toString()));
        }

        return FSResponse.success(new ListPathsResponse(movedFiles));
    }

    @PostMapping("/cp")
    public FSResponse handleCp(@NonNull HttpServletRequest request, @RequestBody FileManagerBody body) {
        if (!auth.checkAuth(request)) {
            return FSResponse.fail("fail");
        }

        var copiedFiles = new ArrayList<String>(body.paths.length/2);

        try {
            for (int i = 0; i < body.paths.length; i+=2) {
                var currentPath = body.paths[i];
                var newPath = body.paths[i+1];
                var resp = copy(currentPath, newPath);
                copiedFiles.add(resp);
            }
        } catch (Exception e) {
            return FSResponse.fail(String.format("Fail to copy: %s", e.toString()));
        }

        return FSResponse.success(new ListPathsResponse(copiedFiles));
    }

    @PostMapping("rm")
    public FSResponse handleRemove(@NonNull HttpServletRequest request, @RequestBody FileManagerBody body) {
        if (!auth.checkAuth(request)) {
            return FSResponse.fail("fail");
        }

        var removedFiles = new ArrayList<String>(body.paths.length);

        try {
            for (int i = 0; i < body.paths.length; i++) {
                var currentPath = body.paths[i];
                ArrayList<String> resp = remove(currentPath);

                for (int j = 0; j < resp.size(); j++) {
                    removedFiles.add(resp.get(i));
                }
            }
        } catch (Exception e) {
            FSResponse.fail(String.format("Fail to remove: %s", e.getMessage()));
        }

        return FSResponse.success(new ListPathsResponse(removedFiles));
    }

    @PostMapping("/mkdir")
    public FSResponse handleNewFolder(@NonNull HttpServletRequest request, @RequestBody FileManagerBody body) {
        if (!auth.checkAuth(request)) {
            return FSResponse.fail("fail");
        }

        Path currentLocation = Paths.get(uploadDir+body.paths[0]);

        try {
            Files.createDirectories(currentLocation);
        } catch (IOException e) {
            return FSResponse.fail("Fail to make dir: " + e.getMessage());
        }

        return FSResponse.success(new PathResponse(body.paths[0]));
    }

    @PostMapping("/ls")
    public FSResponse handleListDir(@NonNull HttpServletRequest request, @RequestBody FileManagerBody body) {
        if (!auth.checkAuth(request)) {
            return FSResponse.fail("fail");
        }

        Path currentLocation = Paths.get(uploadDir+body.paths[0]);

        if (Files.exists(currentLocation)) {
            if (Files.isDirectory(currentLocation)) {
                try(Stream<Path> filesStream = Files.list(currentLocation)) {
                    List<Path> paths = filesStream.toList();
                    ArrayList<ItemListResponse> items = new ArrayList<>(paths.size());
                    for (Path file : paths) {
                        boolean isDir = Files.isDirectory(file);
                        items.add(new ItemListResponse(isDir, file.getFileName().toString(), file.toString().split(uploadDir)[1]));
                    }

                    return FSResponse.success(new ListItemsResponse(items));
                } catch (Exception e) {
                    return FSResponse.fail(String.format("Stream error:%s", e.getMessage()));
                }
            }
        }

        return FSResponse.fail("fail2");
    }
}
