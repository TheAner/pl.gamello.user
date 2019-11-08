package gg.gamello.user.confirmation.infrastructure.exception;

public class IncorrectSecretException extends ConfirmationException {
	public IncorrectSecretException(String message) {
		super(message);
	}
}
