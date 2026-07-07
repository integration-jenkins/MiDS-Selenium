package com.automation.testing.enums;

public enum TrafficShiftingPath {

	TS_UPLOAD("1"), TRAFFIC_REPORT("2"), TRAFFIC_TRACK_VIEW("3"), TS_ASSIGNMENT_HISTORY("4"), TS_DELECTION("5"),

	TRAFFIC_AND_DEL_REPORT_DOW_PATH(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-button"),
	DEL_REPORT_COUNT_PATH(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/span"),
	ASSIGNMENT_REPORT_DOW_PATH("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-button"),
	TRAFIC_TRACK_VIEW_COUNT_PATH("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-horizontal-layout/span"),
	ASSIGNMENT_REPORT_COUNT_PATH("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/span"),
	DOWNLOAD_TRAFFIC_REPORT(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/vaadin-button"),
	TRACK_VIEW_SEARCH_PATH(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[551]/flow-component-renderer/div/vaadin-text-field"),
	OPEN_PLAN_PLAN(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[2]")

	,
	CANCEL_PLAN(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[3]/vaadin-vertical-layout/vaadin-horizontal-layout/div[2]"),
	RESOLVE_HOLD(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[3]/vaadin-vertical-layout/vaadin-horizontal-layout/div[1]");

	private String path;

	TrafficShiftingPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

}
