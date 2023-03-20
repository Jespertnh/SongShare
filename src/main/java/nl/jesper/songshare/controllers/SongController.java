package nl.jesper.songshare.controllers;

import nl.jesper.songshare.SongFile;
import nl.jesper.songshare.datacarry.SongFileAndOriginalFilename;
import nl.jesper.songshare.entities.SongEntity;
import nl.jesper.songshare.entities.UserEntity;
import nl.jesper.songshare.repositories.SongRepository;
import nl.jesper.songshare.requests.get.DownloadSongRequest;
import nl.jesper.songshare.requests.post.SongUploadRequest;
import nl.jesper.songshare.responses.BadResponse;
import nl.jesper.songshare.responses.SuccessResponse;
import nl.jesper.songshare.services.SongService;
import nl.jesper.songshare.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Controller for handling HTTP requests regarding song entities.
 */
@RestController
public class SongController {
    private final SongRepository songRepository;

    private final SongService songService;

    private final UserService userService;

    @Autowired
    public SongController(SongRepository songRepository, SongService songService, UserService userService) {
        this.songRepository = songRepository;
        this.songService = songService;
        this.userService = userService;
    }

    @PostMapping(path = "/songs/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> uploadSong(@RequestBody SongUploadRequest request) {
        String username = request.getUsername();
        String unhashedPassword = request.getPassword();
        String songTitle = request.getSongtitle();
        String songArtist = request.getSongartist();
//        MultipartFile multipartFile = request.getMultiPartSongfile();
        SongFile songFile = request.getSongFile();

        UserEntity user = userService.login(username, unhashedPassword);

        // Als de userService.login een UserEntity heeft teruggegeven, dan is de login gelukt. Zo niet dan is user null.
        // Zelfde geldt voor de songService.addSong.
        if (user != null) {
            SongEntity song = null;
            try {
                song = songService.addSong(songFile, songTitle, songArtist, user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (song != null) {
                return ResponseEntity.ok(new SuccessResponse("Song upload successful. Song with ID " + song.getId() + " added."));
            }
        } else {
            return ResponseEntity.badRequest().body(new BadResponse("Wrong login."));
        }
        return ResponseEntity.badRequest().body(new BadResponse("Something went wrong."));
    }

    @GetMapping(path = "/songs/")
    public ResponseEntity<InputStreamResource> downloadSong(@RequestBody DownloadSongRequest request) throws FileNotFoundException {
        long songID = request.getSongID();

        SongFileAndOriginalFilename songFile = songService.getSongFile(songID);

        if (songFile != null) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(songFile.getSongFile()));
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Disposition","attachment; filename=\"" + songFile.getOriginalFilename() + "\"");

            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .contentLength(songFile.getSongFile().length())
                    .contentType(MediaType.asMediaType(MimeType.valueOf("audio/mpeg3")))
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
