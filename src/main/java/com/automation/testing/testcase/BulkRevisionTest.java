package com.automation.testing.testcase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.data.BulkUploadError;
import com.automation.testing.data.ProcessData;
import com.automation.testing.enums.BulkUploadPath;
import com.automation.testing.enums.Department;
import com.automation.testing.enums.ProcessName;
import com.automation.testing.service.BulkUploadService;
import com.automation.testing.service.CreateResultSheet;
import com.automation.testing.service.DprPlanCompletionService;
import com.automation.testing.service.UserService;
import com.automation.testing.utile.MidsUtile;
import com.automation.testing.utile.ProcessCalculationUtile;

@Component
public class BulkRevisionTest {
	private final UserService userService;
	private final WebDriverWait wait;
	private final WebDriver driver;
	private final CreateResultSheet createResultSheet;
	private final BulkUploadService bulkUploadService;
	private final DriverConfig driverConfig;

	public BulkRevisionTest(UserService userService, WebDriverWait wait, WebDriver driver,
			CreateResultSheet createResultSheet, BulkUploadService bulkUploadService, DriverConfig driverConfig) {
		this.userService = userService;
		this.wait = wait;
		this.driver = driver;
		this.createResultSheet = createResultSheet;
		this.bulkUploadService = bulkUploadService;
		this.driverConfig = driverConfig;
	}

	private ProcessCalculationUtile process = new ProcessCalculationUtile();
	private List<ProcessData> data = new ArrayList<>();

	private final String BASE_PATH = "D:\\testingProject\\testing\\src\\main\\resources\\project_file\\new_dep_bulk_sheet\\bulk_revision\\";
	private final String USER = Department.CIRCLE_MW_PLANNER.getName();

	private Map<String, String> bulkTest1 = Map.of("2", "Plan ID|set empty", "3", "Plan ID|set invalid", "4",
			"Change Plan Status|set empty", "5", "Change Plan Status|set invalid", "6", "LB Revision Status|set empty",
			"7", "LB Revision Status|set invalid", "8", "NOT ASSIGNED|plan not assign", "9",
			"INVALID PLAN STAGE|PLan stage is SR-RAFI/AT ACCEPTED", "10", "plan id|use same plan id");

	private Map<String, String> bulkTest2 = Map.of("2", "Nominal Aop, Nominal Quarter, Final Project|set empty", "3",
			"Nominal Aop, Nominal Quarter, Final Project|set INVALID", "4",
			"Hop Nomenclature Site-A Hop, Nomenclature Site-B|set invalid & empty", "5",
			"Link ID, Channel, PCM PATH, Fiber POP Id, Remarks, Assigned To, Assigned To Department,Task Status , Cancellation Reason	, Last Updated Date	, AT Completed Date,	TS Completed Date, 	Hop Type , DCN RA Number , DCN RA Status, DCN VLAN, GATEWAY IP, Site A End IP, Site B End IP, Antenna Beam Width, Node ID Site A, Node ID Site B, MO Number Site A, MO Number Site B	For Site ID, MRMC Script ID, TS Plan Released Status, TS Plan ID|set invalid & empty fields",
			"6",
			"Site A Lat, Site A Long, Site B Lat, Site B Long, Availability, (MODEM(IF Card)/Ethernet Port) Site-A, (MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-A|set empty & invalid inputes",
			"7", "Availability, ATPC Status|set empty & invalid", "8",
			"Tx Site Elevation (m), BER10e6 Tx Power (dBm), Tx Frequency (MHz), BER10e6 Rx Level (dBm), Tx Ant. Azimuth (°),Tx Ant. Height (m)	Tx Antenna, Tx Ant. Gain (dB), BER10e6 EIRP (dBm)|set invalid inputes",
			"9",
			"Site ID -B, Site B Long	Rx Site Elevation (m), Rx Frequency (MHz), Rx Ant. Azimuth (°), Rx Ant. Height (m), Rx Antenna	Rx Ant. Gain (dB), Tx Radio, Distance (km), Bandwidth (MHz), BER10e6 Eff. Margin (dB), BER10e6 Rain, Interference Thr. Deg. (dB)|set invalid",
			"10",
			"Site ID -B, ACM Min QAM, (MODEM(IF Card)/Ethernet Port) Site-B, (MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-B|set invalid & empty",
			"11",
			"Site ID -B, ACM Min QAM, (MODEM(IF Card)/Ethernet Port) Site-B, (MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-B|set empty");

