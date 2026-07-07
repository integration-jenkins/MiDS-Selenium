package com.automation.testing.testcase;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.data.BulkUploadError;
import com.automation.testing.data.ProcessData;
import com.automation.testing.enums.Department;
import com.automation.testing.enums.DprPlanTrackPath;
import com.automation.testing.enums.ProcessName;
import com.automation.testing.service.BulkUploadService;
import com.automation.testing.service.CreateResultSheet;
import com.automation.testing.service.DprPlanCompletionService;
import com.automation.testing.service.UserService;
import com.automation.testing.utile.MidsUtile;
import com.automation.testing.utile.ProcessCalculationUtile;

@Component
public class PhyAtTest {

	private final WebDriver driver;
	private final WebDriverWait wait;
	private final CreateResultSheet createResultSheet;
	private final UserService userService;
	private final DprPlanCompletionService dprPlanCompletionService;
	private final DriverConfig driverConfig;

	public PhyAtTest(WebDriver driver, WebDriverWait wait, CreateResultSheet createResultSheet, UserService userService,
			DprPlanCompletionService dprPlanCompletionService, DriverConfig driverConfig) {
		super();
		this.driver = driver;
		this.wait = wait;
		this.createResultSheet = createResultSheet;
		this.userService = userService;
		this.dprPlanCompletionService = dprPlanCompletionService;
		this.driverConfig = driverConfig;
	}

	private final String user = Department.CIRCLE_DINC_PARTNER.getName();
	private String[] planId = { "MW-N-ROB-23042025-5659", "MW-N-HP-13062023-8222" };
	private final String FILED_SELECTOR = "input[part='value']";
	private final String CHECK_BOX_SELECTOR = "input[role='combobox']";
	private final String INPUT_SELECTOR = "#input";
	private final String FILE_PATH = "D:\\testingProject\\testing\\src\\main\\resources\\project_file\\PHY_AT.xlsx";
	private final String VALID_FILE_PATH = "D:\\testingProject\\testing\\src\\main\\resources\\project_file\\PHY_AT_VALID.xlsx";

	private ProcessCalculationUtile process = new ProcessCalculationUtile();
	private List<ProcessData> data = new ArrayList<>();

	public void run() {

		try {
			runStep(ProcessName.USER_LOGIN, () -> userService.userLogin(user), "User able To Login", "Failed to login",
					user);

			runStep(ProcessName.OPEN_DPR_PLAN, () -> {
				boolean b = dprPlanCompletionService.searchDprPlanByPlanId(planId[0]);
				b &= MidsUtile.clickElement(wait, DprPlanTrackPath.ACCEPTANCE_TEST_TAB.getPath());
				driverConfig.waitForIdle(driver);
				return b;
			}, planId[0] + " Plan Found", planId[0] + " Plan not found", user);

			verifyManualPhyAt();
			dprPlanCompletionService.saveButton();
			driverConfig.refreshDriver(driver);
			MidsUtile.clickElement(wait, DprPlanTrackPath.ACCEPTANCE_TEST_TAB.getPath());
			verifyFieldsValues();
			dprPlanCompletionService.backButton();

			runStep(ProcessName.OPEN_DPR_PLAN, () -> {
				boolean b = dprPlanCompletionService.searchDprPlanByPlanId(planId[1]);
				b &= MidsUtile.clickElement(wait, DprPlanTrackPath.ACCEPTANCE_TEST_TAB.getPath());
				driverConfig.waitForIdle(driver);
				return b;
			}, planId[1] + " Plan Found", planId[1] + " Plan not found", user);

			verifBulkUpload();
			driverConfig.refreshDriver(driver);
			MidsUtile.clickElement(wait, DprPlanTrackPath.ACCEPTANCE_TEST_TAB.getPath());

			runStep(ProcessName.BULK_UPLOAD_WITH_VALID_DATA, () -> {
				String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-horizontal-layout[3]/vaadin-button[1]";
				MidsUtile.clickElement(wait, path);
				return uploadPlan(VALID_FILE_PATH);
			}, "PHY AT bulk upload is Working", "Phy At Bulk NoT Working [FAILED]", user);

			verifyFieldsValues();

			userService.logOut(driver, wait);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			createResultSheet.createResult("PHY_AT_TEST", data);
		}
	}

