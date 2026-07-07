package com.automation.testing.testcase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.automation.testing.validation.NewDeployBulkValidation;

@Component
public class BulkUploadFunctionalityTest {

	private static final Logger log = LoggerFactory.getLogger(BulkUploadFunctionalityTest.class);

	private final UserService userService;
	private final WebDriverWait wait;
	private final WebDriver driver;
	private final DprPlanCompletionService dprPlanCompletionService;
	private final CreateResultSheet createResultSheet;
	private final BulkUploadService bulkUploadService;
	private final DriverConfig driverConfig;

	public BulkUploadFunctionalityTest(UserService userService, WebDriverWait wait, WebDriver driver,
			DprPlanCompletionService dprPlanCompletionService, CreateResultSheet createResultSheet,
			BulkUploadService bulkUploadService, DriverConfig driverConfig) {
		this.userService = userService;
		this.wait = wait;
		this.driver = driver;
		this.dprPlanCompletionService = dprPlanCompletionService;
		this.createResultSheet = createResultSheet;
		this.bulkUploadService = bulkUploadService;
		this.driverConfig = driverConfig;
	}

	private ProcessCalculationUtile process = new ProcessCalculationUtile();
	private List<ProcessData> data = new ArrayList<>();

	private final String BASE_PATH = "D:\\testingProject\\testing\\src\\main\\resources\\project_file\\new_dep_bulk_sheet\\";

	private Map<ProcessName, String> testNames = Map.of(ProcessName.BULK_ASSIGNMENT, "BULK_ASSIGNMENT",
			ProcessName.BULK_CANCELLATION, "BULK_CANCELLATION", ProcessName.REQUEST_FOR_CANCELLATION,
			"REQUEST_FOR_CANCELLATION", ProcessName.BULK_UPLOAD_FOR_SOFT_UPGRADE, "SOFT_UPGRADE");

	private Map<String, List<ProcessName>> testMap = Map.of(Department.CIRCLE_MW_PLANNER.getName(),
			Arrays.asList(ProcessName.BULK_ASSIGNMENT, ProcessName.REQUEST_FOR_CANCELLATION,
					ProcessName.BULK_CANCELLATION, ProcessName.BULK_UPLOAD_FOR_SOFT_UPGRADE),
			Department.CIRCLE_DEPLOYMENT_TEAM.getName(),
			Arrays.asList(ProcessName.BULK_ASSIGNMENT, ProcessName.REQUEST_FOR_CANCELLATION),
			Department.CIRCLE_DINC_PARTNER.getName(),
			Arrays.asList(ProcessName.BULK_ASSIGNMENT, ProcessName.REQUEST_FOR_CANCELLATION),
			Department.CIRCLE_OPERATION_TEAM.getName(),
			Arrays.asList(ProcessName.BULK_ASSIGNMENT, ProcessName.REQUEST_FOR_CANCELLATION,
					ProcessName.BULK_UPLOAD_FOR_SOFT_UPGRADE),
			Department.CIRCLE_MS_PARTNER.getName(),
			Arrays.asList(ProcessName.BULK_ASSIGNMENT, ProcessName.REQUEST_FOR_CANCELLATION));

	private Map<ProcessName, Map<String, List<String>>> attributeMap = NewDeployBulkValidation.getValidations();

	public void run() {

		try {

			for (Map.Entry<String, List<ProcessName>> i : testMap.entrySet()) {
				String user = i.getKey();
				int idx = 1;

				runStep(ProcessName.USER_LOGIN, () -> userService.userLogin(user), "User able to login",
						"Failed to user login", null, user, null);

				for (ProcessName test : i.getValue()) {
					if (idx == 2 && (user.equals(Department.CIRCLE_MW_PLANNER.getName())
							|| user.equals(Department.CIRCLE_MS_PARTNER.getName()))) {
						idx = 3;
					}
					String path = BASE_PATH + testNames.get(test) + ".xlsx";
					String json = BASE_PATH + testNames.get(test) + ".json";

					verifyUpload(test, idx, user, path, attributeMap.get(test));
					idx++;

					driverConfig.refreshDriver(driver);
					driverConfig.waitForIdle(driver);
				}

				userService.logOut(driver, wait);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			createResultSheet.createResultBulkUpload("BulkUploadTest", data);
		}

	}

	private void verifyUpload(ProcessName name, int idx, String user, String sheetPath, Map<String, List<String>> map) {
		List<BulkUploadError> errorList = new ArrayList<>();
		try {
			runStep(name, () -> {
				String circle = sheetPath.contains("REQUEST_FOR_CANCELLATION_1") ? "" : getCircleCode(name);
				boolean b = true;
				b &= MidsUtile.clickElement(wait, BulkUploadPath.MW_PLAN_TRACKING_BULK_UPLOAD_OPTIONS.getPath());
				String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[4]/vaadin-horizontal-layout/vaadin-horizontal-layout/vaadin-button["
						+ idx + "]";
				b &= MidsUtile.clickElement(wait, path);
				b &= bulkUploadService.uploadSheet(circle, sheetPath, errorList);
				return b;
			}, "Attribute Verified", "Issue in" + name + "bulk upload", errorList, user, map);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private static String getCircleCode(ProcessName processName) {
		if (processName == null) {
			return null;
		}

		switch (processName) {
		case BULK_ASSIGNMENT:
		case BULK_CANCELLATION:
			return "JK";

		case REQUEST_FOR_CANCELLATION:
			return "AP";

		case BULK_UPLOAD_FOR_SOFT_UPGRADE:
			return "RAJ";

		default:
			return null;
		}
	}

	private void runStep(ProcessName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			List<BulkUploadError> errors, String dep, Map<String, List<String>> attribute) {

		try {

			process.start(name.name());
			boolean result = step.get(); // run the actual process
			process.end(name.name());

			String time = process.getExecutionTime(name.name());

			if (result && (errors == null || errors.isEmpty())) {
				data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, dep, "", ""));
				log.info(name.name() + " -> " + time + " execute");
			} else {
				if (errors != null && !errors.isEmpty()) {
					// multiple error rows
					for (BulkUploadError err : errors) {
						String fullMsg = "Row " + err.getRowNumber() + " → " + err.getErrorMessage();
						List<String> list = attribute.get(err.getRowNumber());
						data.add(createResultSheet.createResultData(name.name(), time, true, fullMsg, dep,
								list != null ? list.get(0) : "", list != null ? list.get(1) : ""));
					}
				} else {
					// single generic error
					data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, dep, "ALL COLUMNS",
							"FAILED TO PROCESS"));
				}
				log.info(name.name() + " -> " + time + " FAILED");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			createResultSheet.createResultBulkUpload("BulkUploadTest", data);
		}
	}

}
