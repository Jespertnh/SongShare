package nl.jesper.songshare.dto.requests.post;

import nl.jesper.songshare.SongFile;

/**
 * POST request in JSON format for a song upload.
 */
public class SongUploadRequest {
    private SongFile songFile;
    private String songtitle;
    private String songartist;

    public SongUploadRequest() {}

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
