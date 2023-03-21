package nl.jesper.songshare.dto.requests.get;

/**
 * GET request in JSON format with search queries/parameters to get a song listing.
 */
public class SearchSongsRequest {
    private String songTitle;
    private String artistName;

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
