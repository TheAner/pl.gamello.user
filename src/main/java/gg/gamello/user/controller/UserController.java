package gg.gamello.user.controller;

import gg.gamello.user.dao.User;
import gg.gamello.user.domain.UserDetails;
import gg.gamello.user.exception.user.UserDoesNotExistsException;
import gg.gamello.user.exception.user.UserIsNotActiveException;
import gg.gamello.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public ResponseEntity<User> getUser(Authentication authentication) throws UserDoesNotExistsException, UserIsNotActiveException {
        return ResponseEntity.ok(
                userService.getUserById(User.getFromAuthentication(authentication).getId()));
    }

    @GetMapping("/id")
    public ResponseEntity<List<UserDetails>> getListOfUserDetailsById(@RequestParam List<UUID> userIds) {
        return ResponseEntity.ok(userService.getListOfUserDetailsById(userIds));
    }

    @GetMapping("/special/{slug}")
    public ResponseEntity<User> getUserBySlug(@PathVariable String slug) throws UserDoesNotExistsException {
        return ResponseEntity.ok(userService.getUserBySlug(slug));
    }

    @GetMapping("/api/{userId}")
    public User getUser(@PathVariable UUID userId) throws UserDoesNotExistsException, UserIsNotActiveException {
        return userService.getUserById(userId);
    }

    @PostMapping("/change/language")
    public ResponseEntity<String> changeLanguage(@RequestBody String language, Authentication authentication){
        userService.changeLanguage(authentication, language);
        return ResponseEntity.ok("Language changed");
    }

    @PostMapping("/change/avatar")
    public ResponseEntity<String> changeAvatar(@RequestParam("file") MultipartFile avatar, Authentication authentication) throws IOException, InterruptedException {
        userService.changeAvatar(authentication, avatar);
        return ResponseEntity.ok("Avatar updated");
    }
}