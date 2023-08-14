package thrive.order.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ EntityNotFoundException.class })
    @ResponseBody
    public ResponseEntity<RestApiError> handleAuthenticationException(Exception ex) {
        var status = HttpStatus.NOT_FOUND;
        var error = new RestApiError(status.value(), ex.getMessage());
        return ResponseEntity.status(status).body(error);
    }
}
