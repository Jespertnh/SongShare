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
     * Decodes base64, and writes the decoded bytes to a file with the SHA hash of the bytes as filename to the directory configured in application.properties
     * @param encodedString Base64 encoded file contents.
     * @return The saved File.
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


    public SongEntity addSong(SongFile songFile, String songTitle, String songArtist, UserEntity uploader) throws SongSizeException, IOException, FileTypeNotSongException {
        String fileName = songFile.getFileName();
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

    public ListSongsResponse getSongListingResponse(SearchSongsRequest searchSongsRequest) {
        ListSongsResponse listSongsResponse = new ListSongsResponse();
        List<SongEntity> songEntities = null;
        String songTitleQuery = searchSongsRequest.getSongTitle();
        String songArtistQuery = searchSongsRequest.getArtistName();

        if (!songTitleQuery.isBlank() && songArtistQuery.isBlank()) {
            songEntities = songRepository.findSongEntitiesBySongTitleContainsIgnoreCase(songTitleQuery);
        } else if (!songArtistQuery.isBlank() && songTitleQuery.isBlank()) {
            songEntities = songRepository.findSongEntitiesBySongArtistContainsIgnoreCase(songArtistQuery);
        } else if (!songTitleQuery.isBlank() && !songArtistQuery.isBlank()){
            songEntities = songRepository.findSongEntitiesBySongTitleContainsIgnoreCaseAndSongArtistContainsIgnoreCase(songTitleQuery, songArtistQuery);
        } else if (songArtistQuery.isBlank() && songTitleQuery.isBlank()) {
            throw new EmptySearchException();
        }

        assert songEntities != null;
        if (!songEntities.isEmpty()) {
            for (SongEntity songEntity : songEntities) {
                SongListing songListing = new SongListing();

//                String downloadURL = WebMvcLinkBuilder.linkTo(SongController.class, downloadSong(songEntity.getId())).toString();

                songListing.setSongTitle(songEntity.getSongTitle());
                songListing.setArtistName(songEntity.getSongArtist());
                songListing.setSongID(songEntity.getId());
                songListing.setFileName(songEntity.getOriginalFilename());
                songListing.setUploadDate(songEntity.getUploadTimeStamp().toString());
//                songListing.setDownloadURL(downloadURL);
                listSongsResponse.addSong(songListing);
            }
        } else throw new SongsNotFoundException();
        return listSongsResponse;
    }


}
