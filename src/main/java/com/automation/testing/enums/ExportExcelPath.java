package com.automation.testing.enums;

public enum ExportExcelPath {

	MW_LB_REPORT_DOWNLOAD_PATH(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/vaadin-button"),
	UBR_BL_REPORT_DOWNLOAD_PATH(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[2]/a/iron-icon"),
	DPR_REPORT_DOWNLOAD_PATH(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/vaadin-button"),
	DEPLOYMENT_ASSIGNMENT_REPORT_DOWNLOAD_PATH(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/vaadin-button"),
	PRI_ISSUE_HISTORY_DOWNLOAD_PATH(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/vaadin-button"),
	ASSIGNMENT_HISTORY_DOWNLOAD_PATH(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/vaadin-button"),
	SOFT_AT_REPORT_DOWNLOAD(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-button");

	private final String path;

	ExportExcelPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}
