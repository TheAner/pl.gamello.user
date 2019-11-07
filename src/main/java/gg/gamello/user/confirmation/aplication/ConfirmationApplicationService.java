package gg.gamello.user.confirmation.aplication;

import gg.gamello.user.confirmation.aplication.command.CreateCommand;
import gg.gamello.user.confirmation.domain.Confirmation;
import gg.gamello.user.confirmation.domain.ConfirmationFactory;
import gg.gamello.user.confirmation.domain.ConfirmationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		var optionalConfirmation = confirmationRepository.findByUserIdAndActionType(command.getUserId(), command.getAction());
		optionalConfirmation.ifPresent(confirmation -> confirmationRepository.delete(confirmation));

		Confirmation confirmation = confirmationFactory.create(command);
		confirmationRepository.save(confirmation);
		return confirmation;
	}
}
