package nl.jesper.songshare.repositories;

import nl.jesper.songshare.entities.SongEntity;
import nl.jesper.songshare.entities.UserEntity;
import nl.jesper.songshare.security.RolesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserEntityByUsername(String username); // Vind een user met deze exacte username. Voor intern gebruik.
    List<UserEntity> searchUserEntitiesByUsernameIgnoreCase(String username); // Zoekt naar een user.
    UserEntity findUserEntityByUsersSongsContaining(SongEntity song); // Vind de user bij een Song
}
