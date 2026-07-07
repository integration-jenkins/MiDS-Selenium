package com.automation.testing.enums;

public enum DismantlePath {

	DISMNATLE_OPTIONS(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-horizontal-layout/div"),
	DOWNLOAD_TRACK_PAGE_REPORT(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-horizontal-layout/vaadin-horizontal-layout/vaadin-button[1]"),
	DOWNLOAD_DATA_REPORT(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/vaadin-button"),
	DOWNLOAD_HISTORY_AND_DELETE_HISTORY(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-button"),

	DOWNLOAD_PLAN_JUN(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-button"),

	DATA_REPORT_COUNT(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[3]/span"),
	TRACK_AND_PLAN_JUN_COUNT(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/span"),
	HISTORY_AND_DELETE_COUNT("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/span"),

	;

	private final String path;

	DismantlePath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public String getBulkOptionsPath(Integer idx) {
		return "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-horizontal-layout/vaadin-horizontal-layout/vaadin-button["
				+ idx + "]";
	}

}