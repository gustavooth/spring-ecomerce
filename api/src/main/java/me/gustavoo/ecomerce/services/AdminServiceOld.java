package me.gustavoo.ecomerce.services;

import me.gustavoo.ecomerce.services.models.SessionInternal;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceOld {
    JwtService jwtService;

    public AdminServiceOld(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public boolean check(SessionInternal session) {
        if (session.logged) {
            if (session.userInternal.role.equals("admin")) {
                return true;
            }
        }
        return false;
    }
}
