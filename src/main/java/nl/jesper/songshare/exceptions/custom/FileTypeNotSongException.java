package nl.jesper.songshare.exceptions.custom;

public class FileTypeNotSongException extends RuntimeException {
    public FileTypeNotSongException() {
        super();
    }

    public FileTypeNotSongException(String message) {
        super(message);
    }

    public FileTypeNotSongException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileTypeNotSongException(Throwable cause) {
        super(cause);
    }

    protected FileTypeNotSongException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
