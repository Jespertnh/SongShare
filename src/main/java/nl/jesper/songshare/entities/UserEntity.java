package nl.jesper.songshare.entities;

import jakarta.persistence.*;
import nl.jesper.songshare.security.RolesEnum;

import java.util.ArrayList;
import java.util.List;

// Entity hoort eigenlijk puur een data object te zijn.
// Repository voor database zoekopdrachten
// Service voor de logica
@Entity(name = "user_accounts")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Zorgt er voor dat niemand dezelfde username heeft.
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private List<SongEntity> usersSongs;

    @Column(nullable = false)
    @ElementCollection
    private List<RolesEnum> roles;


    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public List<SongEntity> getUsersSongs() {
        return usersSongs;
    }

    public void setUsersSongs(List<SongEntity> usersSongs) {
        this.usersSongs = usersSongs;
    }

    public List<RolesEnum> getRoles() {
        return roles;
    }

    public void setRoles(List<RolesEnum> roles) {
        this.roles = roles;
    }

    public void addRole(RolesEnum role) {
        if (this.roles != null) {
            roles.add(role);
        } else {
            this.roles = new ArrayList<RolesEnum>();
            roles.add(role);
        }
    }
}
