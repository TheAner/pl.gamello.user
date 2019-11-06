package gg.gamello.user.core.infrastructure.exception;

public class UserAlreadyExistsException extends UserException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
