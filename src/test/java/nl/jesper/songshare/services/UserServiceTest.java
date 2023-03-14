package nl.jesper.songshare.services;

import nl.jesper.songshare.entities.UserEntity;
import nl.jesper.songshare.exceptions.UsernameAlreadyExistsException;
import nl.jesper.songshare.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Zorgt er voor dat ik met @Order() een volgorde kan aangeven.
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    PasswordService passwordService;

    @Test
//    @Order(1)
    void createUserTest() {
        // Op-zet
        String username = "testuser1";
        String password = "1234";

        // Voorbereiden
        userService.createUser(username, password);
        UserEntity user = userRepository.findUserEntityByUsername(username);

        // Testen
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertTrue(passwordService.checkPassword(password, user.getPassword()));
    }

    @Test
//    @Order(2)
    public void createExistingUserThrowsExceptionTest() {
        // Op-zet
        String username = "testuser2";
        String password = "1234";

        // Voorbereiden
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername(username);
        existingUser.setPassword(passwordService.hashPassword(password));
        userRepository.save(existingUser);

        // Testen
        Throwable exception = assertThrows(UsernameAlreadyExistsException.class, () -> {
            userService.createUser(username, passwordService.hashPassword(password)); // Hier probeert ie nog n keer dezelfde gebruikersnaam aan te maken
        });
        assertEquals("Username '" + username + "' already exists.", exception.getMessage());
    }


}