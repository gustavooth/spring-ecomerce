package me.gustavoo.ecomerce.controllers.auth;

import jakarta.servlet.http.HttpServletRequest;
import me.gustavoo.ecomerce.controllers.SessionStateController;
import me.gustavoo.ecomerce.controllers.models.*;
import me.gustavoo.ecomerce.db.repository.SessionRepository;
import me.gustavoo.ecomerce.db.repository.UserRepository;
import me.gustavoo.ecomerce.db.repository.UserStateRepository;
import me.gustavoo.ecomerce.services.JwtService;
import me.gustavoo.ecomerce.services.UserSession;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import me.gustavoo.ecomerce.controllers.RequestBody.LoginBody;

import java.util.UUID;

@RestController
@RequestMapping("/auth/login")
public class LoginController {
    JwtService jwtService;
    UserStateRepository userStateRepository;
    UserRepository userRepository;
    SessionRepository sessionRepository;

    LoginController(JwtService jwtService, UserStateRepository userStateRepository, UserRepository userRepository, SessionRepository sessionRepository, UserSession userSession) {
        this.jwtService = jwtService;
        this.userStateRepository = userStateRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @PostMapping
    ApiResponse<AuthResponse> handleLogin(@RequestBody LoginBody body, @NonNull HttpServletRequest request) {
        String uuid_str = jwtService.extractUuid(body.token());
        var user_state_response = userStateRepository.findById(UUID.fromString(uuid_str));
        if (user_state_response.isPresent() && user_state_response.get().state.startsWith("login")) {
            var user_state_data = user_state_response.get();
            var user_response = userRepository.findByEmail(user_state_data.data);
            if (user_response.isPresent()) {
                var user_data = user_response.get();
                if (user_data.passowrd.equals(body.password())){
                    var sessionState = SessionStateController.loadAndLogin(request, sessionRepository, user_data, jwtService);
                    if (sessionState != null) {
                        AuthResponse response = new AuthResponse(sessionState);
                        return ApiResponse.successResponse(response);
                    }
                }else {
                    AuthResponse response = new AuthResponse("invalid password");
                    return ApiResponse.successResponse(response);
                }
            }
        }
        return ApiResponse.errorResponse("fail");
    }
}
