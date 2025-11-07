package me.gustavoo.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Auth {
    @Value("${authorization.token}")
    String token;

    public boolean checkAuth(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(header != null && !header.isEmpty()) {
            return header.equals(token);
        }
        return false;
    }
}
