package me.gustavoo.controlers;

import jakarta.servlet.http.HttpServletRequest;
import me.gustavoo.controlers.models.FSResponse;
import me.gustavoo.controlers.models.UploadFilesResponse;
import me.gustavoo.services.Auth;
import me.gustavoo.services.UploadToken;
import me.gustavoo.services.models.FileModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/upload_file")
public class UploadFilesController {
    UploadToken uploadToken;
    Auth auth;

    @Value("${storage.location}")
    String uploadDir;

    public UploadFilesController(UploadToken uploadToken, Auth auth) {
        this.uploadToken = uploadToken;
        this.auth = auth;
    }

    @PostMapping("/{token}")
    public FSResponse handleUploadFiles(@NonNull HttpServletRequest request,
                                        @RequestParam("files")MultipartFile[] files,
                                        @PathVariable String token) {
        if (!auth.checkAuth(request)) {
            return FSResponse.fail("fail");
        }

        var uploadInfo = uploadToken.getToken(token);
        if (uploadInfo == null) {
            return FSResponse.fail("fail");
        }

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            FileModel fileModel = null;
            for (int j = 0; j < uploadInfo.files.size(); j++) {
                FileModel currentFile = uploadInfo.files.get(j);
                if (currentFile.name.equals(file.getOriginalFilename())) {
                    fileModel = currentFile;
                    break;
                }
            }
            if (fileModel == null) {
                return FSResponse.fail("arquivo nao esperado!");
            }

            Path rootLocation = Paths.get(uploadDir+fileModel.targetPath);
            try {
                // Garante que o diretório exista
                Files.createDirectories(rootLocation);
            } catch (IOException e) {
                return FSResponse.fail("erro ao criar diretorio: " + e.getMessage());
            }

            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                if (file.isEmpty()) {
                    return FSResponse.fail("Falha ao armazenar arquivo vazio " + filename);
                }
                if (filename.contains("..")) {
                    return FSResponse.fail("Não é possível armazenar arquivo com caminho relativo fora do diretório atual ");
                }
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                return FSResponse.fail(String.format("Falha ao armazenar arquivo:%s Error:%s", file.getName(), e.getMessage()));
            }
        }

        uploadToken.removeToken(token);
        return FSResponse.success(new UploadFilesResponse());
    }
}
