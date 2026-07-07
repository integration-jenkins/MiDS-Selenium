package com.automation.testing.enums;

public enum NavigateButtonPath {
	// LB BUTTONS PATHS
	LB_BUTTON(
			"/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[3]"),
	LB_BUTTON_FOR_OPS(
			"/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[2]"),
	MW_LB_BUTTON(
			"/html[1]/body[1]/div[3]/app-layout-left-hybrid[1]/div[2]/div[1]/vaadin-vertical-layout[1]/div[1]/vaadin-vertical-layout[1]/div[3]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a[1]"),
	URB_LB_REPORT(
			"/html[1]/body[1]/div[3]/app-layout-left-hybrid[1]/div[2]/div[1]/vaadin-vertical-layout[1]/div[1]/vaadin-vertical-layout[1]/div[3]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a[2]"),
	MW_LB_BUTTON_OPS(
			"/html[1]/body[1]/div[3]/app-layout-left-hybrid[1]/div[2]/div[1]/vaadin-vertical-layout[1]/div[1]/vaadin-vertical-layout[1]/div[2]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a[1]"),
	UBR_LB_BUTTON_OPS(
			"/html[1]/body[1]/div[3]/app-layout-left-hybrid[1]/div[2]/div[1]/vaadin-vertical-layout[1]/div[1]/vaadin-vertical-layout[1]/div[2]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a[2]"),

	// DEPLOYMENT BUTTONS PATH
	DEPLOYMENT_REPORT(
			"/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[5]"),
	DPR_REPORT(
			"/html[1]/body[1]/div[3]/app-layout-left-hybrid[1]/div[2]/div[1]/vaadin-vertical-layout[1]/div[1]/vaadin-vertical-layout[1]/div[5]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a[1]"),
	SOFT_AT_REPORT(
			"/html[1]/body[1]/div[3]/app-layout-left-hybrid[1]/div[2]/div[1]/vaadin-vertical-layout[1]/div[1]/vaadin-vertical-layout[1]/div[5]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a[2]"),
	DEPLOYMENT_ASSIGNMENT_REPORT(
			"/html[1]/body[1]/div[3]/app-layout-left-hybrid[1]/div[2]/div[1]/vaadin-vertical-layout[1]/div[1]/vaadin-vertical-layout[1]/div[5]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a[3]"),
	PRI_ISSUE_HISTORY(
			"/html[1]/body[1]/div[3]/app-layout-left-hybrid[1]/div[2]/div[1]/vaadin-vertical-layout[1]/div[1]/vaadin-vertical-layout[1]/div[5]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a[4]"),
	ASSIGNMENT_REPORT(
			"/html[1]/body[1]/div[3]/app-layout-left-hybrid[1]/div[2]/div[1]/vaadin-vertical-layout[1]/div[1]/vaadin-vertical-layout[1]/div[5]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a[5]"),
	DEPLOYMENT_REPORT_FOR_OPS(
			"/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[3]"),
	DEPLOYMENT_REPORT_FOR_DINC(
			"/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[4]"),
	
	// MW Plan Tracking Path
	PLAN_ULOAD_BUTTON(
			"/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[7]"),
	MEDIA_PLANNING_BUTTON(
			"/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[2]"),
	MW_PLAN_TRACKING(
			"/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[2]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a[2]"),
	MW_PLAN_TRACKING_DINC(
			"/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[2]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a[1]"),

	UBR_PLAN_TRACKING(
			"/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[2]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a[1]"),
	SOFT_AT_BULK_UPLOAD(
			"/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[9]"),
	SOFT_AT_BULK_UPLOAD_DINC(
			"/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[7]")

	;

	private final String path;

	NavigateButtonPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}
