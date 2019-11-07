package gg.gamello.user.confirmation.aplication.command;

import gg.gamello.user.confirmation.domain.action.ActionType;
import gg.gamello.user.confirmation.domain.method.MethodType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateCommand {

	private UUID userId;

	private ActionType action;

	private MethodType method;

	private String attachment;

	public static Builder builder(){
		return new Builder();
	}

	public static class Builder {
		private UUID userId;

		private ActionType action;

		private MethodType method;

		private String attachment;

		public Builder userId(UUID userId){
			this.userId = userId;
			return this;
		}

		public Builder action(ActionType action){
			this.action = action;
			return this;
		}

		public Builder method(MethodType method){
			this.method = method;
			return this;
		}

		public Builder attachment(String attachment){
			this.attachment = attachment;
			return this;
		}

		public CreateCommand build(){
			if (userId == null)
				throw new IllegalStateException("userId can not be empty");
			if (action == null)
				throw new IllegalStateException("action can not be empty");
			if (method == null)
				throw new IllegalStateException("method can not be empty");

			CreateCommand command = new CreateCommand();
			command.userId = userId;
			command.action = action;
			command.method = method;
			command.attachment = attachment;
			return command;
		}
	}
}
