package nl.jesper.songshare.services;

import nl.jesper.songshare.SongShareConfig;
import nl.jesper.songshare.SongFile;
import nl.jesper.songshare.dto.SongFileAndOriginalFilename;
import nl.jesper.songshare.dto.SongListing;
import nl.jesper.songshare.dto.requests.get.SearchSongsRequest;
import nl.jesper.songshare.dto.responses.ListSongsResponse;
import nl.jesper.songshare.entities.SongEntity;
import nl.jesper.songshare.entities.UserEntity;
import nl.jesper.songshare.exceptions.custom.EmptySearchException;
import nl.jesper.songshare.exceptions.custom.FileTypeNotSongException;
import nl.jesper.songshare.exceptions.custom.SongSizeException;
import nl.jesper.songshare.exceptions.custom.SongsNotFoundException;
import nl.jesper.songshare.repositories.SongRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.tika.Tika;


/**
 * Service that handles the logic of Song stuff.
 */
@Service
public class SongService {
    private final SongRepository songRepository;
    private final String uploadDirString;

    @Autowired
    public SongService(SongShareConfig songShareConfig, SongRepository songRepository) {
        this.songRepository = songRepository;
        this.uploadDirString = songShareConfig.getSong_files_dir();
    }

    /**
     * Creates a file on the file system from the given base64-encoded string.
     * Checks if the byte array is a valid MPEG MP3 file before writing it to the file system.
     * @param encodedString the base64-encoded string to decode and write to the file system.
     * @return a File object representing the file that was written to the file system.
     * @throws FileTypeNotSongException if the byte array is not a valid MPEG MP3 file.
     */
    private File createFileFromBase64String(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String hash = DigestUtils.sha256Hex(decodedBytes);

        // Slaat het bestand op met als bestandsnaam de hash, in de directory aangegeven in application.properties.
        Path filePath = Paths.get(uploadDirString, hash);

        // Checkt of de byte[] wel echt een mp3-bestand is voordat het bestand naar het bestandssysteem wordt geschreven.
        if (isMP3(decodedBytes)) {
            try {
                Files.write(filePath, decodedBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return filePath.toFile();
        } else throw new FileTypeNotSongException("File is not a mpeg mp3 file.");
    }

    /**
     * Checks if a byte[] is indeed a mpeg mp3 file.
     * @param decodedBytes The byte[] you want to check.
     * @return True if byte[] is a mpeg mp3. False if it is not.
     */
    private boolean isMP3(byte[] decodedBytes) {
        Tika tika = new Tika();
        String fileType = tika.detect(decodedBytes);
        return fileType.equals("audio/mpeg");
    }

    /**
     * Adds a new song to the system with the given song file, title, artist, and uploader.
     * @param songFile the song file to upload.
     * @param songTitle the title of the song.
     * @param songArtist the artist of the song.
     * @param uploader the user who uploaded the song.
     * @return the newly created SongEntity object.
     * @throws SongSizeException if the song file size exceeds the maximum allowed size.
     * @throws IOException if there is an error while reading or writing the song file.
     * @throws FileTypeNotSongException if the uploaded file is not an MP3 file.
     */
    public SongEntity addSong(SongFile songFile, String songTitle, String songArtist, UserEntity uploader) throws SongSizeException, IOException, FileTypeNotSongException {
        String fileName = songFile.getFileName();

        // Makes sure the filename has the right extension
        if (!fileName.toLowerCase().endsWith(".mp3")) {
            fileName = fileName.concat(".mp3");
        }

        String songBase64 = songFile.getSongBase64();
        File decodedSongFile = createFileFromBase64String(songBase64);
        String hash = decodedSongFile.getName(); // De bestandsnaam is de SHA hash.

        SongEntity song = new SongEntity();
        song.setSongArtist(songArtist);
        song.setSongTitle(songTitle);
//        song.setSongBlob(FileUtils.readFileToString(decodedSongFile, StandardCharsets.UTF_8));
        song.setFileHash(hash);
        song.setOriginalFilename(fileName);
        song.setUploader(uploader);
        song.setUploadTimeStamp(new Timestamp(System.currentTimeMillis()));
        return songRepository.save(song);
    }

    /**
     * Used in SongController for a GET download request.
     * @param songId The unique ID of the song you want to get returned.
     * @return A SongFile object which includes the File from the filesystem and the original filename to be used in the HTTP header.
     * Null if song is not found.
     */
    public SongFileAndOriginalFilename getSongFile(Long songId) {
        Optional<SongEntity> optionalSongEntity = songRepository.findById(songId);

        if (optionalSongEntity.isPresent()) {
            SongEntity song = optionalSongEntity.get();
            File songFile = Paths.get(uploadDirString + "/" + song.getFileHash()).toFile();
            String originalFileName = song.getOriginalFilename();

            return new SongFileAndOriginalFilename(originalFileName,songFile);
        } else {
            return null;
        }
    }

    /**
     * Returns a list of songs uploaded by the user with the given username.
     * @param username the username of the user whose uploads to retrieve
     * @return a list of SongListing objects representing the user's uploads
     * @throws SongsNotFoundException if the user has not uploaded any songs
     */
    public ListSongsResponse getOwnUploads(String username) {
        List<SongEntity> songEntities = songRepository.findAllByUploaderUsername(username);

        if (!songEntities.isEmpty()) {
            return makeSongListResponse(songEntities);
        } else throw new SongsNotFoundException();
    }

    /**
     * Generates a List of SongListing DTO's for the user to see.
     * @param songEntities A list of all the songs you want to be in the list
     * @return A ListSongsResponse which you can return to the user in a controller.
     */
    private ListSongsResponse makeSongListResponse(List<SongEntity> songEntities) {
        ListSongsResponse listSongsResponse = new ListSongsResponse();
        for (SongEntity songEntity : songEntities) {
            SongListing songListing = new SongListing(songEntity);
            listSongsResponse.addSong(songListing);
        }
        return listSongsResponse;
    }

    /**
     * First deletes the SongEntity from the database, then checks if there are other SongEntities
     * that link to the same file on the filesystem, if not, the file is also removed from the filesystem.
     * @param songID the ID of the SongEntity you want removed.
     * @throws IOException if there is an error while deleting the song from the file system.
     */
    public void deleteSong(Long songID) throws IOException {
        Optional<SongEntity> optionalSongEntity = songRepository.findById(songID);

        if (optionalSongEntity.isPresent()) {
            // Delete song entry from database
            songRepository.deleteById(songID);

            // Check if there are other song entries that use the same file on the filesystem
            SongEntity songEntity = optionalSongEntity.get();
            String songHash = songEntity.getFileHash();
            List<SongEntity> songsThatHaveSameHash = songRepository.findSongEntitiesByFileHash(songHash);

            if (songsThatHaveSameHash.isEmpty()) {
                String filePath = uploadDirString + "/" + songHash;
                File songFile = new File(filePath);

                if (songFile.exists() && songFile.isFile()) {
                    boolean deleted = songFile.delete();

                    if (!deleted) {
                        throw new IOException("Error removing file: " + filePath);
                    }
                } else throw new IOException("File doesn't exist or is a directory: " + filePath);
            }
        }
    }

    /**
     * Checks if the UserEntity is indeed the uploader of a song.
     * @param user The UserEntity.
     * @param songID The ID of the song.
     * @return True if the UserEntity is the owner of the song.
     */
    public boolean HasUploadedSong(UserEntity user, long songID) {
        Optional<SongEntity> optionalSongEntity = songRepository.findSongEntityByIdAndUploader(songID,user);
        return optionalSongEntity.isPresent();
    }


}
