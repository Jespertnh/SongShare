package nl.jesper.songshare.controllers;

import nl.jesper.songshare.securitylayerJwt.dto.LoginDto;
import nl.jesper.songshare.securitylayerJwt.dto.RegisterDto;
import nl.jesper.songshare.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling HTTP requests regarding user accounts.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerDto) {
//        userService.register(registerDto);
//        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Registered user successfully."));
        return userService.register(registerDto);
    }

    /**
     * The REST-endpoint for authenticating.
     * @param loginDto The login information in JSON format.
     * @return JWT-token.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        return userService.authenticate(loginDto);
    }



}
