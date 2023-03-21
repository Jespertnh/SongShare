package nl.jesper.songshare.controllers;

import nl.jesper.songshare.exceptions.UsernameAlreadyExistsException;
import nl.jesper.songshare.dto.requests.post.UserRegistrationRequest;
import nl.jesper.songshare.dto.responses.BadResponse;
import nl.jesper.songshare.dto.responses.SuccessResponse;
import nl.jesper.songshare.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for handling HTTP requests regarding user accounts.
 */
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/users/register", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            userService.createUser(request.getUsername(), request.getPassword());
//            return ResponseEntity.ok("Success registering user");
            return ResponseEntity.ok(new SuccessResponse("Registered user successfully."));
        } catch (UsernameAlreadyExistsException usernameAlreadyExistsException) {
            return ResponseEntity.badRequest().body(new BadResponse("Username '" + request.getUsername() + "' already exists."));
        }
    }


}
