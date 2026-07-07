package com.automation.testing.validation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.automation.testing.enums.ProcessName;

public class NewDeployBulkValidation {

	private static Map<ProcessName, Map<String, List<String>>> mp = new HashMap<>();

	static {

		mp.put(ProcessName.BULK_ASSIGNMENT,
				new LinkedHashMap<>(Map.of("2", Arrays.asList("ASSIGNMENT TO DEP.", "Is empty"), "3",
						Arrays.asList("Plan ID", "Fills empty"), "4", Arrays.asList("Assign To User", " Fills empty"),
						"6", Arrays.asList("Plan Id", "Fills as Invalid"), "7",
						Arrays.asList("Assign To Dep.", "Fill Invalid"), "8",
						Arrays.asList("Assign To User", "Fills as Invalid"), "12",
						Arrays.asList("PLAN CONDITION", "User assign plan to Deployment User"), "13",
						Arrays.asList("PLAN CONDITION", "User assign plan to Operation User"), "14",
						Arrays.asList("PLAN CONDITION", "User assign plan to MW Planner User"), "15",
						Arrays.asList("PLAN CONDITION", "User assign plan to Deployment User"))));

		mp.get(ProcessName.BULK_ASSIGNMENT).put("5",
				Arrays.asList("PLAN CONDITION", "User assign plan to another user"));
		mp.get(ProcessName.BULK_ASSIGNMENT).put("9",
				Arrays.asList("PLAN CONDITION", "User assign plan to another user"));

		mp.put(ProcessName.REQUEST_FOR_CANCELLATION,
				new LinkedHashMap<>(
						Map.of("2", Arrays.asList("Plan Id", "set empty"), "3", Arrays.asList("Plan Id", "set Invalid"),
								"4", Arrays.asList("RFC Confirmation Status", "set empty"), "5",
								Arrays.asList("RFC Confirmation Status", "set Invalid"), "7",
								Arrays.asList("RFC Additional Remark", "set empty"), "8",
								Arrays.asList("RFC Confirmation Status", "set NO"), "9",
								Arrays.asList("RFC Confirmation Status", "set yesno as value"), "10",
								Arrays.asList("Plan Id", "set Empty"), "11", Arrays.asList("Plan Id", "set Invalid"),
								"12", Arrays.asList("Reason For Cancellation", "set Invalid"))));

		mp.get(ProcessName.REQUEST_FOR_CANCELLATION).put("13", Arrays.asList("Reason For Cancellation", "set EMPTY"));
		mp.get(ProcessName.REQUEST_FOR_CANCELLATION).put("6", Arrays.asList("PLAN CONDITION", "This plan not exists"));

		mp.put(ProcessName.BULK_CANCELLATION,
				new LinkedHashMap<>(Map.of("2", Arrays.asList("Invalid Plan", "Plan not assign to current User"), "3",
						Arrays.asList("Planner Cancellation Remark", "set as EMPTY"), "4",
						Arrays.asList("Planner Cancellation Remark", "set as Invalid"), "5",
						Arrays.asList("Additional Remark", "set as empty"), "6", Arrays.asList("Plan id", "set empty"),
						"7", Arrays.asList("Plan ID", "set Invald"))));

		mp.put(ProcessName.BULK_UPLOAD_FOR_SOFT_UPGRADE,
				new LinkedHashMap<>(Map.of("2", Arrays.asList("Invalid plan", "Upgrade Already Completed"), "3",
						Arrays.asList("Plan id", "Invalid Plan id"), "4", Arrays.asList("Plan Id", "set empty"), "5",
						Arrays.asList("Plan Id", "set as INVALID"), "6",
						Arrays.asList("SOFT Upgrade Status", "set as INVALID"), "7",
						Arrays.asList("SOFT Upgrade Status", "set as empty"), "8",
						Arrays.asList("SOFT Upgrade BW", "set as empty"), "9",
						Arrays.asList("SOFT Upgrade BW", "set as INVALID"), "10",
						Arrays.asList("SOFT Upgrade Remark", "set as empty"), "11",
						Arrays.asList("SOFT Upgrade Remark", "set as INVALID"))));
	}

	public static Map<ProcessName, Map<String, List<String>>> getValidations() {
		return mp;
	}

}
