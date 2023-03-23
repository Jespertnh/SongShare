package nl.jesper.songshare.exceptions.custom;

public class SongsNotFoundException extends RuntimeException {
    public SongsNotFoundException() {
        super();
    }

    public SongsNotFoundException(String message) {
        super(message);
    }

    public SongsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SongsNotFoundException(Throwable cause) {
        super(cause);
    }

    protected SongsNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
