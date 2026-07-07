package com.automation.testing.data;

import com.automation.testing.enums.ProcessName;
import com.automation.testing.enums.TestType;

public class BulkUploadValidationData {

	private ProcessName processName;
	private TestType testType;
	private String remark;
	private String circle;
	private String sheetPath;
	private String planId;

	public ProcessName getProcessName() {
		return processName;
	}

	public void setProcessName(ProcessName processName) {
		this.processName = processName;
	}

	public TestType getTestType() {
		return testType;
	}

	public void setTestType(TestType testType) {
		this.testType = testType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getSheetPath() {
		return sheetPath;
	}

	public void setSheetPath(String sheetPath) {
		this.sheetPath = sheetPath;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

}
