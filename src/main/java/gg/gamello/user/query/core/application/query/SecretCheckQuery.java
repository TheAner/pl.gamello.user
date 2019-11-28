package gg.gamello.user.query.core.application.query;

import gg.gamello.user.command.confirmation.domain.action.ActionType;
import gg.gamello.user.command.core.application.command.ConfirmCommand;
import gg.gamello.user.infrastructure.validation.Enum;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SecretCheckQuery extends ConfirmCommand {
	@NotEmpty(message = "Action can not be empty")
	@Enum(clazz = ActionType.class, message = "Action should be instance of ActionType enum")
	String action;

	public void setAction(String action) {
		this.action = action.toUpperCase();
	}
}
