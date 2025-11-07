package me.gustavoo.controlers;

import jakarta.servlet.http.HttpServletRequest;
import me.gustavoo.controlers.body.NewTokenBody;
import me.gustavoo.controlers.models.NewUploadTokenResponse;
import me.gustavoo.controlers.models.FSResponse;
import me.gustavoo.services.Auth;
import me.gustavoo.services.UploadToken;
import me.gustavoo.services.models.FileModel;
import me.gustavoo.services.models.UploadFilesModel;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/admin/new_upload_token")
public class NewUploadTokenController {
    Auth auth;
    UploadToken uploadToken;

    NewUploadTokenController(Auth auth, UploadToken uploadToken) {
        this.auth = auth;
        this.uploadToken = uploadToken;
    }

    @PostMapping
    public FSResponse handleNewUploadToken(@NonNull HttpServletRequest request, @RequestBody NewTokenBody body) {
        if (!auth.checkAuth(request)) {
            return FSResponse.fail("fail");
        }
        var files = new ArrayList<FileModel>(body.files.length);
        for (int i = 0; i < body.files.length; i++) {
            var newFile = body.files[i];
            var file = new FileModel(newFile.name, newFile.path);
            files.add(file);
        }
        var uploadFiles = new UploadFilesModel(files);
        var newToken = uploadToken.newToken(uploadFiles);

        return FSResponse.success(new NewUploadTokenResponse(newToken));
    }
}
