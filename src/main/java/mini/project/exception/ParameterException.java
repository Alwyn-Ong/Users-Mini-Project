package mini.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// For invalid requests
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ParameterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ParameterException(String message) {
		super(message);
	}
}