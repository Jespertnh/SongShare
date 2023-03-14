package nl.jesper.songshare.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity(name = "songs")
public class SongEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "song_title", nullable = false)
    private String songTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id", nullable = false)
    private UserEntity uploader;

//    @Lob
//    @Column(name = "song_blob", columnDefinition = "LONGBLOB", nullable = false) // Longblob voor maximaal 4gb
//    private String songBlob; // Hier komt het nummer zelf.

    @Column(name = "file_hash", nullable = false)
    private String fileHash;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "song_artist", nullable = false)
    private String songArtist;

    @Column(name = "upload_time_stamp", nullable = false, updatable = false)
    private Timestamp uploadTimeStamp;

    public long getId() {
        return id;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String title) {
        this.songTitle = title;
    }

    public UserEntity getUploader() {
        return uploader;
    }

    public void setUploader(UserEntity uploader) {
        this.uploader = uploader;
    }

//    public String getSongBlob() {
//        return songBlob;
//    }
//
//    public void setSongBlob(String songBlob) {
//        this.songBlob = songBlob;
//    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public Timestamp getUploadTimeStamp() {
        return uploadTimeStamp;
    }

    public void setUploadTimeStamp(Timestamp uploadTimeStamp) {
        this.uploadTimeStamp = uploadTimeStamp;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }
}
