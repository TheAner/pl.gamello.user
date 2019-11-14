package gg.gamello.user.core.infrastructure.exception;

import lombok.Getter;

public class PropertyConflictException extends UserException {
	@Getter
	private final String propertyName;

	public PropertyConflictException(String propertyName, String message) {
		super(message);
		this.propertyName = propertyName;
	}
}
