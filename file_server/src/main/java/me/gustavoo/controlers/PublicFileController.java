package me.gustavoo.controlers;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/public")
public class PublicFileController {
    @Value("${storage.location}")
    String uploadDir;

    @GetMapping()
    public ResponseEntity<Resource> handleViewFile(@PathParam(value = "path")String path) {

        try {
            Path publicPath = Paths.get(uploadDir+path);

            Resource resource = new UrlResource(publicPath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "image/png")
                        .body(resource);
            }
        } catch (MalformedURLException e) {
            return null;
        }
        return null;
    }

    /*
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> handleDownloadFile(@PathVariable String filename) {
        try {
            Path publicPath = Paths.get(uploadDir);
            Path file = publicPath.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            }
        } catch (MalformedURLException e) {
            return null;
        }
        return null;
    }
    */
}
