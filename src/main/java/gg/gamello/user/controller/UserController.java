package gg.gamello.user.controller;

import gg.gamello.user.dao.User;
import gg.gamello.user.exception.UserDoesNotExistsException;
import gg.gamello.user.exception.UserIsNotActiveException;
import gg.gamello.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public User getUser(Authentication authentication) throws UserDoesNotExistsException, UserIsNotActiveException {
        return userService.getUser(User.getFromAuthentication(authentication).getId());
    }

    @GetMapping("/api/{userId}")
    public User getUser(@PathVariable UUID userId) throws UserDoesNotExistsException, UserIsNotActiveException {
        return userService.getUser(userId);
    }

    @PostMapping("/change/language")
    public ResponseEntity<String> changeLanguage(@RequestBody String language, Authentication authentication){
        userService.changeLanguage(authentication, language);
        return ResponseEntity.ok("Language changed");
    }
}