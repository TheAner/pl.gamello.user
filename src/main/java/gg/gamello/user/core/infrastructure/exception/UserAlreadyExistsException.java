package gg.gamello.user.core.infrastructure.exception;

public class UserAlreadyExistsException extends PropertyConflictException {
	public UserAlreadyExistsException(String message) {
		super("user", message);
	}
}
