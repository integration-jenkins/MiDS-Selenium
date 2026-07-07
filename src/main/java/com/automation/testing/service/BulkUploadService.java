package com.automation.testing.service;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.data.AtUploadResultData;
import com.automation.testing.data.BulkUploadError;
import com.automation.testing.data.BulkUploadWrapper;
import com.automation.testing.enums.BulkUploadPath;
import com.automation.testing.utile.MidsUtile;

@Service
public class BulkUploadService {

	private static final Logger log = LoggerFactory.getLogger(BulkUploadService.class);

	@Autowired
	private WebDriver driver;

	@Autowired
	private WebDriverWait wait;

	@Autowired
	private DriverConfig driverConfig;

	private BulkUploadWrapper bulkUploadWrapper = new BulkUploadWrapper();

	private final String UPLOAD_RESULT_PATH = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/div/p[1]";

	public boolean selectCircle(String circle) {
		boolean b = false;
		try {
			Thread.sleep(500);
			SearchContext circleElement = driver.findElement(By.cssSelector("vaadin-combo-box[tabindex='0']"))
					.getShadowRoot();
			Thread.sleep(500);
			SearchContext element = circleElement.findElement(By.cssSelector("#input")).getShadowRoot();
			Thread.sleep(500);
			WebElement webCircle = element.findElement(By.cssSelector("input[placeholder='Select Circle']"));
			webCircle.sendKeys(circle);
			Thread.sleep(500);
			webCircle.sendKeys(Keys.ENTER);
			b = true;
			log.info(circle + " CIRCLE SELECTED");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	// helps to set the sheet path to upload the sheet.
	public boolean uploadPlan(String path) {
		boolean b = false;
		try {
			// upload dismantle Plan
			SearchContext upload = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-upload")))
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

			log.info("Plan is Uploaded");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	// validate after plan upload
	public boolean validatePlanUpload(List<BulkUploadError> errorList) {
		boolean b = false;
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(UPLOAD_RESULT_PATH)));
			log.info("Upload Path data = " + element.getText());
			if (!element.getText().isEmpty()
					&& (element.getText().contains("Excel doesn't contains records for circle id")
							|| element.getText().contains("Excel doesn't contain records for circle id"))) {
				log.info("Invalid Circle Selection!!");
				errorList.add(new BulkUploadError("2", element.getText()));
				return false;
			}
			Map<String, Integer> recordDetails = getPlanRecordDataMap(element);
			log.info("Upload Details : " + recordDetails);

			// If no error plan is created or updated.
			if (recordDetails.get("Error In Records") == 0
					&& (recordDetails.get("New Records") > 0 || recordDetails.get("Total Updated Records") > 0)) {
				log.info("ALL PLANS IS UPLOAD OR UPDATED, THERE IS NOT ISSUE");
				b = true;
			} else { // plan has invalid values then got error message.
				if (recordDetails.get("Error In Records") > 0 && recordDetails.get("New Records") > 0) {
					b = true;
				} else {
					b = false;
				}
				int errorCount = recordDetails.get("Error In Records");
				int idx = 1;
				while (errorCount > 0) {
					String rowPath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content["
							+ idx + "]";
					String errorMessage = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content["
							+ (idx + 1) + "]";
					try {
						WebElement rowElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowPath)));
						WebElement errorElement = wait
								.until(ExpectedConditions.elementToBeClickable(By.xpath(errorMessage)));

						String i = rowElement.getText();
						String j = errorElement.getText();

						BulkUploadError error = new BulkUploadError(i, j);
						errorList.add(error);

					} catch (NullPointerException e) {
						e.printStackTrace();
						log.info("Issue is Testing code.! some data is NULL");
						log.info("ErrorList = " + errorList);
					} catch (Exception e) {
						e.printStackTrace();
						log.info("Issue in Actual code");
						log.info("ErrorList = " + errorList);
					}
					idx += 2;
					errorCount--;
				}
			}

			// set the bulk upload Meta data.
			bulkUploadWrapper.setErrorList(errorList);
			bulkUploadWrapper.setTotalInvalidPlans(recordDetails.get("Error In Records"));
			bulkUploadWrapper.setTotalNewPlans(recordDetails.get("New Records"));
			bulkUploadWrapper.setTotalUpdatePlans(recordDetails.get("Total Updated Records"));

			log.info("ERROR LIST : " + errorList);
		} catch (NullPointerException e) {
			e.printStackTrace();
			log.info("Issue is Testing code.! some data is NULL");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Issue in Actual code");
		}
		return b;
	}

