package nl.jesper.songshare.responses;

public class BadResponse {
    private String message;

    public BadResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
