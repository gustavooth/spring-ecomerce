package me.gustavoo.ecomerce.controllers.authControllers;

import jakarta.servlet.http.HttpServletRequest;
import me.gustavoo.ecomerce.controllers.SessionStateController;
import me.gustavoo.ecomerce.controllers.responseModels.ApiResponse;
import me.gustavoo.ecomerce.controllers.responseModels.AuthResponse;
import me.gustavoo.ecomerce.db.models.UserModel;
import me.gustavoo.ecomerce.db.repository.SessionRepository;
import me.gustavoo.ecomerce.db.repository.UserRepository;
import me.gustavoo.ecomerce.db.repository.UserStateRepository;
import me.gustavoo.ecomerce.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import me.gustavoo.ecomerce.controllers.requestModels.RegisterRequest;

import java.util.UUID;

@RestController
@RequestMapping("/auth/register")
public class RegisterController {
    JwtService jwtService;
    UserStateRepository userStateRepository;
    UserRepository userRepository;
    SessionRepository sessionRepository;

    @Autowired
    RegisterController(JwtService jwtService, UserStateRepository userStateRepository, UserRepository userRepository, SessionRepository sessionRepository) {
        this.jwtService = jwtService;
        this.userStateRepository = userStateRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @PostMapping
    public ApiResponse<AuthResponse> handleRegister(@RequestBody RegisterRequest body, @NonNull HttpServletRequest request) {
        String uuid_str = jwtService.extractUuid(body.token());
        var user_state_response = userStateRepository.findById(UUID.fromString(uuid_str));
        if (user_state_response.isPresent()) {
           var user_state_data = user_state_response.get();
           if (user_state_data.state.startsWith("register")) {
               UserModel newUser = new UserModel(body.name(), user_state_data.data, body.password(), "admin", 9);
               var user_data = userRepository.save(newUser);
               userStateRepository.delete(user_state_data);
               var sessionState = SessionStateController.loadAndLogin(request, sessionRepository, newUser, jwtService);
               if (sessionState != null) {
                   var authResponse = new AuthResponse(sessionState);
                   return ApiResponse.successResponse(authResponse);
               }
           }
        }
        return ApiResponse.errorResponse("fail");
    }
}
