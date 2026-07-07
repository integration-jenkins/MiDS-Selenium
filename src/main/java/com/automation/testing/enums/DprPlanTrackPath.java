package com.automation.testing.enums;

public enum DprPlanTrackPath {

	SR_RAFI_BUTTON(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[3]/vaadin-button[1]"),
	MATERIAL_STATUS_BUTTON(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[3]/vaadin-button[2]"),
	CANCELLED_BUTTON(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[3]/vaadin-button[3]"),
	INSTALLATION_BUTTON(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[3]/vaadin-button[4]"),
	ACCEPTANCE_TEST_BUTTON(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[3]/vaadin-button[5]"),
	TRAFFIC_SHIFTING_BUTTON(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[3]/vaadin-button[6]"),
	SOFT_UPGRADE_BUTTON(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[3]/vaadin-button[7]"),

	SR_RAFI_TAB("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-tabs/vaadin-tab[2]"),
	MATERIAL_ORDER_TAB(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-tabs/vaadin-tab[3]"),
	INSTALL_AND_COMM_TAB(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-tabs/vaadin-tab[4]"),
	ACCEPTANCE_TEST_TAB(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-tabs/vaadin-tab[5]"),
	TRAFFIC_SHIFTING_TAB(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-tabs/vaadin-tab[6]"),

	STATUS_PATH(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/div[2]/label"),
	SAVE_BUTTON("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-tabs/vaadin-button[1]"),
	SEARCH_BY_LINK_ID(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[2]/vaadin-grid/vaadin-grid-cell-content[1073]/flow-component-renderer/div/vaadin-text-field"),
	SEARCH_BY_PLAN_ID(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[2]/vaadin-grid/vaadin-grid-cell-content[1072]/flow-component-renderer/div/vaadin-text-field"),

	PLAN_TRACKING_BUTTON(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[2]"),
	PLAN_SELECT_BUTTON(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[1]"),

	AT_RAISE_BUTTON(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-vertical-layout[1]/vaadin-button"),
	CANCEL_AT_WINDOW_BUTTON("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-vertical-layout/vaadin-button"),
	CANCEL_NOTIFICATION_BUTTON(
			"//*[@id=\"vaadin-notification-card\"]/flow-component-renderer/div/vaadin-horizontal-layout/vaadin-button"),

	AT_ACCEPT_OPEN_BUTTON(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-vertical-layout[1]/vaadin-select"),
	AT_ACCEPT_BUTTON("/html/body/vaadin-select-overlay/vaadin-list-box/vaadin-item[1]"),
	AT_REJECT_BUTTON("/html/body/vaadin-select-overlay/vaadin-list-box/vaadin-item[2]"),

	CLOSE_BUTTON("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-tabs/vaadin-button[2]"),
	REJECT_SOFT_AT_BOX(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-vertical-layout[2]/vaadin-horizontal-layout[1]/vaadin-combo-box[2]"),
	REJECT_SOFT_AT_REMARK(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-vertical-layout[2]/vaadin-horizontal-layout[2]/vaadin-text-area[2]"),
	SELECT_DEPARTMENT(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[1]/vaadin-combo-box[1]"),
	SELECT_USER(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[1]/vaadin-combo-box[3]"),
	SELECT_AT_TYPE(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[1]/vaadin-combo-box[2]"),
	SELECT_USER_BY_COUNT(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[4]/vaadin-combo-box"),
	SEARCH_ASSIGN_TO_DEP(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[2]/vaadin-grid/vaadin-grid-cell-content[1077]/flow-component-renderer/div/vaadin-text-field"),
	SEARCH_BY_PLAN_STAGE(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[2]/vaadin-grid/vaadin-grid-cell-content[1091]/flow-component-renderer/div/vaadin-text-field"),
	CANCAEL_CHEK_BOX_BUTTON(
			"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-checkbox");

	private final String path;

	DprPlanTrackPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
