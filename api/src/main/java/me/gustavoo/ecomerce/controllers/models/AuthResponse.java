package me.gustavoo.ecomerce.controllers.models;

public class AuthResponse {
    public String message;
    public SessionStateResponse sessionState;

    public AuthResponse(String message) {
        this.message = message;
    }

    public AuthResponse(SessionStateResponse sessionState) {
        this.message = "goto index";
        this.sessionState = sessionState;
    }
}
