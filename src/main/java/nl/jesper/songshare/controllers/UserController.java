package nl.jesper.songshare.controllers;

import nl.jesper.songshare.dto.requests.post.UserRegistrationRequest;
import nl.jesper.songshare.dto.responses.ApiResponse;
import nl.jesper.songshare.exceptions.UsernameAlreadyExistsException;
import nl.jesper.songshare.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            userService.createUser(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Registered user successfully."));
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Username '" + request.getUsername() + "' already exists."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while processing your request."));
        }
    }

}
