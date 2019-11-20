package gg.gamello.user.command.core.infrastructure.exception;

public class UserAlreadyExistsException extends PropertyConflictException {
	public UserAlreadyExistsException(String message) {
		super("user", message);
	}
}
