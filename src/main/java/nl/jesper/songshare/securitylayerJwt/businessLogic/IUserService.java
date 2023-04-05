package nl.jesper.songshare.securitylayerJwt.businessLogic;

import nl.jesper.songshare.entities.UserEntity;
import nl.jesper.songshare.securitylayerJwt.dto.LoginDto;
import nl.jesper.songshare.securitylayerJwt.dto.RegisterDto;
import nl.jesper.songshare.securitylayerJwt.models.Role;
import org.springframework.http.ResponseEntity;


public interface IUserService {
    //ResponseEntity<?> register (RegisterDto registerDto);
    //  ResponseEntity<BearerToken> authenticate(LoginDto loginDto);

    String authenticate(LoginDto loginDto);

    ResponseEntity<?> register(RegisterDto registerDto);

    Role saveRole(Role role);

    UserEntity saveUser(UserEntity user);
}
