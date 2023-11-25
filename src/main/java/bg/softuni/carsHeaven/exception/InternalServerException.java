package bg.softuni.carsHeaven.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException {

    private final String id;

    public InternalServerException(String message, String id) {
        super(message);
        this.id = id;
    }


    public String getId() {
        return id;
    }
}
