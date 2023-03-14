package nl.jesper.songshare.repositories;

import nl.jesper.songshare.entities.SongEntity;
import nl.jesper.songshare.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<SongEntity, Long> {
    // Returnt een List met alle SongEntity's als de songTitle exact goed is. Voor intern gebruik.
    List<SongEntity> findBySongTitle(String songTitle);

    // Returnt een List met alle SongEntity's ook als de input maar een deel van de titel is. Handig voor user input.
    List<SongEntity> findBySongTitleContainingIgnoreCase(String title);

    // Zelfde verhaal, intern gebruik want input moet exact zijn (ook hoofdlettergevoelig)
    List<SongEntity> findByUploader(UserEntity uploader);
}
