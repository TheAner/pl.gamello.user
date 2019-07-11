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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AvatarService avatarService;

    public UserService(UserRepository userRepository, AvatarService avatarService) {
        this.userRepository = userRepository;
        this.avatarService = avatarService;
    }

    public User getUserById(UUID userId) throws UserDoesNotExistsException, UserIsNotActiveException {
        User user = userRepository.findUserById(userId)
                .orElseThrow(()-> new UserDoesNotExistsException("User with id " + userId +
                        " does not exists"));

        if(!user.isActive())
            throw new UserIsNotActiveException("User with id " + user.getId() +
                    " is not active");

        log.info("Requested user with id: " + user.getId());
        return user;
    }

    public List<UserDetails> getListOfUserDetailsById(List<UUID> userIds){
        List<UserDetails> userDetails = userRepository.findUsersByIdIn(userIds);

        log.info("Requested users with ids: " + userDetails.toString());
        return userDetails;
    }

    public User getUserBySlug(String slug) throws UserDoesNotExistsException {
        User user = userRepository.findUserBySlug(slug)
                .orElseThrow(()-> new UserDoesNotExistsException("User with slug " + slug +
                        " does not exists"));

        log.info("Requested user with slug: " + user.getSlug());
        return user;
    }

    public void changeLanguage(Authentication authentication, String language){
        User user = userRepository.getUserById(User.getFromAuthentication(authentication).getId());
        user.setLanguage(Language.mapLanguage(language));

        userRepository.save(user);
    }

    public void changeAvatar(Authentication authentication, MultipartFile image) throws IOException, InterruptedException {
        User user = User.getFromAuthentication(authentication);

        String location = avatarService.getLocation(user);

        List<Avatar> avatars = avatarService.getListOfAvatars(image);

        avatarService.deleteCurrentAvatars(user);

        avatarService.uploadListOfAvatars(avatars, location);

        user.setAvatarLocation(location);
        userRepository.save(user);
    }
}
