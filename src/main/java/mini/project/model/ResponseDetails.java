package mini.project.model;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * ResponseDetails class that provides the format of response returned to client
 *
 */
@ApiModel(description = "Response details class that provides response to client")
public class ResponseDetails {

	@ApiModelProperty(notes = "Timestamp of response")
	private Date timestamp;

	@ApiModelProperty(notes = "Response message")
	private String message;

	@ApiModelProperty(notes = "Response details")
	private String details;

	/**
	 * Constructor of errordetails
	 * 
	 * @param timestamp - timestamp of message/error
	 * @param message   - error/success message
	 * @param details   - additional details
	 */
	public ResponseDetails(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
