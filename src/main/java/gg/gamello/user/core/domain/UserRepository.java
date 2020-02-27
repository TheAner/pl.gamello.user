package gg.gamello.user.core.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
	Optional<User> findByEmail(String email);

	boolean existsUserByEmailOrUsername(String email, String username);

	boolean existsBySlug(String slug);
}
