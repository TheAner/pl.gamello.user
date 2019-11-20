package gg.gamello.user.command.confirmation.infrastructure.provider;

import gg.gamello.user.command.confirmation.aplication.command.CreateCommand;

public interface MessageBuilder<T extends Message> {
	<K extends MessageBuilder> K fromCommand(CreateCommand command);

	<K extends MessageBuilder> K secret(String secret);

	<K extends MessageBuilder> K withIssuer(String issuer);

	T build();
}
