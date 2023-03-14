package nl.jesper.songshare.controllers;

import nl.jesper.songshare.SongFile;
import nl.jesper.songshare.SongShareConfig;
import nl.jesper.songshare.entities.SongEntity;
import nl.jesper.songshare.entities.UserEntity;
import nl.jesper.songshare.repositories.SongRepository;
import nl.jesper.songshare.requests.post.SongUploadRequest;
import nl.jesper.songshare.responses.BadResponse;
import nl.jesper.songshare.responses.SuccessResponse;
import nl.jesper.songshare.services.SongService;
import nl.jesper.songshare.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

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
}
