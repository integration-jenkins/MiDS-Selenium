package com.automation.testing.data;

import java.time.LocalDate;

public class ProcessData {
	private String name;
	private String executionTime;
	private boolean executionStatus;
	private LocalDate executionDate;
	private String remark;
	private String userDepartment;
	private Integer reportCount;
	private String attributeName;
	private String attributeRemark;
	private String assignedDep;
	private String notification;
	private String stage;

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeRemark() {
		return attributeRemark;
	}

	public void setAttributeRemark(String attributeRemark) {
		this.attributeRemark = attributeRemark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(String executionTime) {
		this.executionTime = executionTime;
	}

	public boolean getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(boolean executionStatus) {
		this.executionStatus = executionStatus;
	}

	public LocalDate getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(LocalDate executionDate) {
		this.executionDate = executionDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserDepartment() {
		return userDepartment;
	}

	public void setUserDepartment(String userDepartment) {
		this.userDepartment = userDepartment;
	}

	public Integer getReportCount() {
		return reportCount;
	}

	public void setReportCount(Integer reportCount) {
		this.reportCount = reportCount;
	}

	public String getAssignedDep() {
		return assignedDep;
	}

	public void setAssignedDep(String assignedDep) {
		this.assignedDep = assignedDep;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}
	
	

}
