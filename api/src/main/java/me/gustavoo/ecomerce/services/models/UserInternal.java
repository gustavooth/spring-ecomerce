package me.gustavoo.ecomerce.services.models;

import me.gustavoo.ecomerce.db.models.UserModel;

public class UserInternal {
    public String name;
    public String role;
    public int role_level;

    public UserInternal(String name, String role, int role_level) {
        this.name = name;
        this.role = role;
        this.role_level = role_level;
    }

    public static UserInternal fromUserModel(UserModel userModel) {
        return new UserInternal(userModel.name, userModel.role, userModel.roleLevel);
    }
}