	enum FieldType {
		TEXT, CHECK_BOX
	}

	public void verifBulkUpload() {

		String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-horizontal-layout[3]/vaadin-button[1]";
		MidsUtile.clickElement(wait, path);
		uploadPlan(FILE_PATH);

		for (int i = 1; i <= 8; i += 2) {
			try {
				String idxPath = "//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-grid/vaadin-grid-cell-content["
						+ i + "]";
				String validationPath = "//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-grid/vaadin-grid-cell-content["
						+ (i + 1) + "]";

				String idxVal = MidsUtile.getElement(wait, idxPath).getText();
				String val = MidsUtile.getElement(wait, validationPath).getText();

				data.add(createResultSheet.createResultData(ProcessName.VERIFY_BULK_UPLOAD_INVALID_ATTRIBUTES.name(),
						"10 sec", true, idxVal + " : error[" + val + "]", user));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

	}

	private boolean uploadPlan(String path) {
		boolean b = false;
		try {
			// upload dismantle Plan
			SearchContext upload = wait
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
							"//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout/vaadin-upload")))
					.getShadowRoot();
			WebElement file = upload.findElement(By.cssSelector("input[type='file']"));
			file.sendKeys(path);
			SearchContext s0 = upload.findElement(By.cssSelector("vaadin-upload-file")).getShadowRoot();
			WebElement startButton = s0.findElement(By.cssSelector("div[part='start-button']"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", startButton);
			WebDriverWait waitt = new WebDriverWait(driver, Duration.ofSeconds(3));
			waitt.until(ExpectedConditions.elementToBeClickable(startButton));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", startButton);

			b = true;
			driverConfig.waitForIdle(driver);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private void verifyManualPhyAt() {
		runStep(ProcessName.PHY_AT_VALUE_FILLING, () -> {
			boolean b = true;
			MidsUtile.clickElement(wait, fieldPath(2));
			b &= verifySiteDetails();

			MidsUtile.clickElement(wait, fieldPath(3));
			b &= fillingValues(
					"body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > "
							+ "div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > "
							+ "vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(5) > "
							+ "vaadin-accordion-panel:nth-child(3) > div:nth-child(1) > "
							+ "vaadin-form-layout:nth-child(1) > ",
					4, 304, FieldType.TEXT, createGroupException(68, 76, 84, 132, 156, 164, 244, 252, 260, 276, 300),
					Map.of());

			MidsUtile.clickElement(wait, fieldPath(4));
			b &= fillingValues(
					"body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(5) > vaadin-accordion-panel:nth-child(4) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > ",
					4, 168, FieldType.TEXT, createGroupException(20, 28, 36, 44, 68, 76, 84, 92, 116, 140, 148, 156),
					createGroupRule("Ok", 148, 156));

			MidsUtile.clickElement(wait, fieldPath(5));
			b &= fillingValues(
					"body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(5) > vaadin-accordion-panel:nth-child(5) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > ",
					4, 336, FieldType.CHECK_BOX, createGroupException(308, 332), Map.of());

			MidsUtile.clickElement(wait, fieldPath(6));
			b &= fillingValues(
					"body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(5) > vaadin-accordion-panel:nth-child(6) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > ",
					3, 103, FieldType.TEXT, createGroupException(3), Map.of());
			try {
				for (Integer idx : Arrays.asList(107, 115, 118, 123, 126, 131, 134, 139)) {
					String path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(5) > vaadin-accordion-panel:nth-child(6) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > vaadin-text-field:nth-child("
							+ idx + ")";
					WebElement element = MidsUtile.findShadowElement(driver, path, FILED_SELECTOR);

					setValue(element, "SAMPLE");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return b;
		}, "All Fiedls value filled", "There some issue to get Fields", user);
	}

	private void verifyFieldsValues() {
		runStep(ProcessName.AT_ACCEPT, () -> {
			boolean b = true;

			b &= MidsUtile.clickElement(wait, fieldPath(2));
			verifySiteDetails();

			MidsUtile.clickElement(wait, fieldPath(3));
			b &= verifyFieldValues(
					"body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > "
							+ "div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > "
							+ "vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(5) > "
							+ "vaadin-accordion-panel:nth-child(3) > div:nth-child(1) > "
							+ "vaadin-form-layout:nth-child(1) > ",
					4, 304, FieldType.TEXT, createGroupException(68, 76, 84, 132, 156, 164, 244, 252, 260, 276, 300),
					Map.of());

			MidsUtile.clickElement(wait, fieldPath(4));
			b &= verifyFieldValues(
					"body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(5) > vaadin-accordion-panel:nth-child(4) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > ",
					4, 168, FieldType.TEXT, createGroupException(20, 28, 36, 44, 68, 76, 84, 92, 116, 140, 148, 156),
					createGroupRule("Ok", 148, 156));

			MidsUtile.clickElement(wait, fieldPath(5));
			b &= verifyFieldValues(
					"body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(5) > vaadin-accordion-panel:nth-child(5) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > ",
					4, 336, FieldType.CHECK_BOX, createGroupException(308, 332), Map.of());

			MidsUtile.clickElement(wait, fieldPath(6));
			b &= verifyFieldValues(
					"body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(5) > vaadin-accordion-panel:nth-child(6) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > ",
					3, 103, FieldType.TEXT, createGroupException(3), Map.of());
			for (Integer idx : Arrays.asList(107, 115, 118, 123, 126, 131, 134, 139)) {
				String path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(5) > vaadin-accordion-panel:nth-child(6) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > vaadin-text-field:nth-child("
						+ idx + ")";
				WebElement element = MidsUtile.findShadowElement(driver, path, FILED_SELECTOR);
				if (getValueUsingJS(element).isEmpty()) {
					System.out.println("Field Empty.." + idx);
					b &= false;
				}
			}

			return b;
		}, "ALL Fields is Saved", "There some issue in fiedls, All Fields not save", user);

	}

	private Map<Integer, String> createGroupRule(String value, Integer... startIdx) {

		Map<Integer, String> map = new HashMap<>();

		for (int j = 0; j < startIdx.length; j++) {
			for (int i = 0; i < 4; i++) {
				map.put(startIdx[j] + i, value);
			}
		}

		return map;
	}

	private Set<Integer> createGroupException(Integer... starts) {

		Set<Integer> indexes = new HashSet<>();

		for (Integer start : starts) {

			for (int i = 0; i < 4; i++) {
				indexes.add(start + i);
			}
		}

		return indexes;
	}

	private boolean verifyFieldValues(String BASE_PATH, int start, int end, FieldType defaultType,
			Set<Integer> exceptionIndexes, Map<Integer, String> customValues) {

		int i = 1;
		boolean filled = true;

		while (start <= end) {

			try {

				boolean isCheckbox;

				if (defaultType == FieldType.TEXT) {

					isCheckbox = exceptionIndexes.contains(start);

				} else {
					isCheckbox = !exceptionIndexes.contains(start) && !(start % 8 == 0);
				}

				String component = !isCheckbox ? "vaadin-text-field:nth-child(" : "vaadin-combo-box:nth-child(";

				String fullPath = BASE_PATH + component + start + ")";

				WebElement element;

				if (!isCheckbox) {

					for (int step = 0; step <= 3; step++) {
						try {
							element = MidsUtile.findShadowElement(driver, fullPath, FILED_SELECTOR);

							if (getValueUsingJS(element).isEmpty()) {
								System.out.println(start + " is Empty");
								return false;
							}
							break;
						} catch (Exception e) {
							// TODO: handle exception
							System.out.print("Try.." + step);
						}
					}

				} else {

					element = MidsUtile.findShadowElement(driver, fullPath, INPUT_SELECTOR, CHECK_BOX_SELECTOR);

					for (int step = 0; step <= 3; step++) {
						try {
							element = MidsUtile.findShadowElement(driver, fullPath, FILED_SELECTOR);

							if (getValueUsingJS(element).isEmpty()) {
								System.out.println(start + " is Empty");
								return false;
							}
							break;
						} catch (Exception e) {
							// TODO: handle exception
							System.out.print("Try.." + step);
						}
					}

				}

			} catch (Exception e) {

				System.out.println("Failed index : " + start);
				e.printStackTrace();

				filled = false;
			}

			start++;
			i++;

			if (i == 6) {
				start += 3;
				i = 1;
			}
		}

		return filled;
	}

	private boolean fillingValues(String BASE_PATH, int start, int end, FieldType defaultType,
			Set<Integer> exceptionIndexes, Map<Integer, String> customValues) {

		int i = 1;
		boolean filled = true;

		while (start <= end) {

			try {

				boolean isCheckbox;

				if (defaultType == FieldType.TEXT) {

					isCheckbox = exceptionIndexes.contains(start);

				} else {
					isCheckbox = !exceptionIndexes.contains(start) && !(start % 8 == 0);
				}

				String component = !isCheckbox ? "vaadin-text-field:nth-child(" : "vaadin-combo-box:nth-child(";

				String fullPath = BASE_PATH + component + start + ")";

				String value = customValues.getOrDefault(start, isCheckbox ? "Yes" : "Sample");

				WebElement element;

				if (!isCheckbox) {

					element = MidsUtile.findShadowElement(driver, fullPath, FILED_SELECTOR);

					setValue(element, value);

				} else {

					element = MidsUtile.findShadowElement(driver, fullPath, INPUT_SELECTOR, CHECK_BOX_SELECTOR);

					element.sendKeys(value);
					element.sendKeys(Keys.ENTER);
				}

			} catch (Exception e) {

				System.out.println("Failed index : " + start);
				e.printStackTrace();

				filled = false;
			}

			start++;
			i++;

			if (i == 6) {
				start += 3;
				i = 1;
			}
		}

		return filled;
	}

	private void setValue(WebElement element, String value) {

		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("arguments[0].focus();" + "arguments[0].value=arguments[1];"
				+ "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));"
				+ "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", element, value);
	}

	private String getValueUsingJS(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (String) js.executeScript("return arguments[0].value;", element);
	}

	private String fieldPath(int i) {
		return "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-accordion[1]/vaadin-accordion-panel["
				+ i + "]/span";
	}

	private boolean verifySiteDetails() {

		int idx = 3;
		int i = 1;

		while (idx <= 143) {
			try {
				String path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(5) > vaadin-accordion-panel:nth-child(2) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > vaadin-text-field:nth-child("
						+ idx + ")";
				WebElement elm = MidsUtile.findShadowElement(driver, path, FILED_SELECTOR);
				setValue(elm, "Sample");
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Filled in " + idx);
				return false;
			}

			idx++;
			i++;
			if (i == 6) {
				idx += 3;
				i = 1;
			}
		}
		return true;
	}

	private boolean runStep(ProcessName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			String department) {

		process.start(name.name());
		boolean result = step.get(); // run the actual process
		driverConfig.waitForIdle(driver);
		process.end(name.name());

		String time = process.getExecutionTime(name.name());

		if (result) {
			data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, department));
			System.out.println(name.name() + " -> " + time + " execute");
			return true; // continue next step
		} else {
			data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, department));
			System.out.println(name.name() + " -> " + time + " FAILED");
			return false;
		}
	}

}
