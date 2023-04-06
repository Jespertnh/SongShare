package nl.jesper.songshare.services;

import nl.jesper.songshare.entities.UserEntity;
import nl.jesper.songshare.repositories.RoleRepository;
import nl.jesper.songshare.repositories.UserRepository;
import nl.jesper.songshare.securitylayerJwt.businessLogic.IUserService;
import nl.jesper.songshare.securitylayerJwt.dto.BearerToken;
import nl.jesper.songshare.securitylayerJwt.dto.LoginDto;
import nl.jesper.songshare.securitylayerJwt.dto.RegisterDto;
import nl.jesper.songshare.securitylayerJwt.models.Role;
import nl.jesper.songshare.securitylayerJwt.models.RoleName;
import nl.jesper.songshare.securitylayerJwt.security.JwtUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service that handles the logic of user stuff.
 */
@Service
//@Transactional
public class UserService implements IUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final JwtUtilities jwtUtilities;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, RoleRepository roleRepository, JwtUtilities jwtUtilities) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.jwtUtilities = jwtUtilities;
    }


//    public void createUser(RegisterDto registerDto) throws UsernameAlreadyExistsException {
//        String chosenUsername = registerDto.getUsername();
//        String hashedChosenPassword = passwordEncoder.encode(registerDto.getPassword());
//
//        UserEntity user = userRepository.findUserEntityByUsername(chosenUsername);
//        if (user != null) {
//            // Username bestaat al.
//            throw new UsernameAlreadyExistsException("Username '" + chosenUsername + "' already exists.");
//        } else {
//            // Standaard rol = USER
//            Role role = roleRepository.findByRoleName(RoleName.USER);
//
//            // Username bestaat niet, maak nieuwe gebruiker aan.
//            user = new UserEntity();
//            user.setUsername(chosenUsername);
//            user.setPassword(hashedChosenPassword);
//            user.setRoles(Collections.singletonList(role));
//            userRepository.save(user);
//        }
//    }

//    /**
//     *
//     * @param username the username of the UserEntity you're trying to retrieve.
//     * @param unhashedPassword the unhashed password of the UserEntity you're trying to retrieve.
//     * @return The UserEntity if password is correct and null if incorrect.
//     */
//    public Optional<UserEntity> login(String username, String unhashedPassword) {
//        UserEntity user = userRepository.findUserEntityByUsername(username);
//
//        if (passwordEncoder.matches(unhashedPassword,user.getPassword())) {
//            return Optional.of(user);
//        } else {
//            return Optional.empty();
//        }
//    }

    @Override
    public String authenticate(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserEntity user = userRepository.findUserEntityByUsername(authentication.getName());
        List<String> rolesNames = new ArrayList<>();
        user.getRoles().forEach(r -> rolesNames.add(r.getRoleName()));
        String token = jwtUtilities.generateToken(user.getUsername(), rolesNames);
        return token;
    }

    @Override
    public ResponseEntity<?> register(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.CONFLICT);
        } else {
            UserEntity user = new UserEntity();
            user.setUsername(registerDto.getUsername());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

            // Standaard rol = USER
            Role role = roleRepository.findByRoleName(RoleName.USER);
            user.setRoles(Collections.singletonList(role));
            userRepository.save(user);
            String token = jwtUtilities.generateToken(registerDto.getUsername(), Collections.singletonList(role.getRoleName()));
            return new ResponseEntity<>(new BearerToken(token, "Bearer "), HttpStatus.OK);
        }
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    public List<Role> getUserRoles(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserEntity user = userRepository.findUserEntityByUsername(authentication.getPrincipal().toString());
            return user.getRoles();
        } else throw new RuntimeException("Error while trying to retrieve users roles.");
    }
}
