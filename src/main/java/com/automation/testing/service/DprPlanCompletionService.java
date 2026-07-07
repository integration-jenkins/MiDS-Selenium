package com.automation.testing.service;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.enums.Department;
import com.automation.testing.enums.DprPlanTrackPath;
import com.automation.testing.sourcecredentials.UserDetails;
import com.automation.testing.utile.DprPlanCompletionUtile;

@Service
public class DprPlanCompletionService {
	private static final Logger log = LoggerFactory.getLogger(DprPlanCompletionUtile.class);

	private final WebDriverWait wait;
	private final WebDriver driver;
	private final DriverConfig driverConfig;

	public DprPlanCompletionService(WebDriverWait wait, WebDriver driver, DriverConfig driverConfig) {
		this.wait = wait;
		this.driver = driver;
		this.driverConfig = driverConfig;
	}

	public boolean fillBasicDetsils() {
		boolean b = true;
		try {

			Thread.sleep(13000);

			b &= DprPlanCompletionUtile.fillTocoVendor(driver, 16);

			b = b & DprPlanCompletionUtile.fillTocoVendor(driver, 20);
			saveButton();
			driverConfig.waitForIdle(driver);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public boolean srToRafiProcess() {
		boolean b = true;
		try {

			// open SR-RAFI Tab
			clickOnElement(DprPlanTrackPath.SR_RAFI_TAB.getPath());

			// 1. fill SP Details
			b &= DprPlanCompletionUtile.fillSpDetails(driver);

			// 2. Fill SO & SP details
			b &= DprPlanCompletionUtile.fillSOandSPDetails(driver);

			// 3. Fill RAFI Details
			b &= DprPlanCompletionUtile.fillRAFIData(driver);

			// save button
			saveButton();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	// fill the Material Order Fields.
	public boolean materialOrderProcess() {
		boolean b = true;
		try {
			// Open Material Order Tab
			clickOnElement(DprPlanTrackPath.MATERIAL_ORDER_TAB.getPath());
			driverConfig.waitForIdle(driver);

			// Fill the Mo & MD details.
			b &= DprPlanCompletionUtile.fillMOData(driver);

			// save the data
			saveButton();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	// fill INSTALLATION AND COMMISSIONING
	public boolean installAndCommProcess() {
		boolean b = true;
		try {
			// Open Tab
			clickOnElement(DprPlanTrackPath.INSTALL_AND_COMM_TAB.getPath());
			driverConfig.waitForIdle(driver);

			// Fill the Installation, WOL & Hop Deploy details
			b &= DprPlanCompletionUtile.fillInstallAndCommData(driver);

			// save the data
			saveButton();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	// raise Phy & Soft AT
	public boolean phyAndSoftATProcess() {
		boolean b = true;
		try {
			// Open Tab
			clickOnElement(DprPlanTrackPath.ACCEPTANCE_TEST_TAB.getPath());
			driverConfig.waitForIdle(driver);

			// raise the Phy AT
			b = b & raiseAT(0);

			driverConfig.refreshDriver(driver);
			driverConfig.waitForIdle(driver);

			clickOnElement(DprPlanTrackPath.ACCEPTANCE_TEST_TAB.getPath());
			driverConfig.waitForIdle(driver);

			// raise Soft AT
			b &= raiseAT(-1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public boolean softAtRaise() {
		clickOnElement(DprPlanTrackPath.ACCEPTANCE_TEST_TAB.getPath());
		driverConfig.waitForIdle(driver);

		// raise Soft AT
		return raiseAT(-1);
	}

	public boolean backButton() {
		boolean b = clickOnElement(DprPlanTrackPath.CLOSE_BUTTON.getPath()) != null;
		driverConfig.waitForIdle(driver);
		return b;
	}

	private boolean raiseAT(int idx) {
		boolean b = false;
		try {
			WebElement element = DprPlanCompletionUtile.atRaiseProcess(driver, idx);
			clickOnElementByWebElement(element);
			clickOnElement(DprPlanTrackPath.AT_RAISE_BUTTON.getPath());
			driverConfig.waitForIdle(driver);
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	private boolean acceptAT(int idx) {
		boolean b = false;
		try {
			// raise AT
			WebElement element = DprPlanCompletionUtile.atRaiseProcess(driver, idx);
			clickOnElementByWebElement(element);

			clickOnElement(DprPlanTrackPath.AT_ACCEPT_OPEN_BUTTON.getPath());
			clickOnElement(DprPlanTrackPath.AT_ACCEPT_BUTTON.getPath());

			saveButton();
			b = true;
			driverConfig.waitForIdle(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public boolean acceptAtProcess() {
		boolean b = true;
		try {
			// Open Tab
			clickOnElement(DprPlanTrackPath.ACCEPTANCE_TEST_TAB.getPath());
			driverConfig.waitForIdle(driver);

			// accept Phy AT
			b &= acceptAT(0);

			// accept Soft AT
			b &= acceptAT(-1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	// accept or Reject Soft AT
	public boolean acceptOrRejectSoftAT(boolean accept) {
		boolean b = false;
		try {
			// raise AT
			WebElement element = DprPlanCompletionUtile.atRaiseProcess(driver, -1);
			clickOnElementByWebElement(element);

			clickOnElement(DprPlanTrackPath.AT_ACCEPT_OPEN_BUTTON.getPath());
			clickOnElement(
					accept ? DprPlanTrackPath.AT_ACCEPT_BUTTON.getPath() : DprPlanTrackPath.AT_REJECT_BUTTON.getPath());
			if (!accept) { // fill rejection remark
				WebElement rem1 = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath(DprPlanTrackPath.REJECT_SOFT_AT_BOX.getPath())));
				rem1.sendKeys("Configuration Issue");
				Thread.sleep(500);
				rem1.sendKeys(Keys.ENTER);

				WebElement rem2 = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath(DprPlanTrackPath.REJECT_SOFT_AT_REMARK.getPath())));
				rem2.sendKeys("Reject Soft At");
				Thread.sleep(500);
				rem1.sendKeys(Keys.ENTER);

			}

			saveButton();
			b = true;
			driverConfig.waitForIdle(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	// complete the TS process
	public boolean completeTSProcess() {
		boolean b = true;
		try {
			clickOnElement(DprPlanTrackPath.TRAFFIC_SHIFTING_TAB.getPath());

			b &= DprPlanCompletionUtile.fillTSData(driver, "Yes");

			String date = DprPlanCompletionUtile.getDate();
			log.info("TS Date = " + date);
			Thread.sleep(2000);
			b &= DprPlanCompletionUtile.fillTSData(driver, date);

			saveButton();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	private WebElement clickOnElementByWebElement(WebElement element) {
		try {
			// Step 1: Scroll element into view
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

			// Step 2: Wait for clickable
			wait.until(ExpectedConditions.elementToBeClickable(element));

			// Step 3: Try normal click
			try {
				element.click();
				return element;
			} catch (Exception ex) {
				log.info("Normal click failed, trying JS click...");
			}

			// Step 4: Force click via JS
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			return element;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public WebElement clickOnElement(String path) {
		try {
			// Step 1: Wait for element to be present
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(path)));

			// Step 2: Scroll element into view
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

			// Step 3: Wait for clickable
			wait.until(ExpectedConditions.elementToBeClickable(element));

			// Step 4: Try normal click
			try {
				element.click();
				return element;
			} catch (Exception ex) {
				log.info("Normal click failed, trying JS click...");
			}

			// Step 5: Force click via JS
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			return element;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Element not found: " + path);
		}

		return null;
	}

	public boolean searchDprPlanByLinkId(String linkId) {
		boolean b = false;
		try {
			// search by Link Id;
			WebElement element = clickOnElement(DprPlanTrackPath.SEARCH_BY_LINK_ID.getPath());
			if (element != null) {
				element.sendKeys(linkId);
				element.sendKeys(Keys.ENTER);
				driverConfig.waitForIdle(driver);
			} else {
				return b;
			}

			// open the plan
			if (clickOnElement(DprPlanTrackPath.PLAN_TRACKING_BUTTON.getPath()) != null) {
				b = true;
			} else {
				b = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public boolean searchDprPlanByPlanId(String planId) {
		boolean b = false;
		try {
			// search by Link Id;
			WebElement element = clickOnElement(DprPlanTrackPath.SEARCH_BY_PLAN_ID.getPath());
			if (element != null) {
				element.sendKeys(planId);
				element.sendKeys(Keys.ENTER);
				driverConfig.waitForIdle(driver);
			} else {
				return b;
			}

			// open the plan
			if (clickOnElement(DprPlanTrackPath.PLAN_TRACKING_BUTTON.getPath()) != null) {
				b = true;
			} else {
				b = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public boolean selectPlanByPlanId(String planId) {
		boolean b = false;
		try {
			// search by Link Id;
			WebElement element = clickOnElement(DprPlanTrackPath.SEARCH_BY_PLAN_ID.getPath());
			if (element != null) {
				element.sendKeys(planId);
				element.sendKeys(Keys.ENTER);
				driverConfig.waitForIdle(driver);
			} else {
				return b;
			}

			// open the plan
			if (clickOnElement(DprPlanTrackPath.PLAN_SELECT_BUTTON.getPath()) != null) {
				b = true;
			} else {
				b = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	// Assign plan to user, MW Plan Tracking Page.
	public boolean assignToUser(String dep) {
		boolean b = false;
		try {
			b = DprPlanCompletionUtile.assignUser(driver, dep, "input[placeholder='Select Department']", 2);

			b &= DprPlanCompletionUtile.assignUser(driver, UserDetails.getAssignName(dep),
					"input[placeholder='Select User']", 4);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public String getCurrentPlanStatus() {
		WebElement element = clickOnElement(DprPlanTrackPath.STATUS_PATH.getPath());
		return element.getText();
	}

	public boolean verifyCurrectStatus(String status) {
		String currentStatus = getCurrentPlanStatus();
		return currentStatus != null && !currentStatus.isEmpty() && currentStatus.contains(status);
	}

	public void saveButton() {
		clickOnElement(DprPlanTrackPath.SAVE_BUTTON.getPath());
		driverConfig.waitForIdle(driver);
	}

}
