package me.gustavoo.ecomerce.db.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    public String email;

    @Column(name = "password", length = 100, nullable = false)
    public String password;

    @Column(nullable = false, length = 50)
    public String role;

    @Column(name = "role_level", nullable = false)
    public int roleLevel;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public UserModel(String name, String email, String passowrd, String role, int roleLevel) {
        this.name = name;
        this.email = email;
        this.password = passowrd;
        this.role = role;
    }

    public UserModel() {

    }
}