package me.gustavoo.ecomerce.controllers.adminControllers;

import me.gustavoo.ecomerce.controllers.requestModels.CheckAdminRequest;
import me.gustavoo.ecomerce.controllers.responseModels.ApiResponse;
import me.gustavoo.ecomerce.controllers.responseModels.CheckAdminResponse;
import me.gustavoo.ecomerce.services.UserSession;
import me.gustavoo.ecomerce.services.models.SessionInternal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/check_admin")
public class CheckAdminController {
    UserSession userSession;

    public CheckAdminController(UserSession userSession) {
        this.userSession = userSession;
    }

    @PostMapping
    public ApiResponse<CheckAdminResponse> handleCheckAdmin(@RequestBody CheckAdminRequest body) {
        SessionInternal session = userSession.handleSession(body.token());
        if (session != null) {
            if (session.userInternal != null) {
                if (session.userInternal.role.equals("admin")) {
                    return ApiResponse.successResponse(new CheckAdminResponse(true));
                }else {
                    return ApiResponse.successResponse(new CheckAdminResponse(false));
                }
            }
        }
        return ApiResponse.errorResponse("fail");
    }
}
