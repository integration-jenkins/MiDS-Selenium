package com.automation.testing.enums;

public enum ReportCountPath {

	SOFT_AT_REPORT(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/span"),
	MW_PLANING_REPORT(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[3]/span"),
	LB_REPORTS("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[3]/span"),
	DPR_AND_ASSIGNMENT_REPORT(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[3]/span"),
	DEPLOYMENT_AND_PRI_REPORT(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[2]/span")

	, URB_LB_REPORT("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/span");

	private final String path;

	ReportCountPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public String getPathForOpsAndDeployUser(Integer idx) {
		return "/html[1]/body[1]/div[3]/app-layout-left-hybrid[1]/div[2]/div[1]/vaadin-vertical-layout[1]/div[1]/vaadin-vertical-layout[1]/div[3]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a["
				+ idx + "]";
	}

}
