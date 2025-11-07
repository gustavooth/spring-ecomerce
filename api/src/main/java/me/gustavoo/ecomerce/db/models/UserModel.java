package me.gustavoo.ecomerce.db.models;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    public String email;

    @Column(name = "password", length = 100, nullable = false)
    public String passowrd;

    @Column(nullable = false, length = 50)
    public String role;

    @Column(name = "role_level", nullable = false)
    public int roleLevel;

    @Column(name = "created_at", nullable = false)
    public LocalDate createdAt;

    @Column(name = "updated_at", nullable = false)
    public LocalDate updatedAt;

    public UserModel(String name, String email, String passowrd, String role, int roleLevel) {
        this.name = name;
        this.email = email;
        this.passowrd = passowrd;
        this.role = role;
        this.passowrd = passowrd;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    public UserModel() {

    }
}