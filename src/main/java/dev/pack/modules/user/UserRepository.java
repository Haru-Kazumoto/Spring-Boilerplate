package dev.pack.modules.user;

import dev.pack.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {

    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByUsername(String username);
    long countByRole(Roles role);
}
