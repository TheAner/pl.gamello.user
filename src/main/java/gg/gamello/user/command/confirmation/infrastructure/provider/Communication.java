package gg.gamello.user.command.confirmation.infrastructure.provider;

public interface Communication<T extends Message> {
	<K extends MessageBuilder> K messageBuilder();

	void send(T message);
}
