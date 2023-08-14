package thrive.order.exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RestApiError {
    private int statusCode;
    private String message;

    public RestApiError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
