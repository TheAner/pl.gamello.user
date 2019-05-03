package gg.gamello.user.service;


import gg.gamello.user.dao.Token;
import gg.gamello.user.dao.type.TokenType;
import gg.gamello.user.exception.InvalidTypeOfTokenException;
import gg.gamello.user.exception.OutdatedTokenException;
import gg.gamello.user.exception.TokenException;
import gg.gamello.user.exception.TokenNotFoundException;
import gg.gamello.user.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class TokenService {

    @Value("${token.number-of-sections}")
    private int SECTIONS;
    @Value("${token.chars-length}")
    private int CHARS;

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Token createToken(UUID userId, TokenType tokenType){
        TokenFactory tokenFactory = new TokenFactory(tokenType);
        Token token = tokenFactory.createFor(userId);
        tokenRepository.save(token);

        log.info("Created " + tokenType.name() +
                " token for user with id: " + userId);
        return token;
    }

    public void confirmToken(UUID userId, TokenType tokenType, String tokenValue) throws TokenException {
        Token token = validateToken(userId, tokenType, tokenValue);

        tokenRepository.delete(token);

        log.info("Confirmed " + tokenType.name() +
                " operation for user with id: " + userId);
    }

    public Token validateToken(UUID userId, TokenType tokenType, String tokenValue) throws TokenException{
        Token token = tokenRepository.findByUserIdAndValue(userId, tokenValue)
                .orElseThrow(()-> new TokenNotFoundException("Token: {" + tokenValue +
                        "} for user with id: " + userId +
                        " does not exists"));

        checkTypeCompatibility(tokenType, token);
        checkDateValid(token);
        return token;
    }

    void deleteAllTokensForUser(UUID userId){
        tokenRepository.deleteAllByUserId(userId);
    }

    private void checkDateValid(Token token) throws OutdatedTokenException {
        if(token.getTokenExpirationDate().compareTo(new Date())<0){
            tokenRepository.delete(token);

            throw new OutdatedTokenException("Token with id: " + token.getId() +
                                            ", for operation " + token.getTypeOfToken().name() +
                                            " is outdated");
        }
    }

    private void checkTypeCompatibility(TokenType tokenType, Token token) throws InvalidTypeOfTokenException {
        if(!token.getTypeOfToken().equals(tokenType))
            throw new InvalidTypeOfTokenException("Token type " + tokenType.name() +
                                                ", don't match witch operation " + token.getTypeOfToken().name());
    }

    private class TokenFactory{
        private final TokenType tokenType;

        TokenFactory(TokenType tokenType) {
            this.tokenType = tokenType;
        }

        Token createFor(UUID userId){
            String tokenValue = generateToken(SECTIONS, CHARS);
            Token token = new Token(userId, tokenType, tokenValue);
            token.setTokenExpirationDate(getExpirationDate());
            return token;
        }

        private String generateToken(int sections, int chars){
            StringBuilder token = new StringBuilder();

            for (int i = 0; i < sections; i++) {
                token.append(RandomStringUtils.randomAlphanumeric(chars));

                if (i != (sections-1)) {
                    token.append("-");
                }
            }

            return token.toString();
        }

        private Date getExpirationDate(){
            return new Date(new Date().getTime()+tokenType.getLifespan());
        }
    }
}
