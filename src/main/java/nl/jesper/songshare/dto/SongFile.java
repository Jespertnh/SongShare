package nl.jesper.songshare.dto;

public class SongFile {

    /**
     * The base64 contents of a song file.
     */
    private String songBase64;

    /**
     * The filename of the file uploaded
     */
    private String fileName;

    public SongFile(String songBase64, String fileName) {
        this.songBase64 = songBase64;
        this.fileName = fileName;
    }


    public String getSongBase64() {
        return songBase64;
    }

    public void setSongBase64(String songBase64) {
        this.songBase64 = songBase64;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
