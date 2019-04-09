package gg.gamello.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gg.gamello.user.dao.User;
import gg.gamello.user.dao.type.RoleType;
import gg.gamello.user.dao.type.TokenType;
import gg.gamello.user.domain.Credentials;
import gg.gamello.user.domain.Passwords;
import gg.gamello.user.domain.UserRegistrationForm;
import gg.gamello.user.exception.PasswordsDontMatchException;
import gg.gamello.user.exception.UserAlreadyExistsException;
import gg.gamello.user.exception.UserDoesNotExistsException;
import gg.gamello.user.exception.UserIsNotActiveException;
import gg.gamello.user.repository.RoleRepository;
import gg.gamello.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private RestTemplate restTemplate;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       TokenService tokenService,
                       PasswordEncoder passwordEncoder,
                       RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    @Transactional//(rollbackFor = {UnknownHostException.class})
    public User createUser(UserRegistrationForm registrationForm) throws UserAlreadyExistsException, UnknownHostException {
        if (userRepository.existsUserByEmailOrUsername(registrationForm.getEmail(), registrationForm.getUsername()))
            throw new UserAlreadyExistsException("User with credentials "  +
                                                    registrationForm.getEmail() + "/" + registrationForm.getUsername() + " already exists");

        User user = new User(registrationForm.getUsername(), registrationForm.getEmail());
        user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
        user.setRoles(Collections.singletonList(roleRepository.findByRole(RoleType.USER)));

        userRepository.save(user);

        createAndSaveProfile(user);

        tokenService.createToken(user.getId(), TokenType.ACTIVATION);
        log.info("Created user with id: " + user.getId());

        return user;
    }

    public void createAndSaveProfile(User user) throws UnknownHostException {
        ObjectNode profile = new ObjectMapper().createObjectNode();
        profile.put("userId", user.getId());
        profile.put("visibleName", user.getUsername());
        restTemplate.postForLocation("http://profile/api", profile);
    }

    @Transactional
    public void activateUser(Long userId) {
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
    public void createDeleteRequest(Long userId){
        User user = userRepository.getUserById(userId);

        tokenService.createToken(user.getId(), TokenType.DELETE);
        log.info("Created delete request for user with id:  " + user.getId());
    }

    @Transactional
    public void deleteUser(Long userId){
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

            tokenService.createToken(user.getId(), TokenType.PASSWORD);
            log.info("Created recover request for user with id:  " + user.getId());

        } catch (UserDoesNotExistsException | UserIsNotActiveException e) {
            log.error(e.getMessage());
        }
    }

    @Transactional
    public void recoverUser(Long userId, String password) {
        User user = userRepository.getUserById(userId);

        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        log.info("Changed password for user with id:  " + user.getId());
    }

    @Transactional
    public void changePassword(Long userId, Passwords passwords) throws PasswordsDontMatchException {
        User user = userRepository.getUserById(userId);

        if (!passwordEncoder.matches(passwords.getOldPassword(), user.getPassword()))
            throw new PasswordsDontMatchException("Passwords don't match");

        user.setPassword(passwordEncoder.encode(passwords.getNewPassword()));

        userRepository.save(user);

        log.info("Changed password for user with id:  " + user.getId());
    }

    @Transactional
    public void createEmailChangeRequest(Long userId){
        User user = userRepository.getUserById(userId);

        tokenService.createToken(user.getId(), TokenType.EMAIL);

        log.info("Created email change request for user with id:  " + user.getId());
    }

    @Transactional
    public void changeEmail(Long userId, String email) throws UserDoesNotExistsException {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new UserDoesNotExistsException("User with id " + userId +
                        " does not exists"));

        user.setEmail(email);

        userRepository.save(user);

        log.info("Changed email " + user.getEmail() + " for user with id:  " + user.getId());
    }
}
