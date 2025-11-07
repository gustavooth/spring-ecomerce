package me.gustavoo.ecomerce.controllers.auth;

import me.gustavoo.ecomerce.controllers.RequestBody.EmailBody;
import me.gustavoo.ecomerce.controllers.models.ApiResponse;
import me.gustavoo.ecomerce.db.models.LoginStateModel;
import me.gustavoo.ecomerce.db.repository.UserRepository;
import me.gustavoo.ecomerce.db.repository.UserStateRepository;
import me.gustavoo.ecomerce.services.JwtService;
import me.gustavoo.ecomerce.controllers.models.EmailResponse;
import me.gustavoo.ecomerce.services.models.SessionDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/email")
public class EmailController {
    JwtService jwtService;
    UserStateRepository userStateRepository;
    UserRepository userRepository;

    EmailController(JwtService jwtService, UserStateRepository userStateRepository, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userStateRepository = userStateRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ApiResponse<EmailResponse> emailResponse(@RequestBody EmailBody body) {
        var user_result = userRepository.findByEmail(body.email());

        if (user_result.isPresent()) {
            var user_data = user_result.get();

            var user_login = userStateRepository.save(new LoginStateModel(user_data.email, "login"));

            String token = jwtService.generateToken(new SessionDetails(user_login.uuid));

            return ApiResponse.successResponse(new EmailResponse(token, "login"));
        }else {
            var user_login = userStateRepository.save(new LoginStateModel(body.email(), "register"));

            String token = jwtService.generateToken(new SessionDetails(user_login.uuid));

            return ApiResponse.successResponse(new EmailResponse(token, "register"));
        }
    }
}
