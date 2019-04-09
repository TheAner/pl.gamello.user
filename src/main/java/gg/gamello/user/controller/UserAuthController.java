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
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;

@RestController
public class UserAuthController {
    @Autowired
    AuthService authService;

    @Autowired
    TokenService tokenService;

    @Resource(name = "requestUser")
    User requestUser;

    @PostMapping("/")
    public ResponseEntity<String> registerAccount(@RequestBody UserRegistrationForm registrationForm) throws UserAlreadyExistsException, RestClientException {
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

    @DeleteMapping("/")
    public ResponseEntity<String> createDeleteRequest() {
        authService.createDeleteRequest(requestUser.getId());
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
    public ResponseEntity<String> createRecoverRequest(@RequestBody String email){
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

    @PostMapping("/change/password")
    public ResponseEntity<String> changePassword(@RequestBody Passwords passwords) throws PasswordsDontMatchException {
        authService.changePassword(requestUser.getId(), passwords);
        return ResponseEntity.ok("Password changed");
    }

    @PostMapping("/change/email")
    public ResponseEntity<String> createEmailChangeRequest(@RequestBody String email){
        authService.createEmailChangeRequest(requestUser.getId());
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

    @PostMapping("/validate/{typeOfToken}/{userId}")
    public ResponseEntity<String> validateToken(@PathVariable String typeOfToken,
                                                @PathVariable Long userId,
                                                @RequestParam(value = "token") String token) throws IllegalArgumentException, TokenException {
        TokenType tokenType = TokenType.valueOf(typeOfToken);
        tokenService.confirmToken(userId, tokenType, token);
        return ResponseEntity.ok("Correct token");
    }

    @PostMapping("/api/authenticate")
    public User authenticateCredentials(@RequestBody Credentials credentials) throws UserDoesNotExistsException, UserIsNotActiveException {
        return authService.authenticateCredentials(credentials);
    }
}
