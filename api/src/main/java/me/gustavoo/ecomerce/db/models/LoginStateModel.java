package me.gustavoo.ecomerce.db.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "login_state")
@EntityListeners(AuditingEntityListener.class)
public class LoginStateModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID uuid;

    @Column(name = "data", length = 255, nullable = false)
    public String data;

    @Column(name = "state", length = 100, nullable = false)
    public String state;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public LoginStateModel(String data, String state) {
        this.data = data;
        this.state = state;
    }

    public LoginStateModel() {
        this.data = "";
        this.state = "";
    }
}
