package gg.gamello.user.provider;

import gg.gamello.user.domain.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailProvider {

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> sendEmail(EmailRequest emailRequest) {
        return restTemplate.postForEntity("http://email/api/transactional/single", emailRequest, String.class);
    }
}
