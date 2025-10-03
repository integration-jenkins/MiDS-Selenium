package com.avendum.midsautomate.enums;

public enum Departments {
    Circle_MW_Planner("Circle MW Planner"),
    Circle_Operation_Team("Circle Operation Team"),
    Circle_Deployment_Team("Circle Deployment Team"),
    Circle_INS_Partner("Circle I&C Partner"),
    Circle_MS_Partner("Circle MS Partner"),
    MWDT_ADMIN_GROUP("MWDT_ADMIN_GROUP"),
    CIRCLE_CENTRAL_B2B_TEAM("Circle Central B2B Team"),
    Central_NOC("Central NOC");

    private final String departmentName;

    Departments(String departmentName) {
        this.departmentName = departmentName;
    }

    public String departmentName() {
        return departmentName;
    }

    @Override
    public String toString() {
        return departmentName;
    }

    /**
     * Retrieves the Department enum based on the provided name.
     *
     * @param name the department name (case-insensitive, spaces allowed)
     * @return the matching Department enum
     * @throws IllegalArgumentException if no matching department is found
     */
    public static Departments fromName(String name) {
        for (Departments department : Departments.values()) {
            if (department.departmentName.equalsIgnoreCase(name)) {
                return department;
            }
        }
        throw new IllegalArgumentException("No enum constant for department name: " + name);
    }
}
