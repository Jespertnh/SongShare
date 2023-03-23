package nl.jesper.songshare.exceptions.custom;

/**
 * Custom exception that is to be used when theres something wrong with the filesize of a uploaded song.
 */
public class SongSizeException extends RuntimeException {
    public SongSizeException() {
        super();
    }

    public SongSizeException(String message) {
        super(message);
    }

    public SongSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SongSizeException(Throwable cause) {
        super(cause);
    }

    public SongSizeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
