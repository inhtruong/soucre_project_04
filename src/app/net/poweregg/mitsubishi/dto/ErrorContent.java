package net.poweregg.mitsubishi.dto;

/********************************************************************************
 * ErrorContent
 * 
 * @author : minhhtp
 * @createDate : 2021/02/23
 *
 ********************************************************************************/
public class ErrorContent {
	/** Error row */
	private String errorRow;
	/** Error message */
	private Object errorMessage;

	public ErrorContent() {

	}

	public String getErrorRow() {
		return errorRow;
	}

	public void setErrorRow(String errorRow) {
		this.errorRow = errorRow;
	}

	public Object getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(Object errorMessage) {
		this.errorMessage = errorMessage;
	}
}
