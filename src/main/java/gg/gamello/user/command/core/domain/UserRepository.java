package gg.gamello.user.command.core.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByEmail(String email);

	Optional<User> findBySlug(String slug);

	Set<User> findByIdIn(Set<UUID> userId);

	@Query("SELECT user FROM User user WHERE (user.email = :login OR user.username = :login)")
	Optional<User> findByCredentials(@Param("login") String login);

	boolean existsUserByEmailOrUsername(String email, String username);

	boolean existsBySlug(String slug);

	boolean existsByUsername(String username);
}
