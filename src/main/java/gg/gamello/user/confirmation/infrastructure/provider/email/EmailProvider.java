package gg.gamello.user.confirmation.infrastructure.provider.email;

import gg.gamello.user.confirmation.infrastructure.properties.EmailProperties;
import gg.gamello.user.confirmation.infrastructure.provider.Communication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class EmailProvider implements Communication<EmailMessage> {

	@Autowired
	private EmailProperties properties;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public EmailMessage.Builder messageBuilder() {
		return new EmailMessage.Builder();
	}

	@Override
	public void send(EmailMessage message) throws RestClientException {
		if (properties.isEnabled())
			restTemplate.postForEntity(properties.getServiceUrl(), message, String.class);
		else log.info(message.toString());
	}
}
