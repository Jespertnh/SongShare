package nl.jesper.songshare.services;

import nl.jesper.songshare.entities.UserEntity;
import nl.jesper.songshare.exceptions.UsernameAlreadyExistsException;
import nl.jesper.songshare.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service that handles the logic of user stuff.
 */
@Service
public class UserService {
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private UserRepository userRepository;

    /**
     *
     * @param username The username you want the user to have.
     * @param password The unhashed password you want the user to have.
     * @throws UsernameAlreadyExistsException If the username is already taken.
     */
    public void createUser(String username, String password) throws UsernameAlreadyExistsException {
        UserEntity user = userRepository.findUserEntityByUsername(username);
        if (user != null) {
            // Username bestaat al.
            throw new UsernameAlreadyExistsException("Username '" + username + "' already exists.");
        } else {
            // Username bestaat niet, maak nieuwe gebruiker aan.
            user = new UserEntity();
            user.setUsername(username);
            user.setPassword(passwordService.hashPassword(password));
            userRepository.save(user);
//            return userRepository.save(user); // Dit zou alleen zo moeten zijn als deze functie niet void was maar een UserEntity terug zou geven.
        }
    }

    /**
     *
     * @param username the username of the UserEntity you're trying to retrieve.
     * @param unhashedPassword the unhashed password of the UserEntity you're trying to retrieve.
     * @return The UserEntity if password is correct and null if incorrect.
     */
    public Optional<UserEntity> login(String username, String unhashedPassword) {
        UserEntity user = userRepository.findUserEntityByUsername(username);

        if (passwordService.checkPassword(unhashedPassword,user.getPassword())) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }
}
