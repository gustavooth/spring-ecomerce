package me.gustavoo.ecomerce.services.models;

import me.gustavoo.ecomerce.db.models.SessionModel;
import me.gustavoo.ecomerce.db.models.UserModel;

import java.util.UUID;

public class SessionInternal {
    public UUID uuid;
    public boolean logged;
    public UserInternal userInternal;

    SessionInternal(UUID uuid) {
        this.uuid = uuid;
        this.logged = false;
    }

    SessionInternal(UUID uuid, UserInternal userInternal) {
        this.uuid = uuid;
        this.logged = true;
        this.userInternal = userInternal;
    }

    public static SessionInternal fromDbModel(SessionModel sessionModel) {
        return new SessionInternal(sessionModel.uuid);
    }

    public static SessionInternal fromDbModels(SessionModel sessionModel, UserModel userModel) {
        return new SessionInternal(sessionModel.uuid, UserInternal.fromUserModel(userModel));
    }
}
