package gg.gamello.user.core.application.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class VisibleNameChangeCommand {
	@NotEmpty(message = "Name can not be empty")
	String visibleName;
}