	// upload the soft AT to fill AT data
	public boolean atSoftATUpload(String circle, String path, List<BulkUploadError> errorList) {
		boolean b = true;
		try {
			// select the circle
			b &= selectCircle(circle);

			// upload the sheet
			b &= uploadPlan(path);

			// verify the result
			b &= validatePlanUpload(errorList);
			log.info("Result = " + errorList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	private boolean selectCircleTab(String circle) {
		try {

			// wait for vaadin-combo-box host
			WebElement comboBoxHost = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
					"body > vaadin-dialog-overlay:nth-child(7) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-horizontal-layout:nth-child(3) > vaadin-combo-box:nth-child(1)")));

			SearchContext shadow0 = comboBoxHost.getShadowRoot();

			// wait for #input inside first shadow root
			WebElement inputHost = wait.until(driver -> shadow0.findElement(By.cssSelector("#input")));

			SearchContext shadow1 = inputHost.getShadowRoot();

			// wait for actual input field
			WebElement input = wait
					.until(driver -> shadow1.findElement(By.cssSelector("input[placeholder='Select Circle']")));

			// interact
			input.click();
			input.sendKeys(circle);
			input.sendKeys(Keys.ENTER);

			log.info(circle + " CIRCLE SELECTED");
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean uploadFileTab(String path) {
		try {
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

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	public boolean uploadFileTabDismantleDelete(String path, List<BulkUploadError> error) {
		try {

			// 1️ Wait for vaadin-upload component
			WebElement uploadHost = wait
					.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("vaadin-upload")));

			// 2️ Enter first shadow root
			SearchContext shadowRoot = uploadHost.getShadowRoot();

			// 3️ Find file input inside shadow DOM
			WebElement fileInput = shadowRoot.findElement(By.cssSelector("input[type='file']"));

			// 4️ Upload file
			fileInput.sendKeys(path);

			// 5️ Wait for uploaded file row
			WebElement uploadFile = shadowRoot.findElement(By.cssSelector("vaadin-upload-file"));

			SearchContext fileShadow = uploadFile.getShadowRoot();

			WebElement startButton = fileShadow.findElement(By.cssSelector("[part='start-button']"));

			((JavascriptExecutor) driver).executeScript("arguments[0].click();", startButton);

			validateDismantleDeleteUpload(error);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private boolean validateDismantleDeleteUpload(List<BulkUploadError> error) {
		try {
			String path = "//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-vertical-layout/vaadin-vertical-layout/div/label";
			WebElement elm = MidsUtile.getElement(wait, path);
			String exeutionResult = elm.getText();
			error.add(new BulkUploadError("2", exeutionResult));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	// FOR SMALL WINDOW UPLOAD TABS
	public boolean uploadSheet(String circle, String path, List<BulkUploadError> errorMap) {
		boolean b = true;
		try {
			// select the circle
			if (!circle.isEmpty()) {
				b &= selectCircleTab(circle);
			}
			// upload the sheet
			b &= uploadFileTab(path);

//			b &= verifyAtUpload(errorMap);

			b &= bulkOptionsVerify(errorMap);

			log.info("Bulk Upload Errors = " + errorMap);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return b;
	}

	public boolean downloadSampleSheet() {
		try {
			String download = "//*[@id=\"overlay\"]/flow-component-renderer/div/a[1]";
			return MidsUtile.clickElement(wait, download);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public boolean uploadSheetWindow(String circle, String path, List<BulkUploadError> errorMap) {
		boolean b = true;
		try {
			// select the circle
			b &= selectCircle(circle);

			// upload the sheet
			b &= uploadPlan(path);

			b &= validatePlanUpload(errorMap);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return b;
	}

	// FOR AT UPLOAD VARIFICATIONS..
	private boolean verifyAtUpload(List<BulkUploadError> errorMap) {
		try {
			WebElement element = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath(BulkUploadPath.AT_UPLOAD_DATA.getPath())));
			log.info("Upload Path data = " + element.getText());
			if (!element.getText().isEmpty()
					&& element.getText().contains("Excel doesn't contains records for circle id")) {
				errorMap.add(new BulkUploadError("1", element.getText()));
				return false;
			}

			Map<String, Integer> recordDetails = getPlanRecordDataMap(element);
			log.info("Data = " + recordDetails);

			Integer phyAtCount = recordDetails.get("PHY AT");
			Integer softAtCount = recordDetails.get("SOFT AT");
			Integer errorCount = recordDetails.get("Error In Records");
			if (errorCount > 0) {
				Integer idx = 0;
				while (idx <= errorCount) {
					String rowNumberpath = "//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-grid/vaadin-grid-cell-content["
							+ (idx + 7) + "]";
					String rowMessagePath = "//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-grid/vaadin-grid-cell-content["
							+ (idx + 8) + "]";

					WebElement element1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowNumberpath)));

					WebElement element2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowMessagePath)));

					String i = element1.getText();
					String j = element2.getText();

					errorMap.add(new BulkUploadError(i, j));

					idx += 2;
				}
			}
			if (errorCount > 0 && phyAtCount == 0 && softAtCount == 0) {
				return false;
			} else if (phyAtCount > 0 || softAtCount > 0) {
				log.info("Phy/Soft At uploaded");
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// FOR VERIFY BULK UPLOAD OPTIONS (UPLOAD ASSIGNMENT, BULK CANCELLATION..etc)
	private boolean bulkOptionsVerify(List<BulkUploadError> errorMap) {
		try {
			WebElement element = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath(BulkUploadPath.AT_UPLOAD_DATA.getPath())));
			log.info("Upload Path data = " + element.getText());
			if (!element.getText().isEmpty()
					&& element.getText().contains("Excel doesn't contains records for circle id")) {
				errorMap.add(new BulkUploadError("1", element.getText()));
				return false;
			}

			Map<String, Integer> recordDetails = getPlanRecordDataMap(element);
			log.info("Data = " + recordDetails);

			Integer errorCount = recordDetails.get("Total Errors") != null ? recordDetails.get("Total Errors")
					: recordDetails.get("Error In Records") != null ? recordDetails.get("Error In Records")
							: recordDetails.get("Total Errors");

			bulkUploadWrapper.setTotalInvalidPlans(errorCount);
			bulkUploadWrapper.setTotalNewPlans(
					recordDetails.get("Total Records") != null ? recordDetails.get("Total Records") : -10);

			WebElement gridScrollable = driver.findElement(By.xpath("//*[@id='overlay']//vaadin-grid"));
			JavascriptExecutor js = (JavascriptExecutor) driver;

			try {
				if (errorCount > 0) {
					Integer idx = 0;
					Integer counter = 0;
					while (counter < errorCount) {
						String rowNumberpath = "//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-grid/vaadin-grid-cell-content["
								+ (idx + 7) + "]";
						String rowMessagePath = "//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-grid/vaadin-grid-cell-content["
								+ (idx + 8) + "]";

						js.executeScript("arguments[0].scrollBy(0,200);", gridScrollable);

						WebElement element1 = wait
								.until(ExpectedConditions.elementToBeClickable(By.xpath(rowNumberpath)));

						WebElement element2 = wait
								.until(ExpectedConditions.elementToBeClickable(By.xpath(rowMessagePath)));

						String i = element1.getText();
						String j = element2.getText();

						errorMap.add(new BulkUploadError(i, j));

						idx += 2;
						counter++;
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				errorMap.add(new BulkUploadError("1", "Issue in Error Messages"));
				log.info("Issue in Error Messages :: total error = " + errorMap.size() + " error = ss" + errorMap);
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Plan not Uploaded");
			errorMap.add(new BulkUploadError("1", "Plan Not Upload, Total records, Total error are not visible"));
			return false;
		}
		return true;
	}

	private static Map<String, Integer> getPlanRecordDataMap(WebElement element) {
		String uploadMessage = element.getText();
		String[] uploadData = uploadMessage.split(",");

		Map<String, Integer> recordDetails = new HashMap<>();
		for (String part : uploadData) {
			String[] record = part.trim().split("=", 2);
			if (record.length == 2) {
				String key = record[0].trim();
				String valuePart = record[1].trim().replaceAll("[^0-9]", "");
				int value = Integer.parseInt(valuePart);
				recordDetails.put(key, value);
			}
		}
		return recordDetails;
	}

	public BulkUploadWrapper getBulkUploadWrapper() {
		return bulkUploadWrapper;
	}

}
