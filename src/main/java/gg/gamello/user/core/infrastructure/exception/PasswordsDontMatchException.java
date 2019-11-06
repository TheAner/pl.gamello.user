package gg.gamello.user.core.infrastructure.exception;

public class PasswordsDontMatchException extends Exception {
    public PasswordsDontMatchException(String message) {
        super(message);
    }
}
