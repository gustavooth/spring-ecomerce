package me.gustavoo.ecomerce.db.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "sessions")
public class SessionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID uuid;

    @Column(name = "user_id", nullable = false)
    public long userId;

    @Column(name = "logged", nullable = false)
    public boolean logged;

    public SessionModel(long userId) {
        this.userId = userId;
        this.logged = true;
    }

    public SessionModel() {
        this.userId = 0;
        this.logged = false;
    }
}
