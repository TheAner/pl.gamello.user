package gg.gamello.user.confirmation.infrastructure.provider;

import java.util.Map;
import java.util.UUID;

public abstract class Message {
	private UUID userId;
	private String language;
	private Map<String, String> data;
}
