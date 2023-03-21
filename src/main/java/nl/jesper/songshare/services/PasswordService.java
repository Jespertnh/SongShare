package nl.jesper.songshare.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service that handles password hashing & matching.
 */
@Service
public class PasswordService {

    /**
     * Hashes an unhashed password String.
     * @param unhashedPassword The unhashed password String.
     * @return The hashed password String.
     */
    public String hashPassword(String unhashedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(unhashedPassword);
    }

    /**
     * Matches an unhashed password against the hash.
     * @param unhashedPassword The unhashed password String.
     * @param hashedPassword The hashed password String (generated by using the hashPassword method).
     * @return True if the password matches the hash. False if it doesn't match.
     */
    public Boolean checkPassword(String unhashedPassword, String hashedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(unhashedPassword, hashedPassword);
    }
}
