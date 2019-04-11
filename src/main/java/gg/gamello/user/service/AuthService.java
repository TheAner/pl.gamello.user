package gg.gamello.user.service;

import gg.gamello.user.dao.Role;
import gg.gamello.user.dao.User;
import gg.gamello.user.dao.type.Language;
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

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       TokenService tokenService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserRegistrationForm registrationForm) throws UserAlreadyExistsException {
        if (userRepository.existsUserByEmailOrUsername(registrationForm.getEmail(), registrationForm.getUsername()))
            throw new UserAlreadyExistsException("User with credentials "  +
                    registrationForm.getEmail() + "/" + registrationForm.getUsername() + " already exists");

        User user = new User(registrationForm.getUsername(), registrationForm.getEmail());
        user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
        user.setRoles(getDefaultRoles());
        user.setLanguage(Language.mapLanguage(registrationForm.getLanguage()));

        userRepository.save(user);

        tokenService.createToken(user.getId(), TokenType.ACTIVATION);
        log.info("Created user with id: " + user.getId());
        return user;
    }

    private List<Role> getDefaultRoles() {
        return Collections.singletonList(roleRepository.findByRole(RoleType.USER));
    }

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

    public void createDeleteRequest(Long userId){
        User user = userRepository.getUserById(userId);

        tokenService.createToken(user.getId(), TokenType.DELETE);
        log.info("Created delete request for user with id:  " + user.getId());
    }

    public void deleteUser(Long userId){
        User user = userRepository.getUserById(userId);
        tokenService.deleteAllTokensForUser(userId);

        userRepository.delete(user);
        log.info("Deleted user with id:  " + user.getId());
    }

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

    public void recoverUser(Long userId, String password) {
        User user = userRepository.getUserById(userId);

        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        log.info("Changed password for user with id:  " + user.getId());
    }

    public void changePassword(Long userId, Passwords passwords) throws PasswordsDontMatchException {
        User user = userRepository.getUserById(userId);

        if (!passwordEncoder.matches(passwords.getOldPassword(), user.getPassword()))
            throw new PasswordsDontMatchException("Passwords don't match");

        user.setPassword(passwordEncoder.encode(passwords.getNewPassword()));

        userRepository.save(user);

        log.info("Changed password for user with id:  " + user.getId());
    }

    public void createEmailChangeRequest(Long userId){
        User user = userRepository.getUserById(userId);

        tokenService.createToken(user.getId(), TokenType.EMAIL);

        log.info("Created email change request for user with id:  " + user.getId());
    }

    public void changeEmail(Long userId, String email) throws UserDoesNotExistsException {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new UserDoesNotExistsException("User with id " + userId +
                        " does not exists"));

        user.setEmail(email);

        userRepository.save(user);

        log.info("Changed email " + user.getEmail() + " for user with id:  " + user.getId());
    }
}
