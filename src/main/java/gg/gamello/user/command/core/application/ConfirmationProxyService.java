package gg.gamello.user.command.core.application;

import gg.gamello.user.command.confirmation.aplication.ConfirmationApplicationService;
import gg.gamello.user.command.confirmation.aplication.command.ConfirmationCommand;
import gg.gamello.user.command.confirmation.aplication.command.CreateCommand;
import gg.gamello.user.command.confirmation.infrastructure.exception.ConfirmationDoesNotExistsException;
import gg.gamello.user.command.confirmation.infrastructure.exception.IncorrectSecretException;
import gg.gamello.user.command.confirmation.infrastructure.exception.OutdatedConfirmationException;
import gg.gamello.user.command.core.domain.confirmation.Confirmation;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfirmationProxyService implements Confirmation {

	private ConfirmationApplicationService confirmationApplicationService;

	public ConfirmationProxyService(ConfirmationApplicationService confirmationApplicationService) {
		this.confirmationApplicationService = confirmationApplicationService;
	}

	@Override
	public void request(CreateCommand command) {
		confirmationApplicationService.create(command);
	}

	@Override
	public void check(ConfirmationCommand command) throws IncorrectSecretException, ConfirmationDoesNotExistsException, OutdatedConfirmationException {
		confirmationApplicationService.check(command);
	}

	@Override
	public Optional<String> validate(ConfirmationCommand command) throws IncorrectSecretException, ConfirmationDoesNotExistsException, OutdatedConfirmationException {
		return confirmationApplicationService.validate(command);
	}
}
