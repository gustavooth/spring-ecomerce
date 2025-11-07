package me.gustavoo.ecomerce.db.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "login_state")
public class LoginStateModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID uuid;

    @Column(name = "data", length = 255, nullable = false)
    public String data;

    @Column(name = "state", length = 100, nullable = false)
    public String state;

    public LoginStateModel(String data, String state) {
        this.data = data;
        this.state = state;
    }

    public LoginStateModel() {
        this.data = "";
        this.state = "";
    }
}
