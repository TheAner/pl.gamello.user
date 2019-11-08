package gg.gamello.user.confirmation.infrastructure.exception;

import gg.gamello.user.confirmation.domain.action.ActionType;

import java.util.UUID;

public class ConfirmationDoesNotExistsException extends ConfirmationException {
	private final UUID userId;
	private final ActionType actionType;

	public ConfirmationDoesNotExistsException(UUID userId, ActionType actionType, String message) {
		super(message);
		this.userId = userId;
		this.actionType = actionType;
	}
}
