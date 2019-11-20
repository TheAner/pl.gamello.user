package gg.gamello.user.command.confirmation.infrastructure.exception;

public class IncorrectSecretException extends ConfirmationException {
	public IncorrectSecretException(String message) {
		super(message);
	}
}
