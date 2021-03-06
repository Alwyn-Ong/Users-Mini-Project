package mini.project.exception;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonParseException;

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

		Map<String, String> errorDetails = new HashMap<>();
		errorDetails.put("error", e.getMessage());
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
		Map<String, String> errorDetails = new HashMap<>();
		errorDetails.put("error", e.getMessage());
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

		Map<String, String> errorDetails = new HashMap<>();
		String errorMessage = e.getMessage();
		errorDetails.put("error", errorMessage);
		
		e.printStackTrace();		
		if (ExceptionUtils.indexOfType(e, JsonParseException.class) != -1) {
			// For invalid JSON inputs
			errorDetails.put("error", "Invalid JSON input!");
			return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
		} else if (ExceptionUtils.indexOfType(e, BindException.class) != -1) {
			// for invalid parameter types
			errorDetails.put("error", "Invalid parameter input!");
			return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
