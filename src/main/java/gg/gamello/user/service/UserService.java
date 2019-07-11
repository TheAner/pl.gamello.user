package gg.gamello.user.service;

import gg.gamello.user.dao.User;
import gg.gamello.user.dao.type.Language;
import gg.gamello.user.domain.UserDetails;
import gg.gamello.user.domain.avatar.Avatar;
import gg.gamello.user.exception.user.UserDoesNotExistsException;
import gg.gamello.user.exception.user.UserIsNotActiveException;
import gg.gamello.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(UUID userId) throws UserDoesNotExistsException, UserIsNotActiveException {
        User user = userRepository.findUserById(userId)
                .orElseThrow(()-> new UserDoesNotExistsException("User with id " + userId +
                        " does not exists"));

        if(!user.isActive())
            throw new UserIsNotActiveException("User with id " + user.getId() +
                    " is not active");

        log.info("Requested user with id: " + user.getId());
        return user;
    }

    public void changeLanguage(Authentication authentication, String language){
        User user = userRepository.getUserById(User.getFromAuthentication(authentication).getId());
        user.setLanguage(Language.mapLanguage(language));

        userRepository.save(user);
    }
}
