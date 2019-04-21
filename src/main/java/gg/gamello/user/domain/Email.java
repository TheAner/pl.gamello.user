package gg.gamello.user.domain;

import gg.gamello.user.dao.Token;
import gg.gamello.user.dao.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Setter(AccessLevel.PRIVATE)
@Data
public class Email {

    private Long userId;
    private String email;
    private String template;
    private String language;
    private Map<String, String> dataset;

    public static Email createMailForUser(User user){
        Email email = new Email();
        email.setUserId(user.getId());
        email.setEmail(user.getEmail());
        email.setLanguage(user.getLanguage().toString());
        email.dataset = new HashMap<>();
        return email;
    }

    public Email addData(String key, String value){
        this.dataset.put(key, value);
        return this;
    }

    public Email useTemplateForToken(Token token){
        this.setTemplate("user." + token.getTypeOfToken().toString().toLowerCase());
        this.addData("token", token.getValue());
        return this;
    }


    public ResponseEntity sendWithLoadBalance(RestTemplate restTemplate){
        return restTemplate.postForEntity("http://email/api/transactional/single", this, String.class);
    }
}
