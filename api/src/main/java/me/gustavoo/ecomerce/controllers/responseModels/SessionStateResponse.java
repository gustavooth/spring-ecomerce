package me.gustavoo.ecomerce.controllers.responseModels;

public class SessionStateResponse {
    public String token;
    public Boolean logged;
    public UserStateResponse user;

    public SessionStateResponse(String token) {
        this.token = token;
        this.logged = false;
    }

    public SessionStateResponse(String token, UserStateResponse user) {
        this.token = token;
        this.logged = true;
        this.user = user;
    }

}
