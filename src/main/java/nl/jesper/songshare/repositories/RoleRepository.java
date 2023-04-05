package nl.jesper.songshare.repositories;

import nl.jesper.songshare.securitylayerJwt.models.Role;
import nl.jesper.songshare.securitylayerJwt.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(RoleName roleName);

}
