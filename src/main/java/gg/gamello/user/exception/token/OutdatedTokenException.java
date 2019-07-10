package gg.gamello.user.exception.token;

public class OutdatedTokenException extends TokenException {
    public OutdatedTokenException(String message) {
        super(message);
    }
}
