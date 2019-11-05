package gg.gamello.user.core.domain.email;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailRepository extends CrudRepository<Email, UUID> {
}
