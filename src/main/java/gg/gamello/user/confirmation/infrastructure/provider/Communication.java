package gg.gamello.user.confirmation.infrastructure.provider;

public interface Communication<T extends Message> {
	public <K extends MessageBuilder> K messageBuilder();
	public void send(T message);
}
