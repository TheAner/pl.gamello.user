package gg.gamello.user.service;

import gg.gamello.user.dao.User;
import gg.gamello.user.exception.UserDoesNotExistsException;
import gg.gamello.user.exception.UserIsNotActiveException;
import gg.gamello.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Long userId) throws UserDoesNotExistsException, UserIsNotActiveException {
        User user = userRepository.findUserById(userId)
                .orElseThrow(()-> new UserDoesNotExistsException("User with id " + userId +
                        " does not exists"));

        if(!user.isActive())
            throw new UserIsNotActiveException("User with id " + user.getId() +
                    " is not active");

        log.info("Requested user with id: " + user.getId());
        return user;
    }
}
