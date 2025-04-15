package fh.bswe.bookmanager.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Global exception handler for the application.
 * <p>
 * This class provides centralized exception handling for validation-related errors.
 * It intercepts exceptions thrown during controller method execution and returns
 * structured HTTP error responses.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors thrown when method arguments annotated with {@code @Valid}
     * fail to meet their constraints (e.g., {@code @NotBlank}, {@code @Pattern}, etc.).
     * <p>
     * Extracts all field-level validation errors and returns them in a {@link Map},
     * where each key is the name of the invalid field and the value is the
     * associated error message.
     * </p>
     *
     * @param ex the exception containing validation error details
     * @return a {@link ResponseEntity} with HTTP status {@code 400 Bad Request} and a
     *         map of field errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(final MethodArgumentNotValidException ex) {
        final Map<String, String> errors = new ConcurrentHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.unprocessableEntity().body(errors);
    }
}
