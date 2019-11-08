package gg.gamello.user.core.domain.confirmation;

import gg.gamello.user.confirmation.aplication.command.ConfirmationCommand;
import gg.gamello.user.confirmation.aplication.command.CreateCommand;
import gg.gamello.user.confirmation.infrastructure.exception.ConfirmationDoesNotExistsException;
import gg.gamello.user.confirmation.infrastructure.exception.IncorrectSecretException;
import gg.gamello.user.confirmation.infrastructure.exception.OutdatedConfirmationException;

import java.util.Optional;

public interface Confirmation {
	public void request(CreateCommand command);
	public void check(ConfirmationCommand command) throws IncorrectSecretException, ConfirmationDoesNotExistsException, OutdatedConfirmationException;
	public Optional<String> validate(ConfirmationCommand command) throws IncorrectSecretException, ConfirmationDoesNotExistsException, OutdatedConfirmationException;
}
