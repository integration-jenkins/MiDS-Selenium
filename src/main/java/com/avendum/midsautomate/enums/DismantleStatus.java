package com.avendum.midsautomate.enums;

public enum DismantleStatus {
    DISMANTLE_DOABLE_STATUS_PENDING("Dismantle Do-able Status Pending"),
    TS_CONFIRMATION_PENDING("TS Confirmation Pending"),
    IWAN_TS_PENDING("IWAN TS Pending"),
    MATERIAL_SURVEY_PENDING("Material Survey Pending"),
    MATERIAL_DISMANTLE_PENDING("Material Dismantle Pending"),
    MATERIAL_DISMANTLE_DONE("Material Dismantle Done"),
    PARTNER_ALLOCATION_PENDING("Partner Allocation Pending"),
    SITE_A_ACCESS_ISSUE_SITE_B_MATERIAL_SURVEY_PENDING("Site A Access Issue / Site B Material Survey Pending"),
    SITE_A_ACCESS_ISSUE_SITE_B_MATERIAL_DISMANTLE_PENDING("Site A Access Issue / Site B Material Dismantle Pending"),
    SITE_A_ACCESS_ISSUE_SITE_B_MATERIAL_DISMANTLE_DONE("Site A Access Issue / Site B Material Dismantle Done"),
    SITE_A_MATERIAL_SURVEY_PENDING_SITE_B_ACCESS_ISSUE("Site A Material Survey Pending / Site B Access Issue"),
    SITE_A_MATERIAL_DISMANTLE_PENDING_SITE_B_ACCESS_ISSUE("Site A Material Dismantle Pending / Site B Access Issue"),
    SITE_A_MATERIAL_DISMANTLE_DONE_SITE_B_ACCESS_ISSUE("Site A Material Dismantle Done / Site B Access Issue"),
    SITE_A_MATERIAL_SURVEY_PENDING_SITE_B_MATERIAL_DISMANTLE_PENDING("Site A Material Survey Pending / Site B Material Dismantle Pending"),
    SITE_A_SITE_B_ACCESS_ISSUE("Site A + Site B Access Issue"),
    SITE_A_MATERIAL_SURVEY_PENDING_SITE_B_MATERIAL_DISMANTLE_DONE("Site A Material Survey Pending / Site B Material Dismantle Done"),
    SITE_A_MATERIAL_DISMANTLE_DONE_SITE_B_MATERIAL_SURVEY_PENDING("Site A Material Dismantle Done / Site B Material Survey Pending"),
    SITE_A_MATERIAL_DISMANTLE_PENDING_SITE_B_MATERIAL_DISMANTLE_DONE("Site A Material Dismantle Pending / Site B Material Dismantle Done"),
    SITE_A_MATERIAL_DISMANTLE_DONE_SITE_B_MATERIAL_DISMANTLE_PENDING("Site A Material Dismantle Done / Site B Material Dismantle Pending"),
    SITE_A_MATERAL_DISMANTLE_PENDING_SITE_B_MATERIAL_SURVEY_PENDING("Site A Material Dismantle Pending / Site B Material Survey Pending"),
    SITE_A_MATERAL_SURVEY_PENDING_SITE_B_MATERIAL_DISMANTLE_PENDING("Site A Material Survey Pending / Site B Material Dismantle Pending"),
    TRAFFIC_RELEASE_PENDING("TS Release Pending"),
    DELOADING_SR_COMPLETED_NMS_DELETION_COMPLETED("Deloading SR Completed / NMS Deletion Completed"),
    DELOADING_SR_PENDING_NMS_DELETION_COMPLETED("Deloading SR Pending / NMS Deletion Completed"),
    DELOADING_SR_COMPLETED_NMS_DELETION_PENDING("Deloading SR Completed / NMS Deletion Pending"),
    DELOADING_SR_PENDING_NMS_DELETION_PENDING("Deloading SR Pending / NMS Deletion Pending");

    private final String status;

    DismantleStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static DismantleStatus fromString(String status) {
        for (DismantleStatus dismantleStatus : DismantleStatus.values()) {
            if (dismantleStatus.status.equalsIgnoreCase(status)) {
                return dismantleStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
