package gg.gamello.user.confirmation.infrastructure.provider;

public interface Communication<T extends Message> {
	<K extends MessageBuilder> K messageBuilder();
	void send(T message);
}
