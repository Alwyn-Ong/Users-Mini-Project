package mini.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// For server errors
@ResponseStatus(value = HttpStatus.BAD_GATEWAY)
public class APIException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public APIException(String message) {
		super(message);
	}
}