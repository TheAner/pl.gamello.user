package gg.gamello.user.exception;

public class PasswordsDontMatchException extends Exception {
    public PasswordsDontMatchException(String message) {
        super(message);
    }
}
