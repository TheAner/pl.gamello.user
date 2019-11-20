package gg.gamello.user.command.confirmation.infrastructure.exception;

public class OutdatedConfirmationException extends ConfirmationException {
	public OutdatedConfirmationException(String message) {
		super(message);
	}
}
