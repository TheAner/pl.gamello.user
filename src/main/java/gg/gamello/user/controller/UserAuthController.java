package gg.gamello.user.controller;

import gg.gamello.user.dao.User;
import gg.gamello.user.dao.type.TokenType;
import gg.gamello.user.domain.Credentials;
import gg.gamello.user.domain.Passwords;
import gg.gamello.user.domain.UserRegistrationForm;
import gg.gamello.user.exception.*;
import gg.gamello.user.service.AuthService;
import gg.gamello.user.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class UserAuthController {
    @Autowired
    AuthService authService;

    @Autowired
    TokenService tokenService;

    @PostMapping()
    public ResponseEntity<String> registerAccount(@RequestBody UserRegistrationForm registrationForm) throws UserAlreadyExistsException {
        User user = authService.createUser(registrationForm);
        return ResponseEntity
                .created(UriComponentsBuilder
                        .fromUriString("/user/{id}")
                        .build(user.getId()))
                .body("User created");
    }

    @PostMapping("/confirm/{userId}")
    public ResponseEntity<String> confirmAccount(@PathVariable Long userId,
                                                 @RequestParam(value = "token") String token) throws TokenException {
        tokenService.confirmToken(userId, TokenType.ACTIVATION, token);
        authService.activateUser(userId);
        return ResponseEntity.ok("User confirmed");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> createDeleteRequest(@PathVariable Long userId) throws UserDoesNotExistsException {
        authService.createDeleteRequest(userId);
        return ResponseEntity.ok("User delete request sent");
    }

    @DeleteMapping("/confirm/{userId}")
    public ResponseEntity<String> confirmDeleteAccount(@PathVariable Long userId,
                                                       @RequestParam(value = "token") String token) throws TokenException {
        tokenService.confirmToken(userId, TokenType.DELETE, token);
        authService.deleteUser(userId);
        return ResponseEntity.ok("User deleted");
    }

    @PostMapping("/recover")
    public ResponseEntity<String> createRecoverRequest(@RequestBody String email) throws UserDoesNotExistsException, UserIsNotActiveException {
        authService.createRecoverRequest(email);
        return ResponseEntity.ok("User recover request sent");
    }

    @PostMapping("/confirm/password/{userId}")
    public ResponseEntity<String> confirmPasswordRecover(@PathVariable Long userId,
                                                         @RequestParam(value = "token") String token,
                                                         @RequestBody String password) throws TokenException {
        tokenService.confirmToken(userId, TokenType.PASSWORD, token);
        authService.recoverUser(userId, password);
        return ResponseEntity.ok("Password changed");
    }

    @PostMapping("/change/password/{userId}")
    public ResponseEntity<String> changePassword(@PathVariable Long userId,
                                                 @RequestBody Passwords passwords) throws PasswordsDontMatchException, UserDoesNotExistsException {
        authService.changePassword(userId, passwords);
        return ResponseEntity.ok("Password changed");
    }

    @PostMapping("/change/email/{userId}")
    public ResponseEntity<String> createEmailChangeRequest(@PathVariable Long userId) throws UserDoesNotExistsException {
        authService.createEmailChangeRequest(userId);
        return ResponseEntity.ok("User email change request sent");
    }

    @PostMapping("/confirm/email/{userId}")
    public ResponseEntity<String> confirmEmailChange(@PathVariable Long userId,
                                                     @RequestParam(value = "email") String email,
                                                     @RequestParam(value = "token") String token) throws TokenException, UserDoesNotExistsException {
        tokenService.confirmToken(userId, TokenType.EMAIL, token);
        authService.changeEmail(userId, email);
        return ResponseEntity.ok("Email changed");
    }

    @PostMapping("/authenticate")
    public User authenticateCredentials(@RequestBody Credentials credentials) throws UserDoesNotExistsException, UserIsNotActiveException {
        return authService.authenticateCredentials(credentials);
    }
}
