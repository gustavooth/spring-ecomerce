package me.gustavoo.ecomerce.controllers.responseModels;

public class UserStateResponse {
    public String display_name;
    public String role;

    public UserStateResponse(String display_name, String role) {
        this.display_name = display_name;
        this.role = role;
    }
}
