package nl.jesper.songshare.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PasswordServiceTest {

    @Autowired
    PasswordService passwordService;

    @Test
    void hashAndCheckPasswordTest() {
        // Op-zet
        String unhashedPassword = "1234";
        String hashedPassword = passwordService.hashPassword(unhashedPassword);

        // Testen
        assertTrue(passwordService.checkPassword(unhashedPassword, hashedPassword));
    }
}