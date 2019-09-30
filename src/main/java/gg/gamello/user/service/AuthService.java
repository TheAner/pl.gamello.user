package gg.gamello.user.service;

import gg.gamello.user.dao.Token;
import gg.gamello.user.dao.User;
import gg.gamello.user.dao.type.TokenType;
import gg.gamello.user.domain.EmailRequest;
import gg.gamello.user.domain.auth.Credentials;
import gg.gamello.user.domain.auth.Passwords;
import gg.gamello.user.exception.PasswordsDontMatchException;
import gg.gamello.user.exception.user.UserDoesNotExistsException;
import gg.gamello.user.exception.user.UserIsNotActiveException;
import gg.gamello.user.provider.EmailProvider;
import gg.gamello.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final EmailProvider emailProvider;
    private final HttpServletRequest httpRequest;

    public AuthService(UserRepository userRepository,
                       TokenService tokenService,
                       PasswordEncoder passwordEncoder,
                       EmailProvider emailProvider,
                       HttpServletRequest httpRequest) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.emailProvider = emailProvider;
        this.httpRequest = httpRequest;
    }

    public void activateUser(UUID userId) {
        User user = userRepository.getUserById(userId);

        user.setActive(true);
        userRepository.save(user);
        log.info("Activated user with id: " + user.getId());
    }

    public User authenticateCredentials(Credentials credentials) throws UserDoesNotExistsException, UserIsNotActiveException {
        User user = userRepository.findUserByCredentials(credentials.getLogin())
                .orElseThrow(() -> new UserDoesNotExistsException("User with username/email " + credentials.getLogin() +
                        " does not exists"));

        if(!user.isActive())
            throw new UserIsNotActiveException("User with username " + user.getUsername() +
                    " is not active");

        if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword()))
            throw new UserDoesNotExistsException("User with username/email " + credentials.getLogin() +
                                                    " does not exists");

        return user;
    }

    @Transactional
    public void createDeleteRequest(Authentication authentication){
        User user = userRepository.getUserById(User.getFromAuthentication(authentication).getId());

        Token token = tokenService.createToken(user.getId(), TokenType.DELETE);

        processEmail(user, token);

        log.info("Created delete request for user with id:  " + user.getId());
    }

    @Transactional
    public void deleteUser(UUID userId){
        User user = userRepository.getUserById(userId);
        tokenService.deleteAllTokensForUser(userId);

        userRepository.delete(user);
        log.info("Deleted user with id:  " + user.getId());
    }

    @Transactional
    public void createRecoverRequest(String email){
        try {
            User user = userRepository.findUserByEmail(email)
                    .orElseThrow(() -> new UserDoesNotExistsException("User with email " + email +
                            " does not exists"));
            if(!user.isActive())
                throw new UserIsNotActiveException("User with email " + email +
                        " is not active");

            Token token = tokenService.createToken(user.getId(), TokenType.PASSWORD);

            processEmail(user, token);

            log.info("Created recover request for user with id:  " + user.getId());

        } catch (UserDoesNotExistsException | UserIsNotActiveException e) {
            log.error(e.getMessage());
        }
    }

    public void recoverUser(UUID userId, String password) {
        User user = userRepository.getUserById(userId);

        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        log.info("Changed password for user with id:  " + user.getId());
    }

    public void changePassword(Authentication authentication, Passwords passwords) throws PasswordsDontMatchException {
        User user = userRepository.getUserById(User.getFromAuthentication(authentication).getId());

        if (!passwordEncoder.matches(passwords.getOldPassword(), user.getPassword()))
            throw new PasswordsDontMatchException("Passwords don't match");

        EmailRequest emailRequest = EmailRequest.createMailForUser(user)
                .addIssuer(httpRequest.getRemoteUser())
                .useTemplate("user.password.changed");
        emailProvider.sendEmail(emailRequest);

        user.setPassword(passwordEncoder.encode(passwords.getNewPassword()));

        userRepository.save(user);

        log.info("Changed password for user with id:  " + user.getId());
    }

    @Transactional
    public void createEmailChangeRequest(String email, Authentication authentication){
        User user = userRepository.getUserById(User.getFromAuthentication(authentication).getId());

        Token token = tokenService.createToken(user.getId(), TokenType.EMAIL);

        processEmail(user, email, token);

        log.info("Created email change request for user with id:  " + user.getId());
    }

    public void changeEmail(UUID userId, String email) throws UserDoesNotExistsException {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new UserDoesNotExistsException("User with id " + userId +
                        " does not exists"));

        EmailRequest emailRequest = EmailRequest.createMailForUser(user)
                .addIssuer(httpRequest.getRemoteUser())
                .useTemplate("user.email.changed")
                .addData("newEmail", email);
        emailProvider.sendEmail(emailRequest);

        user.setEmail(email);

        userRepository.save(user);

        log.info("Changed email " + user.getEmail() + " for user with id:  " + user.getId());
    }

    private void processEmail(User user, Token token) {
        EmailRequest emailRequest = EmailRequest.createMailForUser(user)
                .useTemplateForToken(token);

        emailProvider.sendEmail(emailRequest);
    }

    private void processEmail(User user, String email, Token token) {
        EmailRequest emailRequest = EmailRequest.createMailForUser(user, email)
                .useTemplateForToken(token);

        emailProvider.sendEmail(emailRequest);
    }
}
