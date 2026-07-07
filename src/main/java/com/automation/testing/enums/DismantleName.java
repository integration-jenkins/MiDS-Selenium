package com.automation.testing.enums;

import com.automation.testing.interfaces.CommanProcessName;

public enum DismantleName implements CommanProcessName{

	TRACK, DATA_REPORT, HISTORY, DELETE_HISTORY, PLAN_JUN, UPLOAD, DOWNLOAD_SRN,

	DOWNLOAD_TRACK_REPORT, DOWNLOAD_HISTORY, DOWNLOAD_DATA_REPORT, DOWNLOAD_DELETE_HISTORY, DOWNLOAD_PLAN_JUN,

	USER_LOGIN,

	NAVIGATE_TO_DISMANTLE_UPLOAD, UPLOAD_SHEET,

	DISMANTLE_BULK_ASSIGNMENT, DISMANTLE_DELETE, DISMANTLE_UPLOAD, UPLOAD_TS_RELEASE,
	DISMANTLE_BULK_STATUS_UPDATE, DISMANTLE_BULK_REJECTION, BULK_UPLOAD_TEST;
	
	@Override
	public String getValue(CommanProcessName processName) {
		// TODO Auto-generated method stub
		return name();
	}

}
