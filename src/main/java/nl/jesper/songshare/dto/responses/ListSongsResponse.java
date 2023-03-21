package nl.jesper.songshare.dto.responses;

import nl.jesper.songshare.dto.SongListing;

import java.util.List;

public class ListSongsResponse {
    private List<SongListing> songs;

    public ListSongsResponse(List<SongListing> songs) {
        this.songs = songs;
    }

    public List<SongListing> getSongs() {
        return songs;
    }

    public void setSongs(List<SongListing> songs) {
        this.songs = songs;
    }

    public void addSong(SongListing songListing) {
        songs.add(songListing);
    }
}
