package gg.gamello.user.exception;

public class OutdatedTokenException extends TokenException {
    public OutdatedTokenException(String message) {
        super(message);
    }
}
