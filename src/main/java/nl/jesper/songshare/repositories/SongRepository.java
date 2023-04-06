package nl.jesper.songshare.repositories;

import nl.jesper.songshare.entities.SongEntity;
import nl.jesper.songshare.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<SongEntity, Long> {
    /**
     * @param songTitle The exact song title of the SongEntity(s) you want returned.
     * @return A List of all SongEntity's with the exact song title you added.
     */
    List<SongEntity> findSongEntitiesBySongTitle(String songTitle);

    /**
     * @param songTitle (Part of) a song title (case ignorant).
     * @return A list of all SongEntity's that have your query in their song title.
     */
    List<SongEntity> findSongEntitiesBySongTitleContainsIgnoreCase(String songTitle);

    /**
     * @param artistName (Part of) the artist name (case ignorant).
     * @return A list of all SongEntity's that have your query in their artist's name.
     */
    List<SongEntity> findSongEntitiesBySongArtistContainsIgnoreCase(String artistName);

    /**
     * @param uploader The UserEntity of which you want to retrieve all songs from.
     * @return A list of all SongEntity's uploaded by the specified UserEntity.
     */
    List<SongEntity> findSongEntitiesByUploader(UserEntity uploader);

    /**
     * @param uploaderUsername The username of the uploader
     * @return A list of all songs uploaded by that user.
     */
    List<SongEntity> findAllByUploaderUsername(String uploaderUsername);

    /**
     * @param songTitle (Part of) a song title (case ignorant).
     * @param artistName (Part of) the artist name (case ignorant).
     * @return A list of all SongEntity's that contain (parts of) both the song title and artist name.
     */
    List<SongEntity> findSongEntitiesBySongTitleContainsIgnoreCaseAndSongArtistContainsIgnoreCase(String songTitle, String artistName);

    /**
     * @param fileHash the hash of the file.
     * @return A list of all songs in the database that link to the same file on the filesystem.
     */
    List<SongEntity> findSongEntitiesByFileHash(String fileHash);

    Optional<SongEntity> findSongEntityByIdAndUploader(Long songID, UserEntity uploader);
}
