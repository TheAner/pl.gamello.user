package gg.gamello.user.confirmation.infrastructure.provider.email;

import gg.gamello.user.confirmation.aplication.command.CreateCommand;
import gg.gamello.user.confirmation.domain.action.ActionType;
import gg.gamello.user.confirmation.infrastructure.provider.Message;
import gg.gamello.user.confirmation.infrastructure.provider.MessageBuilder;
import gg.gamello.user.core.domain.language.Language;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ToString
class EmailMessage extends Message {
	private UUID userId;
	private String name;
	private String email;
	private String template;
	private String language;
	private String issuer;
	private Map<String, String> data;

	public static class Builder implements MessageBuilder<EmailMessage> {
		private UUID userId;
		private String name;
		private String email;
		private String template;
		private String language;
		private String issuer = "";
		private Map<String, String> data = new HashMap<>();

		public Builder user(UUID userId, String name, String email) {
			this.userId = userId;
			this.name = name;
			this.email = email;
			return this;
		}

		public Builder useTemplate(ActionType actionType, String secret) {
			this.template = "user." + actionType.toString().toLowerCase();
			this.addData("token", secret);
			return this;
		}

		public Builder useTemplateChanged(ActionType actionType) {
			this.template = "user." + actionType.toString().toLowerCase() + ".changed";
			return this;
		}

		public Builder language(Language language) {
			this.language = language.toString();
			return this;
		}

		@Override
		public Builder issuer(String issuer) {
			this.issuer = issuer;
			return this;
		}

		public Builder addData(String key, String value) {
			this.data.put(key, value);
			return this;
		}

		@Override
		public Builder fromCommand(CreateCommand command) {
			this.user(command.getUser().getId(), command.getUser().getUsername(), command.getUser().getEmail());
			this.language(command.getUser().getLanguage());
			this.template = "user." + command.getAction().toString().toLowerCase();
			if (command.getAction().equals(ActionType.EMAIL)) //Email confirmation routed to new email
				this.email = command.getAttachment();
			return this;
		}

		@Override
		public Builder secret(String secret) {
			this.addData("token", secret);
			return this;
		}

		@Override
		public EmailMessage build() {
			if (userId == null || name == null || email == null)
				throw new IllegalStateException("user contact can not be empty");
			if (template == null)
				throw new IllegalStateException("template can not be empty");
			if (language == null)
				throw new IllegalStateException("language can not be empty");

			var message = new EmailMessage();
			message.userId = userId;
			message.name = name;
			message.email = email;
			message.template = template;
			message.language = language;
			message.issuer = issuer;
			message.data = data;
			return message;
		}
	}
}
