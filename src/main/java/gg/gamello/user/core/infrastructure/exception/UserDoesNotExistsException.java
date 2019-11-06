package gg.gamello.user.core.infrastructure.exception;

public class UserDoesNotExistsException extends UserException {
    private final String id;

    public UserDoesNotExistsException(String id, String message) {
        super(message);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
