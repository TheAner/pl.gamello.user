package gg.gamello.user.core.infrastructure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Data
@Component
@ConfigurationProperties("aws")
public class AwsProperties {

	/**
	 * AWS IAM access key
	 */
	private String access;

	/**
	 * AWS IAM secret key
	 */
	private String secret;

	/**
	 * AWS regions per service
	 */
	private Map<String, String> regions;

	public String getRegion(String service) {
		return Optional.ofNullable(regions.get(service))
				.orElseThrow(() -> new RuntimeException("Missing region for " + service));
	}
}
