package me.gustavoo.ecomerce.db.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@EntityListeners(AuditingEntityListener.class)
public class SessionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID uuid;

    @Column(name = "user_id", nullable = false)
    public long userId;

    @Column(name = "logged", nullable = false)
    public boolean logged;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public SessionModel(long userId) {
        this.userId = userId;
        this.logged = true;
    }

    public SessionModel() {
        this.userId = 0;
        this.logged = false;
    }
}
