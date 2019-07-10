package gg.gamello.user.service;

import gg.gamello.user.dao.Profile;
import gg.gamello.user.dao.User;
import gg.gamello.user.domain.Avatar;
import gg.gamello.user.exception.ProfileDoesNotExistsException;
import gg.gamello.user.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AvatarService avatarService;

    public ProfileService(ProfileRepository profileRepository,
                          AvatarService avatarService) {
        this.profileRepository = profileRepository;
        this.avatarService = avatarService;
    }

    public Profile getProfile(UUID userId) throws ProfileDoesNotExistsException {
        return profileRepository.findProfileById(userId)
                .orElseThrow(()-> new ProfileDoesNotExistsException("Profile with id " + userId + " does not exists"));
    }

    public Profile getProfile(String slug) throws ProfileDoesNotExistsException {
        return profileRepository.findProfileBySlug(slug)
                .orElseThrow(()-> new ProfileDoesNotExistsException("Profile with slug " + slug + " does not exists"));
    }

    public Profile getProfileByLoggedUser(Authentication authentication){
        User user = User.getFromAuthentication(authentication);
        return profileRepository.findProfileById(user.getId())
                .orElseGet(()->createNewProfileForUser(user));
    }

    private Profile createNewProfileForUser(User user) {
        Profile profile = new Profile(user.getId(), user.getUsername());
        profile.setAvatarLocation(avatarService.getDefaultAvatar());
        profileRepository.save(profile);

        log.info("Created profile for user with id: " + profile.getId());
        return profile;
    }

    public void changeAvatar(Authentication authentication, MultipartFile image) throws IOException, InterruptedException {
        Profile profile = this.getProfileByLoggedUser(authentication);

        String location = avatarService.getLocation(User.getFromAuthentication(authentication));

        List<Avatar> avatars = avatarService.getListOfAvatars(image);

        avatarService.deleteCurrentAvatars(profile);

        avatarService.uploadListOfAvatars(avatars, location);

        profile.setAvatarLocation(location);
        profileRepository.save(profile);
    }
}
