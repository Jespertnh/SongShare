package nl.jesper.songshare.repositories;

import nl.jesper.songshare.entities.UserEntity;
import nl.jesper.songshare.security.RolesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RolesEnum, Long> {
    List<RolesEnum> findRolesEnumsByUser(UserEntity user);
}