	public void run() {

		runStep(ProcessName.USER_LOGIN, () -> userService.userLogin(USER), "User able to login", "Failed to user login",
				null, USER, null);

		uploadBulkRevision(ProcessName.BULK_REVISION_ATTRIBUTE_VALIDATIONS, bulkTest1,
				BASE_PATH + "BULK_REVISION_1.xlsx");
		driverConfig.refreshDriver(driver);

		uploadBulkRevision(ProcessName.BULK_REVISION_LB_TEST, bulkTest2, BASE_PATH + "BULK_REVISION_2.xlsx");
		driverConfig.refreshDriver(driver);

		uploadBulkRevision(ProcessName.BULK_REVISION_LB_TEST, Map.of("2", "VALIDATION|LB is missing"),
				BASE_PATH + "BULK_REVISION_4.xlsx");
		driverConfig.refreshDriver(driver);

		uploadBulkRevision(ProcessName.BULK_REVISION_LB_TEST, Map.of("1", "VALIDATION|revision sheet is missing"),
				BASE_PATH + "BULK_REVISION_5.xlsx");
		driverConfig.refreshDriver(driver);

		runStep(ProcessName.DOWNLOAD_SMAPLE_FILE, () -> {
			boolean b = true;
			try {
				b &= MidsUtile.clickElement(wait, BulkUploadPath.MW_PLAN_TRACKING_BULK_UPLOAD_OPTIONS.getPath());
				String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[4]/vaadin-horizontal-layout/vaadin-horizontal-layout/vaadin-button[6]";
				b &= MidsUtile.clickElement(wait, path);

				b &= bulkUploadService.downloadSampleSheet();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return b;
		}, "Sample File Download successfully", "Failed to download sample file", null, USER, null);
		driverConfig.refreshDriver(driver);

		uploadBulkRevision(ProcessName.BULK_REVISION_LB_TEST, Map.of("2", "VALIDATION|empty Revision & Lb Sheet"),
				BASE_PATH + "BULK_REVISION_6.xlsx");
		driverConfig.refreshDriver(driver);
//
//		runStep(ProcessName.BULK_REVISION, () -> {
//			String circle = "JK";
//			boolean b = true;
//			b &= MidsUtile.clickElement(wait, BulkUploadPath.MW_PLAN_TRACKING_BULK_UPLOAD_OPTIONS.getPath());
//			String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[4]/vaadin-horizontal-layout/vaadin-horizontal-layout/vaadin-button[6]";
//			b &= MidsUtile.clickElement(wait, path);
//			b &= bulkUploadService.uploadSheet(circle, BASE_PATH + "BULK_REVISION_3.xlsx",
//					new ArrayList<BulkUploadError>());
//			verifyCounts();
//			return b;
//		}, "Performance Test with 50 plans", "Failed to test with 50 plans", null, USER, null);
//		driverConfig.refreshDriver(driver);

		userService.logOut(driver, wait);
		createResultSheet.createResultBulkUpload("bulk_Revision", data);
	}

	private void verifyCounts() {
		// TODO Auto-generated method stub
		try {

			int count = bulkUploadService.getBulkUploadWrapper().getTotalNewPlans();
			int error = bulkUploadService.getBulkUploadWrapper().getTotalInvalidPlans();

			boolean b = count == error;

			runStep(ProcessName.COUNT_VERIFICATION, () -> b, "All count is valid after upload",
					"Count is invalid[total plans = " + count + ", error count = " + error + "]", null, USER, null);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private boolean uploadBulkRevision(ProcessName name, Map<String, String> map, String sheetPath) {
		List<BulkUploadError> errorList = new ArrayList<>();
		try {

			runStep(name, () -> {
				String circle = "JK";
				boolean b = true;
				b &= MidsUtile.clickElement(wait, BulkUploadPath.MW_PLAN_TRACKING_BULK_UPLOAD_OPTIONS.getPath());
				String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[4]/vaadin-horizontal-layout/vaadin-horizontal-layout/vaadin-button[6]";
				b &= MidsUtile.clickElement(wait, path);
				b &= bulkUploadService.uploadSheet(circle, sheetPath, errorList);
				verifyCounts();
				return b;
			}, "Attribute Verified", "Issue in bulk upload", errorList, USER, map);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void runStep(ProcessName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			List<BulkUploadError> errors, String dep, Map<String, String> attribute) {

		try {

			process.start(name.name());
			boolean result = step.get(); // run the actual process
			process.end(name.name());

			String time = process.getExecutionTime(name.name());

			if (result && (errors == null || errors.isEmpty())) {
				data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, dep, "", ""));
				System.out.println(name.name() + " -> " + time + " execute");
			} else {
				if (errors != null && !errors.isEmpty()) {
					// multiple error rows
					for (BulkUploadError err : errors) {
						String fullMsg = "Row " + err.getRowNumber() + " → " + err.getErrorMessage();

						String attributeName = attribute.get(err.getRowNumber()) != null
								? attribute.get(err.getRowNumber()).split("\\|")[0]
								: "";
						String validation = attribute.get(err.getRowNumber()) != null
								? attribute.get(err.getRowNumber()).split("\\|")[1]
								: "";

						data.add(createResultSheet.createResultData(name.name(), time, true, fullMsg, dep,
								attributeName != null ? attributeName : "", validation != null ? validation : ""));
					}
				} else {
					// single generic error
					data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, dep, "ALL COLUMNS",
							"FAILED TO PROCESS"));
				}
				System.out.println(name.name() + " -> " + time + " FAILED");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			createResultSheet.createResultBulkUpload("BulkUploadTest", data);
		}
	}

}
