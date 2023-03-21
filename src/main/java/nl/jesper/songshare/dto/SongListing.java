package nl.jesper.songshare.dto;

/**
 * Een DTO-object wat alleen de informatie die een gebruiker moet zien van 1 nummer bevat.
 */
public class SongListing {
    private String artistName;
    private String songTitle;

    /**
     * Tip: zet Timestamp om in String
     */
    private String uploadDate;
    private String fileName;
//    private String downloadURL;
    private Long songID;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

//    public String getDownloadURL() {
//        return downloadURL;
//    }
//
//    public void setDownloadURL(String downloadURL) {
//        this.downloadURL = downloadURL;
//    }

    public Long getSongID() {
        return songID;
    }

    public void setSongID(Long songID) {
        this.songID = songID;
    }
}
