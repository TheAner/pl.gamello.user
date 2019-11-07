package gg.gamello.user.confirmation.infrastructure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("confirmation.email")
public class EmailProperties {

	/**
	 * Token generator configuration
	 */
	private Token token = new Token();

	@Data
	public static class Token {

		/**
		 * Number of sections in token
		 */
		private int sections = 4;

		/**
		 * Number of characters in section
		 */
		private int chars = 8;
	}
}
