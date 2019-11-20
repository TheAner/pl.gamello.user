package gg.gamello.user.command.avatar.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("avatar")
public class AvatarProperties {

	/**
	 * Name of the bucket where avatars are stored
	 */
	private String bucketName;

	/**
	 * Localization in bucket where avatars are stored
	 */
	private String avatarsLocation;

	/**
	 * Localization of default avatar
	 */
	private String defaultAvatarLocation;
}
