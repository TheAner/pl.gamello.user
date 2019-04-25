package gg.gamello.user.controller;

import gg.gamello.user.dao.User;
import gg.gamello.user.exception.UserDoesNotExistsException;
import gg.gamello.user.exception.UserIsNotActiveException;
import gg.gamello.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;


@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Resource(name = "requestUser")
    User requestUser;

    @GetMapping("/")
    public User getUser() throws UserDoesNotExistsException, UserIsNotActiveException {
        return userService.getUser(requestUser.getId());
    }

    @GetMapping("/api/{userId}")
    public User getUser(@PathVariable UUID userId) throws UserDoesNotExistsException, UserIsNotActiveException {
        return userService.getUser(userId);
    }

    @PostMapping("/change/language")
    public ResponseEntity<String> changeLanguage(@RequestBody String language){
        userService.changeLanguage(requestUser.getId(), language);
        return ResponseEntity.ok("Language changed");
    }
}