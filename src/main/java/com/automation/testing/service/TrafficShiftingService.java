package com.automation.testing.service;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.enums.TrafficShiftingPath;
import com.automation.testing.utile.MidsUtile;
import com.automation.testing.utile.TsNavigateUtile;

@Service
public class TrafficShiftingService {

	private static final Logger log = LoggerFactory.getLogger(TrafficShiftingService.class);

	private final WebDriverWait wait;
	private final WebDriver driver;
	private final DriverConfig driverConfig;

	public TrafficShiftingService(WebDriverWait wait, WebDriver driver, DriverConfig driverConfig) {
		this.wait = wait;
		this.driver = driver;
		this.driverConfig = driverConfig;
	}

	// NAVIGATE TO TRAFFIC SHIFTING TRACK VIEW PAGE
	public boolean openTsTrackPage(String user) {
		boolean b = true;
		b &= TsNavigateUtile.navigateToTsPage(driver, wait, user);
		b &= TsNavigateUtile.navigateToReport(driver, wait, user, TrafficShiftingPath.TRAFFIC_TRACK_VIEW.getPath());
		return b;
	}

	public boolean openTsUpload(String user) {
		boolean b = true;
		b &= TsNavigateUtile.navigateToTsPage(driver, wait, user);
		b &= TsNavigateUtile.navigateToReport(driver, wait, user, TrafficShiftingPath.TS_UPLOAD.getPath());
		return b;
	}

	// SEARCH PLAN BY SIDE ID & OPEN IT
	public boolean openTsPlan(String id) {
		boolean b = true;
		try {
			// FILTER THE PLAN
			WebElement element = MidsUtile.getElement(wait, TrafficShiftingPath.TRACK_VIEW_SEARCH_PATH.getPath());
			if (element != null) {
				element.sendKeys(id);
				Thread.sleep(300);
				element.sendKeys(Keys.ENTER);
			} else
				return false;

			Integer i = MidsUtile.getReportCount(wait, TrafficShiftingPath.TRAFIC_TRACK_VIEW_COUNT_PATH.getPath());
			if (i != null && i != -1 && i > 0) {
				b &= MidsUtile.clickElement(wait, TrafficShiftingPath.OPEN_PLAN_PLAN.getPath());
				log.info(id + ": Plan Found.");
			} else {
				log.info(id + ": Plan Not Found");
				b = false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return b;
	}

	// FILL TS STATUS (YES, NO OR HOLD)
	public boolean fillTsStatus(String status) {
		boolean b = true;
		try {
			String elementPath = "#tsDoneStatus";
			WebElement element = MidsUtile.findShadowElement(driver, elementPath, "#input", "input[role='combobox']");
			element.clear();
			element.sendKeys(status);
			Thread.sleep(300);
			element.sendKeys(Keys.ENTER);

			// SAVE
			if (status.equals("Yes")) {
				b &= saveButton();
				driverConfig.waitForIdle(driver);
				backButton();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	// FILL THE HOLDS DETAILS FOR PLAN HOLD
	public boolean fillHoldData(String side) {
		try {
			WebElement element = MidsUtile.findShadowElement(driver, "#tsHoldCategory", "#input",
					"input[role='combobox']");
			element.sendKeys("DESCOPE");
			Thread.sleep(300);
			element.sendKeys(Keys.ENTER);

			WebElement element1 = MidsUtile.findShadowElement(driver, "#tsHoldCategoryRemarks", "input[part='value']");
			element1.sendKeys("sample");
			Thread.sleep(300);
			element1.sendKeys(Keys.ENTER);

			String remarkPath = "body > vaadin-dialog-overlay:nth-child(7) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(1) > vaadin-text-field:nth-child(8)";
			WebElement element2 = MidsUtile.findShadowElement(driver, remarkPath, "input[part='value']");
			element2.sendKeys("sample");
			Thread.sleep(300);
			element2.sendKeys(Keys.ENTER);

			WebElement element3 = MidsUtile.findShadowElement(driver, "#tsAccessIssueSite", "#input",
					"input[role='combobox']");
			element3.sendKeys(side);
			Thread.sleep(300);
			element3.sendKeys(Keys.ENTER);

			// save data;
			String savePath = "//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-button";
			MidsUtile.clickElement(wait, savePath);
			Thread.sleep(500);
			backButton();

			return true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	// RESOLVE THE HOLDED PLAN
	public boolean retriveHold() {
		boolean b = true;
		// CLICK ON RESOLVE BUTTON
		b &= MidsUtile.clickElement(wait, TrafficShiftingPath.RESOLVE_HOLD.getPath());
		driverConfig.waitForIdle(driver);

		try {
			// FILL THE REQUIRED FIELDS AND SAVE IT
			WebElement elm1 = MidsUtile.findShadowElement(driver, "#tsResolutionRemarks", "input[part='value']");
			elm1.sendKeys("resolve");
			elm1.sendKeys(Keys.ENTER);

			WebElement elm2 = MidsUtile.findShadowElement(driver, "#tsHoldCloseDate", "#input", "input[part='value']");
			elm2.sendKeys(MidsUtile.getDate());
			Thread.sleep(300);
			elm2.sendKeys(Keys.ENTER);

			String savePath = "//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-form-layout/vaadin-button";
			b &= MidsUtile.clickElement(wait, savePath);

			// AFTER PROCESSING BACK THE PAGE FOR NEXT PLAN
			backButton();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			b = false; // IF PROCESS INTRUPET THE RETUNR FALSE
		}

		return b;
	}

	// CANCEL THE PLAN
	public boolean cancelPlan() {
		boolean b = true;
		try {
			String cancelButtonpath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[3]/vaadin-vertical-layout/vaadin-horizontal-layout/div[2]/button";
			b &= MidsUtile.clickElement(wait, cancelButtonpath);

			String path = "//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-vertical-layout/vaadin-radio-group/vaadin-radio-button[1]";
			b &= MidsUtile.clickElement(wait, path);

			path = "//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-button[2]";
			b &= MidsUtile.clickElement(wait, path);

			// FILL THE REMARK
			String remPath = "//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-vertical-layout/vaadin-text-area";
			WebElement elm = MidsUtile.getElement(wait, remPath);
			elm.sendKeys("cancel the plan");
			Thread.sleep(300);

			String yesPath = "//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-button[2]";
			b &= MidsUtile.clickElement(wait, yesPath);

			Thread.sleep(5000);
			backButton();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			b = false;
		}

		return b;
	}

	// MAIN PAGE SAVE BUTTON THE TS DATA
	private boolean saveButton() {
		String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/div[2]";
		return MidsUtile.clickElement(wait, path);
	}

	// MAIN PAGE BACK BUTTON TO MOVE PREVIOUS PAGE
	private boolean backButton() {
		String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/div[1]";
		driverConfig.waitForIdle(driver);
		boolean b = MidsUtile.forceClick(driver, wait, path);
		driverConfig.waitForIdle(driver);
		return b;
	}

	public String getCurrentStatus() {
		try {
			String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[3]/vaadin-vertical-layout/div/span[2]";
			WebElement elm = MidsUtile.getElement(wait, path);
			return elm.getText();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
