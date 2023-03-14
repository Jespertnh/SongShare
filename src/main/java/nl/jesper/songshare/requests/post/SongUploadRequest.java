package nl.jesper.songshare.requests.post;

import nl.jesper.songshare.SongFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * POST request in JSON format for a song upload.
 */
public class SongUploadRequest {
    private String username;
    private String password;
    private SongFile songFile;
    private String songtitle;
    private String songartist;

    public SongUploadRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SongFile getSongFile() {
        return songFile;
    }

    public void setSongFile(SongFile songFile) {
        this.songFile = songFile;
    }

    public String getSongtitle() {
        return songtitle;
    }

    public void setSongtitle(String songtitle) {
        this.songtitle = songtitle;
    }

    public String getSongartist() {
        return songartist;
    }

    public void setSongartist(String songartist) {
        this.songartist = songartist;
    }
}
