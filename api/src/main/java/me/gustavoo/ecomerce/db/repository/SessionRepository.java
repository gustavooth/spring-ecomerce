package me.gustavoo.ecomerce.db.repository;

import me.gustavoo.ecomerce.db.models.SessionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<SessionModel, UUID> {
}
