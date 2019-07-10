package gg.gamello.user.controller;

import gg.gamello.user.dao.Profile;
import gg.gamello.user.exception.ProfileDoesNotExistsException;
import gg.gamello.user.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController @RequestMapping("/profile")
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @GetMapping("/")
    public ResponseEntity<Profile> getLoggedUserProfile(Authentication authentication){
        Profile profile = profileService.getProfileByLoggedUser(authentication);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<Profile> getProfileById(@PathVariable UUID userId) throws ProfileDoesNotExistsException {
        Profile profile = profileService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/special/{slug}")
    public ResponseEntity<Profile> getProfileBySlug(@PathVariable String slug) throws ProfileDoesNotExistsException {
        Profile profile = profileService.getProfile(slug);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/change/avatar")
    public ResponseEntity<String> changeAvatar(@RequestParam("file") MultipartFile avatar, Authentication authentication) throws IOException, InterruptedException {
        profileService.changeAvatar(authentication, avatar);
        return ResponseEntity.ok("Avatar updated");
    }
}
