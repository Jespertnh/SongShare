package nl.jesper.songshare.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PasswordEncoderTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void hashAndCheckPasswordTest() {
        // Op-zet
        String unhashedPassword = "1234";
        String hashedPassword = passwordEncoder.encode(unhashedPassword);

        // Testen
        assertTrue(passwordEncoder.matches(unhashedPassword, hashedPassword));
    }
}