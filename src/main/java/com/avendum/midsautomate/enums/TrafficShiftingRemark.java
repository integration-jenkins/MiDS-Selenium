package com.avendum.midsautomate.enums;

public enum TrafficShiftingRemark {

    LOGIN_SUCCESSFULLY("User logged in successfully"),
    LOGIN_FAILED("User login failed"),

    LOGOUT_SUCCESSFULLY("User logged out successfully"),
    LOGOUT_FAILED("User logout failed"),

    BULK_UPLOAD_VALID("Bulk upload executed successfully"),
    BULK_UPLOAD_INVALID("Bulk upload failed due to issues"),

    BULK_UPLOAD_SHEET_INVALID("Upload sheet contains invalid fields"),
    SHEET_INVALID("Upload sheet is valid and all fields are correct"),

    PLAN_OPEN("Plan opened successfully"),
    PLAN_NOT_OPEN("Unable to open plan: plan does not exist or system issue"),

    HOLD_WORKING("Plan hold functionality is working"),
    HOLD_NOT_WORKING("Plan hold functionality is not working"),

    TS_STATUS_FILLED("TS status updated successfully"),
    TS_STATUS_NOT_FILLED("Unable to update TS status"),

    ALL_TEST_PASS("All Traffic Shifting functionalities are working correctly"),
    TEST_FAILED("Issues detected in the Traffic Shifting module"),

    HOLD_RESOLVE_WORKING("Plan hold resolution is working"),
    HOLD_RESOLVE_NOT_WORKING("Plan hold resolution is not working");



    ;


    private final String status;

    TrafficShiftingRemark(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
