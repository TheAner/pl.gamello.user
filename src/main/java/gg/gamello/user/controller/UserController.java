package gg.gamello.user.controller;

import gg.gamello.user.dao.User;
import gg.gamello.user.exception.UserDoesNotExistsException;
import gg.gamello.user.exception.UserIsNotActiveException;
import gg.gamello.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) throws UserDoesNotExistsException, UserIsNotActiveException {
        return userService.getUser(userId);
    }
}