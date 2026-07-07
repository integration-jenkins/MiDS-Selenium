package com.automation.testing.data;

public class TestStatusData {
	private String remark;
	private boolean status;
	private String stage;

	public TestStatusData(String remark, boolean status, String stage) {
		this.remark = remark;
		this.status = status;
		this.stage = stage;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}
}
