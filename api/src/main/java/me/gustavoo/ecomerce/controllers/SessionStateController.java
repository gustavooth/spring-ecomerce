package me.gustavoo.ecomerce.controllers;

import jakarta.servlet.http.HttpServletRequest;
import me.gustavoo.ecomerce.controllers.models.ApiResponse;
import me.gustavoo.ecomerce.controllers.models.SessionStateResponse;
import me.gustavoo.ecomerce.controllers.models.UserStateResponse;
import me.gustavoo.ecomerce.db.models.SessionModel;
import me.gustavoo.ecomerce.db.models.UserModel;
import me.gustavoo.ecomerce.db.repository.SessionRepository;
import me.gustavoo.ecomerce.db.repository.UserRepository;
import me.gustavoo.ecomerce.services.JwtService;
import me.gustavoo.ecomerce.services.models.SessionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/session_state")
public class SessionStateController {
    JwtService jwtService;
    SessionRepository sessionRepository;
    UserRepository userRepository;

    @Autowired
    SessionStateController(JwtService jwtService, SessionRepository sessionRepository, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    /**
     * @return pode retornar null
     */
    public static SessionStateResponse loadAndLogin(HttpServletRequest request, SessionRepository sessionRepository, UserModel userModel, JwtService jwtService) {
        String ss_token = request.getHeader("Authorization");
        if (ss_token == null || ss_token.isEmpty()) {
            System.out.println(ss_token);
            return null;
        }
        String uuid_str;
        UUID uuid;
        try {
            uuid_str  = jwtService.extractUuid(ss_token);
            uuid = UUID.fromString(uuid_str);
        } catch (Exception e) {
            return null;
        }
        var session_request = sessionRepository.findById(uuid);
        if (session_request.isPresent()) {
            SessionModel ss_data = session_request.get();
            ss_data.logged = true;
            ss_data.userId = userModel.id;
            sessionRepository.save(ss_data);
            return new SessionStateResponse(ss_token, new UserStateResponse(userModel.name, userModel.role));
        }
        return null;
    }

    /**
     * @return pode retornar null
     */
    public static SessionStateResponse loadAndLogout(HttpServletRequest request, SessionRepository sessionRepository, JwtService jwtService) {
        String ss_token = request.getHeader("Authorization");
        if (ss_token == null || ss_token.isEmpty()) {
            return null;
        }
        String uuid_str;
        UUID uuid;

        try {
            uuid_str  = jwtService.extractUuid(ss_token);
            uuid = UUID.fromString(uuid_str);
        } catch (Exception e) {
            return null;
        }

        var session_request = sessionRepository.findById(uuid);
        if (session_request.isPresent()) {
            SessionModel ss_data = session_request.get();
            ss_data.logged = false;
            ss_data.userId = 0;
            sessionRepository.save(ss_data);
            return new SessionStateResponse(ss_token);
        }
        return null;
    }

    public ApiResponse<SessionStateResponse> sessionResponseLoad(String token, HttpServletRequest request) {
        String uuid_str;
        try {
            uuid_str = jwtService.extractUuid(token);
        } catch (Exception e) {
            return sessionResponseSave(request);
        }
        UUID uuid = UUID.fromString(uuid_str);
        var session_result = sessionRepository.findById(uuid);

        if (session_result.isPresent()) {
            var session_data = session_result.get();

            if (session_data.logged) {
                var user_result = userRepository.findById(session_data.userId);

                if (user_result.isPresent()) {
                    var user_data = user_result.get();
                    return ApiResponse.successResponse(new SessionStateResponse(token, new UserStateResponse(user_data.name, user_data.role)));
                }
            }else {
                return ApiResponse.successResponse(new SessionStateResponse(token));
            }
        }

        return sessionResponseSave(request);
    }

    public ApiResponse<SessionStateResponse> sessionResponseSave(HttpServletRequest request) {
        var session = sessionRepository.save(new SessionModel());
        var token = jwtService.generateToken(new SessionDetails(session.uuid));

        return ApiResponse.successResponse(new SessionStateResponse(token));
    }

    @GetMapping
    public ApiResponse<SessionStateResponse> handleSessionState(@NonNull HttpServletRequest request) {
        System.out.println(request);
        var token = request.getHeader("Authorization");
        if(token != null && !token.isEmpty()) {
            return sessionResponseLoad(token, request);
        }
        return sessionResponseSave(request);
    }
}
