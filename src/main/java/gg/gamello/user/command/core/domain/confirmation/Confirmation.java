package gg.gamello.user.command.core.domain.confirmation;

import gg.gamello.user.command.confirmation.aplication.command.ConfirmationCommand;
import gg.gamello.user.command.confirmation.aplication.command.CreateCommand;
import gg.gamello.user.command.confirmation.infrastructure.exception.ConfirmationDoesNotExistsException;
import gg.gamello.user.command.confirmation.infrastructure.exception.IncorrectSecretException;
import gg.gamello.user.command.confirmation.infrastructure.exception.OutdatedConfirmationException;

import java.util.Optional;

public interface Confirmation {
	void request(CreateCommand command);

	void check(ConfirmationCommand command) throws IncorrectSecretException, ConfirmationDoesNotExistsException, OutdatedConfirmationException;

	Optional<String> validate(ConfirmationCommand command) throws IncorrectSecretException, ConfirmationDoesNotExistsException, OutdatedConfirmationException;
}
