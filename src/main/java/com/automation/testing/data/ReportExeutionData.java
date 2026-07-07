package com.automation.testing.data;

import com.automation.testing.enums.ProcessName;

public class ReportExeutionData {

	private ProcessName reportName;
	private String report;
	private boolean isCompleted;
	private Long startTime;
	private String executionTime;

	public ProcessName getReportName() {
		return reportName;
	}

	public void setReportName(ProcessName reportName) {
		this.reportName = reportName;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public String getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(String executionTime) {
		this.executionTime = executionTime;
	}

}
