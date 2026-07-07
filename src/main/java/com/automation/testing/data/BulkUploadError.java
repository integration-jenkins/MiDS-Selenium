package com.automation.testing.data;

public class BulkUploadError implements Cloneable {

	private String rowNumber;
	private String errorMessage;

	public BulkUploadError(String rowNumber, String errorMessage) {
		this.rowNumber = rowNumber;
		this.errorMessage = errorMessage;
	}

	public String getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "BulkUploadError [rowNumber=" + rowNumber + ", errorMessage=" + errorMessage + "]";
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return new BulkUploadError(this.rowNumber, this.errorMessage);
	}

}
