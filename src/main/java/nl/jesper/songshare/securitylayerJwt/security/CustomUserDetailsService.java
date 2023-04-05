package nl.jesper.songshare.securitylayerJwt.security;

import lombok.RequiredArgsConstructor;
import nl.jesper.songshare.entities.UserEntity;
import nl.jesper.songshare.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserEntityByUsername(username);

        if (user != null) {
            return user;
        } else throw new UsernameNotFoundException("Username " + username + " not found.");

    }


}
