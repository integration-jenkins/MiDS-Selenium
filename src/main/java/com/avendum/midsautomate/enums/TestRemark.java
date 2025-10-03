package com.avendum.midsautomate.enums;

public enum TestRemark {

    LOGIN_SUCCESSFULLY("user login successfully"),
    LOGIN_FAILED("user login failed"),
    LOGOUT_SUCCESSFULLY("user logout successfully"),
    LOGOUT_FAILED("user logout failed"),

    DO_ABLE_PASS("user can fill do-able remark"),
    DO_ABLE_FAIL("user not able to fill do-able remark"),

    PLAN_UPLOAD_WORK("User can upload the Plan"),
    PLAN_UPLOAD_FAILED("User can't Upload Plan"),

    PLANNER_UPLOAD_SHEET_PASS("Planner Upload Sheet is Valid"),
    PLANNER_UPLOAD_SHEET_FAIL("Planner Upload Sheet is Invalid"),

    PLAN_OPEN("user can open the plan"),
    PLAN_NOT_OPEN("user not able to open the plan"),

    TS_RUNNING_APPROVE("user able to approve TS running Details"),
    TS_RUNNING_NOT_APPROVE("user not able to approve TS running Details"),

    TS_IWAN_STATUS_WORK("user able to fill TS status & IWAN Status"),
    TS_STATUS_NOT_WORK("user not able to fill TS status"),
    IWAN_STATUS_NOT_WORK("user not able to fill IWAN Status"),

    PLAN_NOT_ASSIGN("user not able to assign plan to any user"),
    PLAN_ASSIGN_WORK("user able to assign plan to another user"),

    SURVEY_DETAILS_WORK_BOTH_SITE("user able to fill site survey details"),
    SURVEY_DETAILS_BOTH_SITE_ISSUE("user not able to fill site survey details"),

    ALL_COMPONENT_FILLED("user able to fill component details"),
    ISSUE_IN_COMPONENT("user not able to fill component details"),

    NMS_DONE("user able to fill NMS status"),
    NMS_NOT_WORK("user not able to fill NMS status"),

    // Traffic Shifting Status
    TS_PENDING("All details are valid plan move to TS Pending Stage"),
    TS_PENDING_ISSUE("Invalid Details In TS Upload Sheet"),

    LOGIN("Login working"),
    LOGIN_NOT_WORKING("Login Not Working"),

    PLAN_HOLD("Plan Hold is working"),
    PLAN_HOLD_NOT_WORK("Issue in Plan Holding"),

    TS_COMPLETE("All workFlow is working, Plan Completed"),
    TS_NOT_COMPLETE("All workFlow is not working, Issue is TS Done Status Field"),

    PASSED("All workflow is working, No Issue"),
    FAILED("Testing Failed, Issue in System"),

    HOLD_CANNOT_RESOLVE("Hold Plan can't be resolve")


    ;







    private final String status;

    TestRemark(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
