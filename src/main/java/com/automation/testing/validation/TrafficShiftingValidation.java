package com.automation.testing.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.automation.testing.data.BulkUploadError;
import com.automation.testing.enums.TrafficServiceName;
import com.automation.testing.utile.MapperJsonObjectUtile;

public class TrafficShiftingValidation {

	public static List<String> verifyTsUpload(List<BulkUploadError> actualVal, String path) {
		try {
			Map<String, String> expectedData = MapperJsonObjectUtile.readJsonByPath(path);
			return verifyUploadError(actualVal, expectedData);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	private static boolean matchBySlashIgnoringOrder(String expected, String actual) {
		if (expected == null || actual == null) {
			return false;
		}

		Set<String> expectedSet = Arrays.stream(expected.split("/")).map(String::trim).collect(Collectors.toSet());

		Set<String> actualSet = Arrays.stream(actual.split("/")).map(String::trim).collect(Collectors.toSet());

		return expectedSet.equals(actualSet);
	}

	private static List<String> verifyUploadError(List<BulkUploadError> actualVal, Map<String, String> expectedVal) {

		List<String> result = new ArrayList<>();

		// Both null → no errors
		if (actualVal == null && expectedVal == null) {
			return result;
		}

		// One null → mismatch
		if (actualVal == null || expectedVal == null) {
			result.add("Either actual or expected error list is null");
			return result;
		}

		// Convert actual list to map
		Map<String, String> actualMap = new HashMap<>();
		for (BulkUploadError error : actualVal) {
			actualMap.put(error.getRowNumber(), error.getErrorMessage());
		}

		// Check expected vs actual
		for (Map.Entry<String, String> entry : expectedVal.entrySet()) {
			String row = entry.getKey();
			String expectedMsg = entry.getValue();

			if (!actualMap.containsKey(row)) {
				result.add("Missing expected error at row: " + row);
			} else {
				String actualMsg = actualMap.get(row);

				// 1️⃣ Exact match
				if (expectedMsg.equals(actualMsg)) {
					continue;
				}

				// 2️⃣ Match by splitting with '/' ignoring order
				if (!matchBySlashIgnoringOrder(expectedMsg, actualMsg)) {
					result.add(
							"Error mismatch at row " + row + " | Expected: " + expectedMsg + " | Actual: " + actualMsg);
				}
			}
		}

		// Check unexpected actual errors
		for (Map.Entry<String, String> entry : actualMap.entrySet()) {
			if (!expectedVal.containsKey(entry.getKey())) {
				result.add("Unexpected error found at row: " + entry.getKey() + " | Message: " + entry.getValue());
			}
		}

		return result;
	}

	public static Map<TrafficServiceName, Map<String, String>> getBulkOptionTest() {
		Map<TrafficServiceName, Map<String, String>> mp = new HashMap<>();

		mp.put(TrafficServiceName.BULK_ASSIGNMENT,
				Map.of("2", "CIRCLE||select another circle", "3", "CIRCLE||fill circle as invalid", "4",
						"PLAN ID||set empty", "5", "CIRCLE||set invalid circle", "6", "PLAN ID|| set invalid", "7",
						"ASSIGN TO DEP.||set empty", "8", "ASSIGN TO DEP.||set as invalid", "9",
						"ASSIGN TO USER||set empty", "10", "ASSIGN TO USER||set as invalid", "11",
						"VALID PLAN||ALL INPUT VALID "));
		mp.put(TrafficServiceName.BULK_REOPEN,
				Map.of("2", "PLAN VALIDATION||PLAN NOT COMPLETE", "3", "TS PLAN ID||set empty", "4",
						"TS PLAN ID||set as invalid", "5", "TS REOPEN REMARK||set empty", "6",
						"PLAN VALIDATION||Plan at canceled stage", "7", "PLAN VALIDATION||Plan at Hold stage", "8",
						"PLAN VALIDATION||Plan at New Hop Deployment Pending stage", "9",
						"PLAN VALIDATION||Plan at RA Provisioning Pending stage", "10",
						"PLAN VALIDATION||Plan at TS pending stage"));
		mp.put(TrafficServiceName.PLAN_CANCELLATION,
				Map.of("2", "MIDS TS PLAN ID||INVALID PLAN ID", "3",
						"MIDS TS PLAN ID & TS PLAN CANCELLATION STATUS|| INVALID ID WITH INVALID PLAN CAN. STATUS", "4",
						"TS PLAN CANCELLATION STATUS||SET NO", "5", "CANCELLATION REMARK||SET EMPTY", "6",
						"PLAN VALIDATION||INVALID PLAN ID", "7", "TS PLAN CANCELLATION STATUS||SET EMPTY", "8",
						"PLAN VALIDATION||INVALID PLAN"));
		mp.put(TrafficServiceName.PLAN_DELETE,
				Map.of("2", "PLAN VALIDATION||invalid plan", "3", "DELETION TS STATUS||set empty", "4",
						"DELETION TS STAUS||set invalid", "5", "DELETION BY||set empty", "6",
						"DELETION BY||set invalid", "7", "Deletion Remark||set empty", "8",
						"DELETION BY||set another user", "9", "DELETION PLAN ID||plan stage is canceled", "10",
						"PLAN VALIDATION||plan stage is complete", "11", "PLAN VALIDATIONS||invalid plan"));
		mp.put(TrafficServiceName.HOLD_RETRIEVE,
				Map.of("2", "MIDS TS Plan ID||set empty", "3", "MIDS TS Plan ID||set invalid", "4",
						"TS Issue Category||set empty", "5", "TS Issue Category||set invalid", "6",
						"TS Issue Resolution Remarks||set empty", "7", "TS Issue Resolution Date||set empty", "8",
						"TS Issue Resolution Date||set invalid", "9", "plan validation|| invalid plan ", "10",
						"plan validation||invalid plan", "11", "plan validation|| invalid plan"));
		mp.put(TrafficServiceName.HOLD_UPDATE_STATUS,
				Map.of("2", "PLAN VALIDATION||INVALID PLAN", "3", "PLAN VALIDATION||INVALID INPUTES"));

		return mp;
	}

	public static Map<TrafficServiceName, Map<String, String>> getBulkUploadValidation() {
		Map<TrafficServiceName, Map<String, String>> mp = new HashMap<>();

		mp.put(TrafficServiceName.BASIC_TS_VALIDATION,
				Map.ofEntries(Map.entry("2", "SITE ID||set empty"), Map.entry("3", "PROJECT||set empty"),
						Map.entry("4", "PLANNED POP||set empty"), Map.entry("5", "PLANNED PCM||set empty"),
						Map.entry("6", "CONNECTIVITY TYPE||set empty"), Map.entry("7", "OEM||set empty"),
						Map.entry("8", "TS PLAN RELEASE||set empty"), Map.entry("9", "SITE ID||set invalid"),
						Map.entry("10", "PLANNED POP||set invalid"),
						Map.entry("11", "PCM PATH||set Invalid(SiteA invalid/SiteB valid"),
						Map.entry("12", "PCM PATH||set Invalid(SiteA valid/SiteB invalid"),
						Map.entry("13", "PLAN VALIDATION||fiils only circle and all fields empty")

				));

		mp.put(TrafficServiceName.DNC_VALIDATIONS, Map.ofEntries(Map.entry("2", "DCN RA(HOP 1)||set empty"),
				Map.entry("3", "DCN VLAN(HOP 1)||set empty"), Map.entry("4", "DCN GW(HOP 1)||set empty"),
				Map.entry("5", "Dcn Site B(HOP 1) and Dcn Site A(HOP 1)||invalid inputes"),
				Map.entry("6", "DCN Site A(HOP 1)||set empty"), Map.entry("7", "DCN Site B(HOP 1)||set empty"),
				Map.entry("8", "DCN RA(HOP 2)||set empty"), Map.entry("9", "DCN VLAN(HOP 2)||set empty"),
				Map.entry("10", "DCN VLAN(HOP 2)||set empty"), Map.entry("11", "DCN GW(HOP 2)||set empty"),
				Map.entry("12", "DCN Site B(HOP 2)||set empty"),

				Map.entry("13", "DCN RA(HOP 1)||set invalid"), Map.entry("14", "DCN VLAN(HOP 1)||set invalid"),
				Map.entry("15", "DCN GW(HOP 1)||set invalid"), Map.entry("16", "DCN Site A(HOP 1)||set invalid"),
				Map.entry("17", "DCN Site B(HOP 1)||set invalid"), Map.entry("18", "DCN RA(HOP 2)||set invalid"),
				Map.entry("19", "DCN VLAN(HOP 2)||set invalid"), Map.entry("20", "DCN VLAN(HOP 2)||set invalid"),
				Map.entry("21", "DCN GW(HOP 2)||set invalid"), Map.entry("22", "DCN Site B(HOP 2)||set invalid")));

		mp.put(TrafficServiceName.TS_2G_VALIDATIONS, Map.ofEntries(Map.entry("2", "2G RA||set empty"),
				Map.entry("3", "2G UP VLAN (ABIS)||set empty"), Map.entry("4", "2G UP Network IP (ABIS)||set empty"),
				Map.entry("5", "2G UP GW (ABIS)||set empty"), Map.entry("6", "2G UP IP (ABIS)||set empty"),
				Map.entry("7", "2G MP Network IP (O&M)||set empty"), Map.entry("8", "2G MP GW (O&M)||set empty"),
				Map.entry("9", "2G MP IP (O&M)||set empty"), Map.entry("10", "2G (MPLS/MPBN/CEN)||set empty"),

				Map.entry("11", "2G RA||set invalid"), Map.entry("12", "2G UP VLAN (ABIS)||set invalid"),
				Map.entry("13", "2G UP Network IP (ABIS)||set invalid"),
				Map.entry("14", "2G UP GW (ABIS)||set invalid"), Map.entry("15", "2G UP IP (ABIS)||set invalid"),
				Map.entry("16", "2G MP VLAN (O&M)||set invalid"),
				Map.entry("17", "2G MP Network IP (O&M)||set invalid"), Map.entry("18", "2G MP GW (O&M)||set invalid"),
				Map.entry("19", "2G MP IP (O&M)||set invalid")));

		mp.put(TrafficServiceName.TS_4G_VALIDATIONS,
				Map.ofEntries(Map.entry("2", "4G RA||set empty"), Map.entry("3", "4G BBU1 UP VLAN||set empty"),
						Map.entry("4", "4G BBU1 UP GW||set empty"), Map.entry("5", "4G BBU1 UP IP||set empty"),
						Map.entry("6", "4G BBU1 CP VLAN||set empty"), Map.entry("7", "4G BBU1 CP GW||set empty"),
						Map.entry("8", "4G BBU1 CP IP||set empty"), Map.entry("9", "4G BBU1 MP VLAN||set empty"),
						Map.entry("10", "4G BBU1 MP GW||set empty"), Map.entry("11", "4G BBU1 MP IP||set empty"),

						Map.entry("12", "4G RA||set INVALID"), Map.entry("13", "4G BBU1 UP VLAN||set INVALID"),
						Map.entry("14", "4G BBU1 UP GW||set INVALID"), Map.entry("15", "4G BBU1 UP IP||set INVALID"),
						Map.entry("16", "4G BBU1 CP VLAN||set INVALID"), Map.entry("17", "4G BBU1 CP GW||set INVALID"),
						Map.entry("18", "4G BBU1 CP IP||set INVALID"), Map.entry("19", "4G BBU1 MP VLAN||set INVALID"),
						Map.entry("20", "4G BBU1 MP GW||set INVALID"), Map.entry("21", "4G BBU1 MP IP||set INVALID")));

		mp.put(TrafficServiceName.TS_5G_VALIDATIONS,
				Map.ofEntries(Map.entry("2", "5G Site On Air (Y/N)||set empty"),
						Map.entry("3", "5G Colo RA||set empty"), Map.entry("4", "5G VLAN||set empty"),
						Map.entry("5", "5G GW||set empty"), Map.entry("6", "gNB IP1||set empty"),
						Map.entry("7", "gNB IP2||set empty"), Map.entry("8", "5G Anchor GW||set empty"),
						Map.entry("9", "Anchor eNB IP1||set empty"), Map.entry("10", "Anchor eNB IP2||set empty"),

						Map.entry("11", "5G Site On Air (Y/N)||set invalid"),
						Map.entry("12", "5G Colo RA||set invalid"), Map.entry("13", "5G VLAN||set invalid"),
						Map.entry("14", "5G GW||set invalid"), Map.entry("15", "gNB IP1||set invalid"),
						Map.entry("16", "gNB IP2||set invalid"), Map.entry("17", "5G Anchor GW||set invalid"),
						Map.entry("18", "Anchor eNB IP1||set invalid"), Map.entry("19", "Anchor eNB IP2||set invalid")

				));

		return mp;
	}

}
