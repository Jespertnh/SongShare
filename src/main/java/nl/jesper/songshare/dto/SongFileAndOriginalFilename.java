package nl.jesper.songshare.dto;

import java.io.File;

/**
 * Object made so I can transfer the file content AND the original filename to SongController
 * when someone wants to download a song.
 */
public class SongFileAndOriginalFilename {

    private final String originalFilename;
    private final File songFile;

    public SongFileAndOriginalFilename(String originalFilename, File songFile) {
        this.originalFilename = originalFilename;
        this.songFile = songFile;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public File getSongFile() {
        return songFile;
    }
}
