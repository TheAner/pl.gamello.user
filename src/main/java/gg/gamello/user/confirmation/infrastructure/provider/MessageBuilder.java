package gg.gamello.user.confirmation.infrastructure.provider;

import gg.gamello.user.confirmation.aplication.command.CreateCommand;

public interface MessageBuilder<T extends Message> {
	public <K extends MessageBuilder> K fromCommand(CreateCommand command);
	public <K extends MessageBuilder> K secret(String secret);
	public <K extends MessageBuilder> K issuer(String issuer);
	public T build();
}
