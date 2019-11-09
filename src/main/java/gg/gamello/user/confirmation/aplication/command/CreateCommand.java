package gg.gamello.user.confirmation.aplication.command;

import gg.gamello.user.confirmation.domain.action.ActionType;
import gg.gamello.user.confirmation.domain.method.MethodType;
import gg.gamello.user.core.application.dto.UserDto;
import lombok.Getter;

@Getter
public class CreateCommand {

	private UserDto user;

	private ActionType action;

	private MethodType method;

	private String attachment;

	public static Builder builder(){
		return new Builder();
	}

	public static class Builder {
		private UserDto user;

		private ActionType action;

		private MethodType method;

		private String attachment;

		public Builder user(UserDto user){
			this.user = user;
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
			if (user == null)
				throw new IllegalStateException("user can not be null");
			if (action == null)
				throw new IllegalStateException("action can not be empty");
			if (method == null)
				throw new IllegalStateException("method can not be empty");

			CreateCommand command = new CreateCommand();
			command.user = user;
			command.action = action;
			command.method = method;
			command.attachment = attachment;
			return command;
		}
	}
}
