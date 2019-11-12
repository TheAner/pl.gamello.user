package gg.gamello.user.core.application.command;

import gg.gamello.user.confirmation.domain.action.ActionType;
import gg.gamello.user.core.application.command.validation.Enum;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CheckSecretCommand extends ConfirmCommand {
	@NotEmpty(message = "Action can not be empty")
	@Enum(clazz = ActionType.class, message = "Action should be instance of ActionType enum")
	String action;

	public void setAction(String action) {
		this.action = action.toUpperCase();
	}
}
