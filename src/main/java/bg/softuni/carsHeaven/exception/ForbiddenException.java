package bg.softuni.carsHeaven.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    private final String id;

    public ForbiddenException(String message, String id) {
        super(message);
        this.id = id;
    }


    public String getId() {
        return id;
    }
}
