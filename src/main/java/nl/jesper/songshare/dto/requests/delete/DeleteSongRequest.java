package nl.jesper.songshare.dto.requests.delete;

public class DeleteSongRequest {
    private long songID;

    public long getSongID() {
        return songID;
    }

    public void setSongID(long songID) {
        this.songID = songID;
    }
}
