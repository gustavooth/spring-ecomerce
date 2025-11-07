package me.gustavoo.ecomerce.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.gustavoo.ecomerce.db.models.LoginStateModel;

import java.util.UUID;

@Repository
public interface UserStateRepository extends JpaRepository<LoginStateModel, UUID> {
}
