package gg.gamello.user.confirmation.domain;

import gg.gamello.user.confirmation.domain.action.ActionType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfirmationRepository extends CrudRepository<Confirmation, UUID> {
	Optional<Confirmation> findByUserIdAndActionType(@NotNull UUID userId, ActionType actionType);
}
