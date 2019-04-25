package gg.gamello.user.repository;

import gg.gamello.user.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User getUserById(UUID userId);

    Optional<User> findUserById(UUID userId);
    Optional<User> findUserByEmail(String email);

    boolean existsUserByEmailOrUsername(String email, String username);

    @Query("SELECT user FROM User user WHERE (user.email = :login OR user.username = :login)")
    Optional<User> findUserByCredentials(@Param("login") String login);
}
