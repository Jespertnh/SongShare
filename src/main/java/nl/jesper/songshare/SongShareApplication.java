package nl.jesper.songshare;

import nl.jesper.songshare.repositories.RoleRepository;
import nl.jesper.songshare.repositories.UserRepository;
import nl.jesper.songshare.securitylayerJwt.models.Role;
import nl.jesper.songshare.securitylayerJwt.models.RoleName;
import nl.jesper.songshare.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class }) // Voor debuggen met postman anders zit security in de weg.
@SpringBootApplication
public class SongShareApplication {

    public static void main(String[] args) {
        SpringApplication.run(SongShareApplication.class, args);
    }

    @Bean
    CommandLineRunner run (UserService userService , RoleRepository roleRepository , UserRepository userRepository , PasswordEncoder passwordEncoder)
    {return  args ->
    {   userService.saveRole(new Role(RoleName.USER));
        userService.saveRole(new Role(RoleName.ADMIN));
//        userService.saveRole(new Role(RoleName.SUPERADMIN));
//        userService.saverUser(new User("admin@gmail.com", passwordEncoder.encode("adminPassword"), new ArrayList<>()));
//        userService.saverUser(new User("superadminadmin@gmail.com", passwordEncoder.encode("superadminPassword"), new ArrayList<>()));

//        Role role = roleRepository.findByRoleName(RoleName.ADMIN);
//        User user = userRepository.findByEmail("admin@gmail.com").orElse(null);
//        user.getRoles().add(role);
//        userService.saverUser(user);
//
//        User userr = userRepository.findByEmail("superadminadmin@gmail.com").orElse(null);
//        Role rolee = roleRepository.findByRoleName(RoleName.SUPERADMIN);
//        userr.getRoles().add(rolee);
//        userService.saverUser(userr);

    };}

}
