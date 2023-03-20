package nl.jesper.songshare.services;

import nl.jesper.songshare.SongShareConfig;
import nl.jesper.songshare.SongFile;
import nl.jesper.songshare.DTO.SongFileAndOriginalFilename;
import nl.jesper.songshare.entities.SongEntity;
import nl.jesper.songshare.entities.UserEntity;
import nl.jesper.songshare.exceptions.FileTypeNotSongException;
import nl.jesper.songshare.exceptions.SongSizeException;
import nl.jesper.songshare.repositories.SongRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Optional;

import org.apache.tika.Tika;

/**
 * Service that handles the logic of Song stuff.
 */
@Service
public class SongService {
    private final SongRepository songRepository;


    private SongShareConfig configuration;

    private final String uploadDirString;

    private Path uploadDirPath;

    @Autowired
    public SongService(SongShareConfig songShareConfig, SongRepository songRepository) {
        this.songRepository = songRepository;
        this.configuration = songShareConfig;

        this.uploadDirString = songShareConfig.getSong_files_dir();
        this.uploadDirPath = Path.of(this.uploadDirString);
    }

//    /**
//     * Turns a String into a temporary file and stores the file in the systems temporary file directory.
//     * @param encodedString Base64 decoded String.
//     * @param fileName The file name of the file.
//     * @return The created temporary file.
//     * @throws IOException When the file couldn't be made on the filesystem.
//     */
//    private File createTempFileFromBase64String(String encodedString, String fileName) throws IOException {
//        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
//        Path tempFilePath = Files.createTempFile("temp-", "-" + fileName);
//        File tempFile = tempFilePath.toFile();
//        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
//            fos.write(decodedBytes);
//        }
//        return tempFile;
//    }

    /**
     * Decodes base64, and writes the decoded bytes to a file with the SHA hash of the bytes as filename to the directory configured in application.properties
     * @param encodedString Base64 encoded file contents.
     * @param fileName The filename that belongs to the content
     * @return The saved File.
     * @throws IOException When the file couldn't be written to disk.
     */
    private File createFileFromBase64String(String encodedString, String fileName) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String hash = DigestUtils.sha256Hex(decodedBytes);

        // Slaat het bestand op met als bestandsnaam de hash, in de directory aangegeven in application.properties.
        Path filePath = Paths.get(uploadDirString, hash);

