package nl.jesper.songshare.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import nl.jesper.songshare.dto.SongFileAndOriginalFilename;
import nl.jesper.songshare.dto.requests.get.DownloadSongRequest;
import nl.jesper.songshare.dto.requests.get.SearchSongsRequest;
import nl.jesper.songshare.dto.requests.post.SongUploadRequest;
import nl.jesper.songshare.dto.responses.ApiResponse;
import nl.jesper.songshare.dto.responses.ListSongsResponse;
import nl.jesper.songshare.entities.SongEntity;
import nl.jesper.songshare.entities.UserEntity;
import nl.jesper.songshare.repositories.SongRepository;
import nl.jesper.songshare.services.SongService;
import nl.jesper.songshare.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;

/**
 * Controller voor song endpoints
 */
@RestController
@RequestMapping("/songs")
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

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadSong(@RequestBody SongUploadRequest request) {
        Optional<UserEntity> userOpt = userService.login(request.getUsername(), request.getPassword());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "Wrong login."));
        }
        try {
            SongEntity song = songService.addSong(request.getSongFile(), request.getSongtitle(), request.getSongartist(), userOpt.get());
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Song upload successful. Song with ID " + song.getId() + " added."));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ListSongsResponse> getAllSongs(@RequestBody SearchSongsRequest searchSongsRequest) {
        return ResponseEntity.ok(songService.getSongListingResponse(searchSongsRequest));
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadSong(@RequestBody DownloadSongRequest request) throws FileNotFoundException {
        long songID = request.getSongID();

        SongFileAndOriginalFilename songFile = songService.getSongFile(songID);

        if (songFile != null) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(songFile.getSongFile()));
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + songFile.getOriginalFilename() + "\"");

            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .contentLength(songFile.getSongFile().length())
                    .contentType(MediaType.asMediaType(MimeType.valueOf("audio/mpeg3")))
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND.value(), "Song not found"));
        }
    }
}
