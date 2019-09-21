package gg.gamello.user.provider;

import gg.gamello.user.domain.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailProvider {

    @Value("${email.enabled:true}")
    private boolean emailEnabled;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> sendEmail(EmailRequest emailRequest) {
        if (emailEnabled)
            return restTemplate.postForEntity("http://email/api/transactional/single", emailRequest, String.class);
        return ResponseEntity.ok(emailRequest.toString());
    }
}
