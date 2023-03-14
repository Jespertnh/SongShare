package nl.jesper.songshare.entities;

import jakarta.persistence.*;

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

    // Dit is bedoeld om te checken of de gebruiker admin is of niet.
    // Eigenlijk wil ik dit alleen aanpassen via directe toegang tot de database.
    @Column(columnDefinition = "boolean default false", nullable = false) // Deze annotatie zorgt ervoor dat als er een user wordt aangemaakt, deze niet standaard admin is.
    private Boolean isAdmin = false; // In MySQL is een boolean een tinyint (0 of 1). Verwarrend maar logisch.


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

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setUsersSongs(List<SongEntity> usersSongs) {
        this.usersSongs = usersSongs;
    }
}
