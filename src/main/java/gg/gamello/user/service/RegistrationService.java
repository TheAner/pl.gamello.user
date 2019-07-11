package gg.gamello.user.service;

import gg.gamello.user.dao.Role;
import gg.gamello.user.dao.Token;
import gg.gamello.user.dao.User;
import gg.gamello.user.dao.type.Language;
import gg.gamello.user.dao.type.RoleType;
import gg.gamello.user.dao.type.TokenType;
import gg.gamello.user.domain.EmailRequest;
import gg.gamello.user.domain.auth.UserRegistrationForm;
import gg.gamello.user.exception.user.UserAlreadyExistsException;
import gg.gamello.user.provider.EmailProvider;
import gg.gamello.user.repository.RoleRepository;
import gg.gamello.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final AvatarService avatarService;
    private final EmailProvider emailProvider;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository,
                               RoleRepository roleRepository,
                               TokenService tokenService,
                               AvatarService avatarService, EmailProvider emailProvider,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenService = tokenService;
        this.avatarService = avatarService;
        this.emailProvider = emailProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(UserRegistrationForm registrationForm) throws UserAlreadyExistsException {
        if (userRepository.existsUserByEmailOrUsername(registrationForm.getEmail(), registrationForm.getUsername()))
            throw new UserAlreadyExistsException("User with credentials "  +
                    registrationForm.getEmail() + "/" + registrationForm.getUsername() + " already exists");

        User user = new User(registrationForm.getUsername(), registrationForm.getEmail());
        user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
        user.setRoles(getDefaultRoles());
        user.setLanguage(Language.mapLanguage(registrationForm.getLanguage()));
        user.setAvatarLocation(avatarService.getDefaultAvatar());

        userRepository.save(user);

        Token token = tokenService.createToken(user.getId(), TokenType.ACTIVATION);

        processEmail(user, token);

        log.info("Created user with id: " + user.getId());

        return user;
    }

    private void processEmail(User user, Token token) {
        EmailRequest emailRequest = EmailRequest.createMailForUser(user)
                .useTemplateForToken(token)
                .addData("username", user.getUsername());

        emailProvider.sendEmail(emailRequest);
    }

    private List<Role> getDefaultRoles() {
        return Collections.singletonList(roleRepository.findByRole(RoleType.USER));
    }
}