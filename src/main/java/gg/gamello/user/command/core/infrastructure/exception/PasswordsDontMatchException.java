package gg.gamello.user.command.core.infrastructure.exception;

public class PasswordsDontMatchException extends Exception {
	public PasswordsDontMatchException(String message) {
		super(message);
	}
}
