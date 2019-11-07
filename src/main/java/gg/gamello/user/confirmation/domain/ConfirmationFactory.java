package gg.gamello.user.confirmation.domain;

import gg.gamello.user.confirmation.aplication.command.CreateCommand;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ConfirmationFactory {

	public Confirmation create(CreateCommand command) {
		Confirmation confirmation = new Confirmation();
		confirmation.setId(UUID.randomUUID());
		confirmation.setUserId(command.getUserId());
		confirmation.setActionType(command.getAction());
		confirmation.setExpiration(command.getAction().getExpirationDate());
		confirmation.setMethodType(command.getMethod());
		confirmation.setToken(command.getMethod().getFactory().create());
		confirmation.setAttachment(command.getAttachment());
		return confirmation;
	}
}
