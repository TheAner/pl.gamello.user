package gg.gamello.user.domain;

import gg.gamello.user.dao.Token;
import gg.gamello.user.dao.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Setter(AccessLevel.PRIVATE)
@Data
public class EmailRequest {

    private UUID userId;
    private String name;
    private String email;
    private String template;
    private String language;
    private String issuer;
    private Map<String, String> data;

    public static EmailRequest createMailForUser(User user){
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setUserId(user.getId());
        emailRequest.setName(user.getUsername());
        emailRequest.setEmail(user.getEmail());
        emailRequest.setLanguage(user.getLanguage().toString());
        emailRequest.data = new HashMap<>();
        return emailRequest;
    }

    public static EmailRequest createMailForUser(User user, String email){
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setUserId(user.getId());
        emailRequest.setEmail(email);
        emailRequest.setLanguage(user.getLanguage().toString());
        emailRequest.data = new HashMap<>();
        return emailRequest;
    }

    public EmailRequest addData(String key, String value){
        this.data.put(key, value);
        return this;
    }

    public EmailRequest addIssuer(String issuer){
        this.setIssuer(issuer);
        return this;
    }

    public EmailRequest useTemplateForToken(Token token){
        this.setTemplate("user." + token.getTypeOfToken().toString().toLowerCase());
        this.addData("token", token.getValue());
        return this;
    }

    public EmailRequest useTemplate(String template){
        this.setTemplate(template);
        return this;
    }
}
