package com.automation.testing.sourcecredentials;

import java.util.List;

import com.automation.testing.enums.Department;

import java.util.Arrays;

public class UserDetails {

	public static List<String> getUserDetails(String department) {
		if (department != null && department.isEmpty())
			return null;
		switch (department) {
		case "CIRCLE MW PLANNER": {
			return Arrays.asList("K_Planner", "plan@123");
		}
		case "CIRCLE DEPLOYMENT TEAM": {
			return Arrays.asList("Test_deploy", "deploy@123");
		}
		case "CIRCLE DINC PARTNER": {
			return Arrays.asList("Test_dinc_partner", "incp@123");
		}
		case "CIRCLE MS PARTNER": {
			return Arrays.asList("Test_ms_partner", "mspr@123");
		}
		case "CIRCLE OPERATION TEAM": {
			return Arrays.asList("Test_ops", "cops@123");
		}

		case "MW PLANNER USER": {
			return Arrays.asList("User_planner", "Easy@321");
		}
		case "DEPLOYMENT USER": {
			return Arrays.asList("User_deploy", "Easy@321");
		}
		case "DINC PARTNER USER": {
			return Arrays.asList("User_dinc_partner", "Easy@321");
		}
		case "MS PARTNER USER": {
			return Arrays.asList("User_ms_partner", "Easy@321");
		}
		case "OPERATION USER": {
			return Arrays.asList("User_ops", "Easy@321");
		}
		}
		return null;
	}

	public static String getAssignName(String department) {
		switch (department) {
		case "CIRCLE MW PLANNER": {
			return "lohate-kartik-K_Planner";
		}
		case "CIRCLE DEPLOYMENT TEAM": {
			return "lohate-kartik-Test_deploy";
		}
		case "CIRCLE DINC PARTNER": {
			return "lohate-kartik-Test_dinc_partner";
		}
		case "CIRCLE MS PARTNER": {
			return "lohate-kartik-Test_ms_partner";
		}
		case "CIRCLE OPERATION TEAM": {
			return "lohate-kartik-Test_ops";
		}
		}
		return null;
	}

	public static List<String> getUserDepList() {
		return List.of(Department.CIRCLE_MW_PLANNER.getName(), Department.CIRCLE_DEPLOYMENT_TEAM.getName(),
				Department.CIRCLE_OPERATION_TEAM.getName(), Department.CIRCLE_DINC_PARTNER.getName(),
				Department.CIRCLE_MS_PARTNER.getName());
	}

	public static List<String> getUserDepListForTS() {
		return List.of(Department.CIRCLE_DEPLOYMENT_TEAM.getName(), Department.CIRCLE_MW_PLANNER.getName(),
				Department.CIRCLE_OPERATION_TEAM.getName());
	}

	public static List<String> getUserDepListForDismantle() {
		return List.of(Department.CIRCLE_MW_PLANNER.getName(), Department.CIRCLE_OPERATION_TEAM.getName(),
				Department.CIRCLE_DEPLOYMENT_TEAM.getName(), Department.CIRCLE_DINC_PARTNER.getName());
	}

}
