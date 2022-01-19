package mini.project.exception;

import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonParseException;

import mini.project.model.ResponseDetails;

/**
 * 
 * GlobalExceptionHandler
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handle APIException
	 * 
	 * @param e       - APIException
	 * @param request - Web Request
	 * @return ResponseEntity with http status code
	 */
	@ExceptionHandler(APIException.class)
	public ResponseEntity<?> handleAPIException(APIException e, WebRequest request) {

		ResponseDetails errorDetails = new ResponseDetails(new Date(), e.getMessage(), request.getDescription(false));
		return new ResponseEntity(errorDetails, HttpStatus.BAD_GATEWAY);
	}

	/**
	 * Handle ParameterException
	 * 
	 * @param e       - ParameterException
	 * @param request - Web Request
	 * @return ResponseEntity with http status code
	 */
	@ExceptionHandler(ParameterException.class)
	public ResponseEntity<?> handleAPIException(ParameterException e, WebRequest request) {

		ResponseDetails errorDetails = new ResponseDetails(new Date(), e.getMessage(), request.getDescription(false));
		return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle Exception - More general Exceptions
	 * 
	 * @param e       - Exception
	 * @param request - Web Request
	 * @return ResponseEntity with http status code
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGlobalException(Exception e, WebRequest request) {

		ResponseDetails errorDetails = new ResponseDetails(new Date(), e.getMessage(), request.getDescription(false));

		// For invalid JSON inputs
		if (ExceptionUtils.indexOfType(e, JsonParseException.class) != -1) {
			errorDetails.setMessage("Invalid JSON input!");
			return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
