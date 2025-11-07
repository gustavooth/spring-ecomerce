package me.gustavoo.ecomerce.controllers.auth;

import jakarta.servlet.http.HttpServletRequest;
import me.gustavoo.ecomerce.Api;
import me.gustavoo.ecomerce.controllers.SessionStateController;
import me.gustavoo.ecomerce.controllers.models.ApiResponse;
import me.gustavoo.ecomerce.controllers.models.AuthResponse;
import me.gustavoo.ecomerce.controllers.models.CheckAdminResponse;
import me.gustavoo.ecomerce.controllers.models.SessionStateResponse;
import me.gustavoo.ecomerce.db.repository.SessionRepository;
import me.gustavoo.ecomerce.services.JwtService;
import me.gustavoo.ecomerce.services.UserSession;
import me.gustavoo.ecomerce.services.models.SessionInternal;
import org.apache.catalina.User;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth/logout")
public class LoggoutController {
    JwtService jwtService;
    SessionRepository sessionRepository;
    UserSession userSession;

    LoggoutController(JwtService jwtService, SessionRepository sessionRepository, UserSession userSession) {
        this.jwtService = jwtService;
        this.sessionRepository = sessionRepository;
        this.userSession = userSession;
    }

    @PostMapping
    ApiResponse<AuthResponse> handleLoggout(@NonNull HttpServletRequest request) {
        SessionInternal session = userSession.handleSession(request);
        if (session != null) {
            var session_state = SessionStateController.loadAndLogout(request, sessionRepository, jwtService);
            if (session_state != null) {
                AuthResponse response = new AuthResponse(session_state);
                return ApiResponse.successResponse(session_state);
            }
        }
        return ApiResponse.errorResponse("fail");
    }
}
