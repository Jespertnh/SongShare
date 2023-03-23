package nl.jesper.songshare.exceptions.custom;

public class EmptySearchException extends RuntimeException {
    public EmptySearchException() {
        super();
    }

    public EmptySearchException(String message) {
        super(message);
    }

    public EmptySearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptySearchException(Throwable cause) {
        super(cause);
    }

    protected EmptySearchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
