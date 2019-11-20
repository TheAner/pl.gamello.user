package gg.gamello.user.command.core.infrastructure.exception;

import lombok.Getter;

public class PropertyConflictException extends UserException {
	@Getter
	private final String propertyName;

	public PropertyConflictException(String propertyName, String message) {
		super(message);
		this.propertyName = propertyName;
	}
}
