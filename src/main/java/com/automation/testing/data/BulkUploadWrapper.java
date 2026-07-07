package com.automation.testing.data;

import java.util.List;

public class BulkUploadWrapper {
	private List<BulkUploadError> errorList;
	private Integer totalNewPlans;
	private Integer totalUpdatePlans;
	private Integer totalInvalidPlans;

	public List<BulkUploadError> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<BulkUploadError> errorList) {
		this.errorList = errorList;
	}

	public Integer getTotalNewPlans() {
		return totalNewPlans;
	}

	public void setTotalNewPlans(Integer totalNewPlans) {
		this.totalNewPlans = totalNewPlans;
	}

	public Integer getTotalUpdatePlans() {
		return totalUpdatePlans;
	}

	public void setTotalUpdatePlans(Integer totalUpdatePlans) {
		this.totalUpdatePlans = totalUpdatePlans;
	}

	public Integer getTotalInvalidPlans() {
		return totalInvalidPlans;
	}

	public void setTotalInvalidPlans(Integer totalInvalidPlans) {
		this.totalInvalidPlans = totalInvalidPlans;
	}

	@Override
	public String toString() {
		return "BulkUploadWrapper [errorList=" + errorList + ", totalNewPlans=" + totalNewPlans + ", totalUpdatePlans="
				+ totalUpdatePlans + ", totalInvalidPlans=" + totalInvalidPlans + "]";
	}

}
