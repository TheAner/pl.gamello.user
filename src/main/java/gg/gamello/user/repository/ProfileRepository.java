package gg.gamello.user.repository;

import gg.gamello.user.dao.Profile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends CrudRepository<Profile, UUID> {
    Optional<Profile> findProfileById(UUID id);
    Optional<Profile> findProfileBySlug(String slug);
}
