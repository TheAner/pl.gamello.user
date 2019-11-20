package gg.gamello.user.command.confirmation.aplication.command;

import gg.gamello.user.command.confirmation.domain.action.ActionType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ConfirmationCommand {

	private UUID userId;

	private ActionType actionType;

	private String secret;

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private UUID userId;

		private ActionType action;

		private String secret;

		public Builder userId(UUID userId) {
			this.userId = userId;
			return this;
		}

		public Builder action(ActionType action) {
			this.action = action;
			return this;
		}

		public Builder secret(String secret) {
			this.secret = secret;
			return this;
		}

		public ConfirmationCommand build() {
			if (userId == null)
				throw new IllegalStateException("userId can not be empty");
			if (action == null)
				throw new IllegalStateException("action can not be empty");
			if (secret == null)
				throw new IllegalStateException("secret can not be empty");

			ConfirmationCommand command = new ConfirmationCommand();
			command.userId = userId;
			command.actionType = action;
			command.secret = secret;
			return command;
		}
	}
}
