package gg.gamello.user.confirmation.aplication;

import gg.gamello.user.confirmation.aplication.command.ConfirmationCommand;
import gg.gamello.user.confirmation.aplication.command.CreateCommand;
import gg.gamello.user.confirmation.domain.Confirmation;
import gg.gamello.user.confirmation.domain.ConfirmationFactory;
import gg.gamello.user.confirmation.domain.ConfirmationRepository;
import gg.gamello.user.confirmation.domain.action.ActionType;
import gg.gamello.user.confirmation.infrastructure.exception.ConfirmationDoesNotExistsException;
import gg.gamello.user.confirmation.infrastructure.exception.IncorrectSecretException;
import gg.gamello.user.confirmation.infrastructure.exception.OutdatedConfirmationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationApplicationService {

	private ConfirmationFactory confirmationFactory;

	private ConfirmationRepository confirmationRepository;

	public ConfirmationApplicationService(ConfirmationFactory confirmationFactory, ConfirmationRepository confirmationRepository) {
		this.confirmationFactory = confirmationFactory;
		this.confirmationRepository = confirmationRepository;
	}

	@Transactional
	public Confirmation create(CreateCommand command) {
		var optionalConfirmation = confirmationRepository.findByUserIdAndActionType(command.getUser().getId(), command.getAction());
		optionalConfirmation.ifPresent(confirmation -> confirmationRepository.delete(confirmation));
		Confirmation confirmation = confirmationFactory.create(command);

		var message = command.getMethod().getProvider().messageBuilder()
				.fromCommand(command)
				.secret(confirmation.getSecret())
				.issuer("127.0.0.1")
				.build();
		command.getMethod().getProvider().send(message);

		confirmationRepository.save(confirmation);
		return confirmation;
	}

	@Transactional
	public void check(ConfirmationCommand command) throws ConfirmationDoesNotExistsException, OutdatedConfirmationException, IncorrectSecretException {
		Confirmation confirmation = find(command.getUserId(), command.getActionType());
		try {
			confirmation.check(command.getSecret());
		} catch (OutdatedConfirmationException e) {
			confirmationRepository.delete(confirmation);
			throw e;
		}
	}

	@Transactional
	public Optional<String> validate(ConfirmationCommand command) throws ConfirmationDoesNotExistsException, OutdatedConfirmationException, IncorrectSecretException {
		Confirmation confirmation = find(command.getUserId(), command.getActionType());
		confirmation.check(command.getSecret());
		confirmationRepository.delete(confirmation);
		return Optional.ofNullable(confirmation.getAttachment());
	}

	private Confirmation find(UUID userId, ActionType actionType) throws ConfirmationDoesNotExistsException {
		return confirmationRepository
				.findByUserIdAndActionType(userId, actionType)
				.orElseThrow(() -> new ConfirmationDoesNotExistsException(userId, actionType, "Confirmation does not exists"));
	}
}