package com.automation.testing.enums;

public enum BulkUploadPath {

	MW_PLAN_TRACKING_BULK_UPLOAD_OPTIONS(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[4]/vaadin-horizontal-layout/div"),

	BULK_AT_UPLOAD(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[4]/vaadin-horizontal-layout/vaadin-horizontal-layout/vaadin-button[2]"),
	AT_UPLOAD_DATA("//*[@id=\"overlay\"]/flow-component-renderer/div/div/p[1]");

	private final String path;

	BulkUploadPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}
