package nl.jesper.songshare.dto.requests.get;

/**
 * GET request in JSON format to download a song with a specific ID.
 */
public class DownloadSongRequest {
    private long songID;

    public DownloadSongRequest() {
    }

    public long getSongID() {
        return songID;
    }

    public void setSongID(long songID) {
        this.songID = songID;
    }
}
