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
import java.util.stream.Collectors;
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

    public ArrayList<String> removeAll(Path path) {
        var removedFiles = new ArrayList<String>();
        if (Files.isDirectory(path)) {
            try(Stream<Path> filesStream = Files.list(path)) {
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
            } catch (Exception e) {
                System.out.println(String.format("file Stream error: %s", path.toString(), e.getMessage()));
            }
        }

        try {
            if (path.toString().startsWith(uploadDir)) {
                removedFiles.add(path.toString().split(uploadDir)[1]);
                Files.deleteIfExists(path);
                return removedFiles;
            }
        } catch (IOException e) {
            System.out.println(String.format("Delete if exists %s error: %s", path.toString(), e.getMessage()));
            return null;
        }

        System.out.println("fail: "+path.toString());
        return null;
    }

    @PostMapping("rm")
    public FSResponse handleRemove(@NonNull HttpServletRequest request, @RequestBody FileManagerBody body) {
        if (!auth.checkAuth(request)) {
            return FSResponse.fail("fail");
        }

        Path currentLocation = Paths.get(uploadDir+body.path);

        var removedFiles = removeAll(currentLocation);

        return FSResponse.success(new RemovedFilesResponse(removedFiles));
    }

    @PostMapping("/mkdir")
    public FSResponse handleNewFolder(@NonNull HttpServletRequest request, @RequestBody FileManagerBody body) {
        if (!auth.checkAuth(request)) {
            return FSResponse.fail("fail");
        }

        Path currentLocation = Paths.get(uploadDir+body.path);

        try {
            Files.createDirectories(currentLocation);
        } catch (IOException e) {
            return FSResponse.fail("erro ao criar diretorio: " + e.getMessage());
        }

        return FSResponse.success(new NewFolderResponse(currentLocation.toString().split(uploadDir)[1]));
    }

    @PostMapping("/ls")
    public FSResponse handleListDir(@NonNull HttpServletRequest request, @RequestBody FileManagerBody body) {
        if (!auth.checkAuth(request)) {
            return FSResponse.fail("fail");
        }

        Path currentLocation = Paths.get(uploadDir+body.path);

        if (Files.exists(currentLocation)) {
            if (Files.isDirectory(currentLocation)) {
                try(Stream<Path> filesStream = Files.list(currentLocation)) {
                    List<Path> paths = filesStream.toList();
                    ArrayList<ItemListResponse> items = new ArrayList<>(paths.size());
                    for (Path file : paths) {
                        boolean isDir = Files.isDirectory(file);
                        items.add(new ItemListResponse(isDir, file.getFileName().toString(), file.toString().split(uploadDir)[1]));
                    }

                    return FSResponse.success(new ListFilesResponse(items));
                } catch (Exception e) {
                    return FSResponse.fail(String.format("Stream error:%s", e.getMessage()));
                }
            }
        }

        return FSResponse.fail("fail2");
    }
}
