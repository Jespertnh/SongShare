package nl.jesper.songshare.exceptions;

import nl.jesper.songshare.exceptions.custom.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing the request. Error: " + ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileTypeNotSongException.class)
    public ResponseEntity<ErrorResponse> handleFileTypeNotSongException(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, "The file you uploaded is not a mp3 file.");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SongSizeException.class)
    public ResponseEntity<ErrorResponse> handleSongSizeException(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, "The file you uploaded is too large.");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SongsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSongsNotFoundException(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "The song(s) you were looking for were not found.");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptySearchException.class)
    public ResponseEntity<ErrorResponse> handleEmptySearchException(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, "Search queries are blank. Please enter something to search for.");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExistsException(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT, "That username has already been taken.");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    public static class ErrorResponse {
        private HttpStatus status;
        private String message;

        public ErrorResponse(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public void setStatus(HttpStatus status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
