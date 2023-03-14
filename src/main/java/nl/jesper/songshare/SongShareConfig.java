package nl.jesper.songshare;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "nl.jesper.songshare")
public class SongShareConfig {

    /**
     * The directory where you want to store and retrieve files from.
     */
    private String song_files_dir;

    public String getSong_files_dir() {
        return song_files_dir;
    }

    public void setSong_files_dir(String song_files_dir) {
        this.song_files_dir = song_files_dir;
    }
}