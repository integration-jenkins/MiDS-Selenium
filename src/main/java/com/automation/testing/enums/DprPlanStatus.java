package com.automation.testing.enums;

public enum DprPlanStatus {

	SR_PENDING("SR PENDING"), MO_PENDING("MO PENDING"), I_AND_C_PENDING("I&C PENDING"),
	PHT_SOFT_AT_PENDING("PHY+SOFT AT PENDING"), PHY_SOFT_AT_RAISED("PHY-AT RAISED/SOFT-AT RAISED"),
	AT_ACCEPTED("AT ACCEPTED"), TS_DONE("Traffic Shifting Completed");

	private final String status;

	DprPlanStatus(String path) {
		this.status = path;
	}

	public String getStatus() {
		return status;
	}

}
