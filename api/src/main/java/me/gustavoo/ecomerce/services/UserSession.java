package me.gustavoo.ecomerce.services;

import jakarta.servlet.http.HttpServletRequest;
import me.gustavoo.ecomerce.db.models.SessionModel;
import me.gustavoo.ecomerce.db.models.UserModel;
import me.gustavoo.ecomerce.db.repository.SessionRepository;
import me.gustavoo.ecomerce.db.repository.UserRepository;
import me.gustavoo.ecomerce.services.models.SessionInternal;
import me.gustavoo.ecomerce.services.models.UserInternal;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserSession {
    JwtService jwtService;
    SessionRepository sessionRepository;
    UserRepository userRepository;

    public UserSession(JwtService jwtService, SessionRepository sessionRepository, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    /**
     * @return Pode retornar null
     */
    public SessionInternal handleSession(@NonNull HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return null;
        }

        return handleSession(token);
    }

    /**
     * @return Pode retornar null
     */
    public SessionInternal handleSession(String token) {
        var uuid_str = jwtService.extractUuid(token);
        var sessionResult = sessionRepository.findById(UUID.fromString(uuid_str));
        if (sessionResult.isPresent()) {
            SessionModel sessionData = sessionResult.get();
            if (sessionData.logged) {
                var userResult = userRepository.findById(sessionData.userId);
                if (userResult.isPresent()) {
                    UserModel userData = userResult.get();
                    return SessionInternal.fromDbModels(sessionData, userData);
                }
            }else {
                return SessionInternal.fromDbModel(sessionData);
            }
        }

        return null;
    }
}