        // Checkt of de byte[] wel echt een mp3-bestand is voordat het bestand naar het bestandssysteem wordt geschreven.
        if (isMP3(decodedBytes)) {
            try {
                Files.write(filePath, decodedBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return filePath.toFile();
        } else throw new FileTypeNotSongException("File is not a mpeg mp3 file.");
    }

    /**
     * Checks if a file is indeed a mpeg mp3 file.
     * @param file The File you want to check.
     * @return True if file is a mpeg mp3. False if it is not.
     * @throws IOException When the File couldn't be read.
     */
    private boolean isMP3(File file) throws IOException {
        Tika tika = new Tika();
        String fileType = tika.detect(file);
        return fileType.equals("audio/mpeg");
    }

    /**
     * Checks if a file is indeed a mpeg mp3 file.
     * @param decodedBytes The byte[] you want to check.
     * @return True if byte[] is a mpeg mp3. False if it is not.
     */
    private boolean isMP3(byte[] decodedBytes) {
        Tika tika = new Tika();
        String fileType = tika.detect(decodedBytes);
        return fileType.equals("audio/mpeg");
    }

//    /**
//     * Checks if a File isn't too large to fit into a LONGBLOB field in a MySQL database.
//     * @param songFile The File you want to check.
//     * @return True if the file fits into a LONGBLOB field. False if it doesn't.
//     */
//    private boolean fileFitsInLongBlobField(File songFile) {
//        long fileSize = songFile.length();
//        long maxLongBlobSize = 4294967295L; // Maximum size of a LONGBLOB field in bytes
//        return fileSize < maxLongBlobSize;
//    }


    public SongEntity addSong(SongFile songFile, String songTitle, String songArtist, UserEntity uploader) throws SongSizeException, IOException, FileTypeNotSongException {
        String fileName = songFile.getFileName();
        String songBase64 = songFile.getSongBase64();
        File decodedSongFile = createFileFromBase64String(songBase64, fileName);
        String hash = decodedSongFile.getName(); // De bestandsnaam is de SHA hash.

//        if (!fileFitsInLongBlobField(decodedSongFile)) {
//            throw new SongSizeException("File is too big to fit into a LONGBLOB field.");
//        } else if (!isMP3(decodedSongFile)) {
//            throw new FileTypeNotSongException("File is not a mpeg .mp3 file.");
//        }

//        // Hoeft eigenlijk niet meer omdat ik al check voordat hij naar disk schrijft. Dus als ie hier aankomt zonder errors is t al goed.
//        if (!isMP3(decodedSongFile)) {
//            throw new FileTypeNotSongException("File is not a mpeg .mp3 file.");
//        }

        SongEntity song = new SongEntity();
        song.setSongArtist(songArtist);
        song.setSongTitle(songTitle);
//        song.setSongBlob(FileUtils.readFileToString(decodedSongFile, StandardCharsets.UTF_8));
        song.setFileHash(hash);
        song.setOriginalFilename(fileName);
        song.setUploader(uploader);
        song.setUploadTimeStamp(new Timestamp(System.currentTimeMillis()));
        return songRepository.save(song);
    }

    /**
     * Downloads a song using the song's id.
     * @param songId The unique ID of the song.
     * @return The SongEntity if found, null if not found.
     */
    public SongEntity downloadSong(Long songId) {
        Optional<SongEntity> optionalSongEntity = songRepository.findById(songId);
        return optionalSongEntity.orElse(null);
    }

    /**
     * Used in SongController for a GET download request.
     * @param songId The unique ID of the song you want to get returned.
     * @return A SongFile object which includes the File from the filesystem and the original filename to be used in the HTTP header.
     * Null if song is not found.
     */
    public SongFileAndOriginalFilename getSongFile(Long songId) {
        Optional<SongEntity> optionalSongEntity = songRepository.findById(songId);

        if (optionalSongEntity.isPresent()) {
            SongEntity song = optionalSongEntity.get();
            File songFile = Paths.get(uploadDirString + "/" + song.getFileHash()).toFile();
            String originalFileName = song.getOriginalFilename();

            return new SongFileAndOriginalFilename(originalFileName,songFile);
        } else {
            return null;
        }
    }

//    /**
//     * All the logic of adding a song to the database.
//     * @param multiPartSongFile The MultiPartFile of the song you want to add.
//     * @param songTitle The custom title of the song you want to add.
//     * @param songArtist The custom artist of the song you want to add.
//     * @param uploader The UserEntity of the user uploading the song.
//     * @return The SongEntity after it's successfully added to the database.
//     * @throws SongSizeException When the size of the songFile is larger than around 4GB (the max size for largeblob), or when the size of the songFile is 0 bytes.
//     */
//    public SongEntity addSong(MultipartFile multiPartSongFile, String songTitle, String songArtist, UserEntity uploader) throws SongSizeException {
//        final Path tempDirPath = Paths.get("tempFiles");
//        final String tempFilePrefix = "tempSongFile";
//        final String tempFileSuffix = ".tmp";
//
//        String originalFileName = multiPartSongFile.getOriginalFilename();
//
//        File songFile = null;
//        File tempSongFile = null;
//        Path filePath = null;
//
//        // Probeert tijdelijk bestand aan te maken.
//        try {
//            filePath = Files.createTempFile(tempDirPath,tempFilePrefix,tempFileSuffix);
//            tempSongFile = new File(filePath.toString());
//            //System.out.println("Temporary file created: " + filePath.toString());
//        } catch (IOException e) {
//            throw new RuntimeException("Could not create temporary file at: " + filePath.toString());
//        }
//
//        // Zet MultiPartFile om in File met behulp van MultiPartFile.transferto
//        try {
//            assert tempSongFile != null;
//            multiPartSongFile.transferTo(tempSongFile);
//        } catch (IOException e) {
//            throw new RuntimeException("Could not transfer multiPartSongFile to tempSongFile.");
//        }
//
//        // Zet File songFile gelijk aan File tempSongFile
//        songFile = tempSongFile;
//
//        // Probeert het tijdelijke bestand weer te verwijderen
//        try {
//            Files.delete(Path.of(tempSongFile.getAbsolutePath()));
//        } catch (IOException e) {
//            throw new RuntimeException("Could not remove temporary file: " + tempSongFile.getAbsolutePath());
//        }
//
//        byte[] songBlob;
//        Path pathToFile = Path.of(songFile.getAbsolutePath());
//        BigInteger fileSizeInBytes = new BigInteger("0");
//        BigInteger maxFileSizeInBytes = new BigInteger("4294967290"); // Iets minder dan het max voor mySQL largeblob. (4gb)
//
//        // Probeert bestand te lezen & vervolgens de grootte op te slaan in een BigInteger om te vergelijken met de max.
//        try {
//            fileSizeInBytes = new BigInteger(String.valueOf(Files.size(pathToFile)));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        // Gebruikt do compareTo functie van BigInteger om te berekenen of het bestand niet te groot is voor largeblob.
//        // Checkt ook of het bestand niet 0 bytes is.
//        if (fileSizeInBytes.compareTo(maxFileSizeInBytes) > 0) {
//            throw new SongSizeException("Song size too large.");
//        } else if (fileSizeInBytes.compareTo(new BigInteger("0")) == 0) {
//            throw new SongSizeException("Song size is 0 bytes.");
//        }
//
//        // Zet File om in byte array om op te slaan in database.
//        try {
//            songBlob = Files.readAllBytes(pathToFile);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        SongEntity song = new SongEntity();
//        song.setOriginalFilename(originalFileName);
//        song.setSongBlob(songBlob);
//        song.setSongTitle(songTitle);
//        song.setSongArtist(songArtist);
//        song.setUploader(uploader);
//        song.setUploadTimeStamp(new Timestamp(System.currentTimeMillis()));
//        return songRepository.save(song);
//    }


}
