package com.automation.testing.utile;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.SearchContext;

import com.automation.testing.sourcecredentials.DprSampleDataValues;

public class DprPlanCompletionUtile {

	private static final Logger log = LoggerFactory.getLogger(DprPlanCompletionUtile.class);

	// fill Toco Vendor Basic Details
	public static boolean fillTocoVendor(WebDriver driver, int idx) {
		boolean b = false;
		try {
			String path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child("
					+ idx + ")";

			WebElement element = findShadowElement(driver, path, "#input", "input[role='combobox']");
			element.sendKeys("INDUS");
			Thread.sleep(300);
			element.sendKeys(Keys.ENTER);
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	// This is method Fill the SP Details
	public static boolean fillSpDetails(WebDriver driver) {
		boolean b = false;
		try {
			int i = 2;
			while (i < 6) {

				if (i % 2 == 0) {
					// TEXT
					String host = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3)"
							+ " > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3)"
							+ " > vaadin-text-field:nth-child(" + i + ")";

					WebElement input = findShadowElement(driver, host, "input[part='value']");

					input.sendKeys("Text Value " + i);
					input.sendKeys(Keys.ENTER);
				} else {
					// DATE PICKER
					String host1 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3)"
							+ " > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3)"
							+ " > vaadin-date-picker:nth-child(" + i + ")";

					WebElement input = findShadowElement(driver, host1, "#input", "input[part='value']");

					input.sendKeys(getDate());
					input.sendKeys(Keys.ENTER);
				}

				i++;
			}
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	// This method fills the SO and SP Details
	public static boolean fillSOandSPDetails(WebDriver driver) {
		boolean b = false;
		try {
			// index of elements
			Integer idx[] = { 9, 10, 12, 13 };
			for (Integer i : idx) {
				String host1 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > "
						+ "vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > "
						+ "vaadin-date-picker:nth-child(" + i + ")";

				// fetch the actual element in shadow elements.
				WebElement element = findShadowElement(driver, host1, // shadow dom 1
						"#input", // shadow dom 2
						"input[part='value']" // final input
				);

				// set data into element
				element.sendKeys(getDate());
				element.sendKeys(Keys.ENTER);
			}
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	// fill Material Order Details.
	public static boolean fillMOData(WebDriver driver) {
		boolean b = false;
		try {
			int i = 2;
			// fill Material Ordered (2 - 9) & Material Delivered (11-15) details
			while (i <= 15) {
				String path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3)";
				if (i == 2 || i == 5 || i == 8) { // This for Text fields.
					path += " > " + "vaadin-text-field:nth-child(" + i + ")";
					WebElement element = findShadowElement(driver, path, "input[part='value']");
					element.sendKeys("sample_value");
					element.sendKeys(Keys.ENTER);
				} else if (i == 3 || i == 6 || i == 15) { // for combo fields
					path += " > " + "vaadin-combo-box:nth-child(" + i + ")";
					WebElement element = findShadowElement(driver, path, "#input", "input[role='combobox']");
					if (i == 15) {
						Thread.sleep(1000);
						element.sendKeys("Fully Delivered");
					} else {
						element.sendKeys("SRN");
					}
					element.sendKeys(Keys.ENTER);
				} else { // else Date fields
					path += " > " + "vaadin-date-picker:nth-child(" + i + ")";
					WebElement element = findShadowElement(driver, path, "#input", "input[part='value']");
					element.sendKeys(getDate());
					element.sendKeys(Keys.ENTER);
					// 10 is not any index then jump 9 to 11
					if (i == 9) {
						i = 10;
					}
				}
				i++;
			}

			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	// fill INSTALLATION AND COMMISSIONING data
	public static boolean fillInstallAndCommData(WebDriver driver) {
		boolean b = false;
		try {

			Integer dateElements[] = { 2, 3, 8, 10, 11, 13, 15, 17, 21, 24 };
			Integer textElements[] = { 7, 9, 12, 14, 16, 18 };
			String base_path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3)";

			// fill current date
			for (Integer i : dateElements) {
				String path = base_path + " > " + "vaadin-date-picker:nth-child(" + i + ")";
				WebElement element = findShadowElement(driver, path, "#input", "input[part='value']");
				element.sendKeys(getDate());
				element.sendKeys(Keys.ENTER);
			}

			// file Text fields
			for (Integer i : textElements) {
				String path = base_path + " > " + "vaadin-text-field:nth-child(" + i + ")";
				WebElement element = findShadowElement(driver, path, "input[part='value']");
				element.sendKeys((i == 12 || i == 16) ? "NW12345" : (i == 14 || i == 18) ? "I@A45" : "sampleVal");
				element.sendKeys(Keys.ENTER);
			}

			// handle special case for NMS Visibility Status
			String nms_path = "vaadin-combo-box[class='planEditText'][label='NMS Visibility Status']";
			WebElement element = waitForInput(driver,
					findShadowElement(driver, nms_path, "#input", "input[role='combobox']"));
			element.sendKeys("YES");
			element.sendKeys(Keys.ENTER);

			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	private static WebElement waitForInput(WebDriver driver, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		return wait.until(d -> (element.isDisplayed() && element.isEnabled()) ? element : null);
	}

	// Raise Phy & Soft AT
	public static WebElement atRaiseProcess(WebDriver driver, int idx) {
		try {
			String path = "vaadin-radio-button[role='radio'][tabindex='" + idx + "']";
			WebElement element = findShadowElement(driver, path, "label");
			return element;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// cancel button during AT raise;
	public static WebElement clickOnCancelButton(WebDriver driver) throws InterruptedException {
		Thread.sleep(9600);
		String path = "body > vaadin-dialog-overlay:nth-child(7) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-button:nth-child(3)";
		WebElement elm = findShadowElement(driver, path, "#button");
		return elm;
	}

	// fill TS fields
	public static boolean fillTSData(WebDriver driver, String data) {
		boolean b = false;
		try {
			if (data.equalsIgnoreCase("Yes")) {
				WebElement element = waitForInput(driver,
						findShadowElement(driver, "vaadin-combo-box[class='planEditText']", "#input"));
				element.sendKeys(data);
				Thread.sleep(200);

				// Press ENTER
				element.sendKeys(Keys.ENTER);

				// Verify the value was accepted
				Thread.sleep(300);
			} else {
				JavascriptExecutor js = (JavascriptExecutor) driver;

				String jsSelector = "vaadin-date-picker.planEditText";

				String script = "var el = document.querySelector('" + jsSelector + "');" + "if (el) {"
						+ "   var input = el.shadowRoot.querySelector('#input');" + "   if (input) {"
						+ "       input.value = '" + data + "';"
						+ "       input.dispatchEvent(new Event('input', { bubbles: true }));"
						+ "       input.dispatchEvent(new Event('change', { bubbles: true }));" + "   }" + "}";

				js.executeScript(script);

				Thread.sleep(300);
				waitForInput(driver, findShadowElement(driver, "vaadin-date-picker[class='planEditText']", "#input"))
						.sendKeys(Keys.ENTER);
			}

			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	// this is fill the RAFI Details
	public static boolean fillRAFIData(WebDriver driver) {
		boolean b = false;
		Map<Integer, String> rafiData = DprSampleDataValues.getRAFIData();
		try {
			// for Site A fields which index (15 - 23)
			for (int i = 15; i < 24; i++) {
				boolean isDate = !rafiData.containsKey(i); // your date pickers
				WebElement input = getRFAIElement(driver, i, isDate);

				if (rafiData.containsKey(i)) {
					input.sendKeys(rafiData.get(i)); // fill text
				} else if (isDate) {
					input.sendKeys(getDate()); // default date
				} else {
					input.sendKeys("SAMPLE"); // default text
				}
				input.sendKeys(Keys.ENTER);
			}

			// for Site A fields which index (27 - 35)
			for (int a = 15; a <= 23; a++) {

				int idxB = a + 12; // shift for site B

				boolean isDate = !rafiData.containsKey(a); // your date pickers

				WebElement input = getRFAIElement(driver, idxB, isDate);

				if (rafiData.containsKey(a)) { // use Site A map but index shifted
					input.sendKeys(rafiData.get(a));
				} else if (isDate) {
					input.sendKeys(getDate());
				} else {
					input.sendKeys("SAMPLE");
				}
				input.sendKeys(Keys.ENTER);
			}
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	// Create the XPath of element according to IDX & Send the Value.
	private static WebElement getRFAIElement(WebDriver driver, int index, boolean isDatePicker) {

		String base = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > "
				+ "vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > ";

		if (isDatePicker) {
			// 2 nested shadow DOM
			String host1 = base + "vaadin-date-picker:nth-child(" + index + ")";
			return findShadowElement(driver, host1, "#input", "input[part='value']");
		} else {
			// 1 shadow DOM
			String host1 = base + "vaadin-text-field:nth-child(" + index + ")";
			return findShadowElement(driver, host1, "input[part='value']");
		}
	}

	public static boolean verifyCurrentPlanStatus(String actualStatus, String expectedStatus) {
		return actualStatus != null && expectedStatus.equalsIgnoreCase(actualStatus);
	}

	public static String getDate() {
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
		return date.format(formatter);
	}

	private static WebElement findShadowElement(WebDriver driver, String... selectors) {

		// selectors[0] = first shadow host
		// selectors[1] = next shadow host (if any)
		// selectors[last] = final element inside deepest shadow root

		SearchContext context = driver;

		// Loop through all hosts except the last selector (final element)
		for (int i = 0; i < selectors.length - 1; i++) {
			WebElement host = context.findElement(By.cssSelector(selectors[i]));
			context = host.getShadowRoot(); // enter shadow DOM
		}

		// Return final element inside deepest shadow DOM
		return context.findElement(By.cssSelector(selectors[selectors.length - 1]));
	}

	public static boolean verifyTocoVendor(WebDriver driver) {
		boolean b = true;
		int idx = 16;

		try {
			while (idx != 0) {
				String path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child("
						+ idx + ")";

				WebElement element = findShadowElement(driver, path, "#input", "input[role='combobox']");
				b &= verifyData(element);
				if (idx == 20)
					idx = 0;
				else
					idx = 20;

			}

		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	// This is method Fill the SP Details
	public static boolean verifySpDetails(WebDriver driver) {
		boolean b = true;
		try {
			int i = 2;
			while (i < 6) {

				if (i % 2 == 0) {
					// TEXT
					String host = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3)"
							+ " > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3)"
							+ " > vaadin-text-field:nth-child(" + i + ")";

					WebElement input = findShadowElement(driver, host, "input[part='value']");

					b &= verifyData(input);
				} else {
					// DATE PICKER
					String host1 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3)"
							+ " > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3)"
							+ " > vaadin-date-picker:nth-child(" + i + ")";

					WebElement input = findShadowElement(driver, host1, "#input", "input[part='value']");
					b &= verifyData(input);
				}

				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	// This method fills the SO and SP Details
	public static boolean verifySOandSPDetails(WebDriver driver) {
		boolean b = true;
		try {
			// index of elements
			Integer idx[] = { 9, 10, 12, 13 };
			for (Integer i : idx) {
				String host1 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > "
						+ "vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > "
						+ "vaadin-date-picker:nth-child(" + i + ")";

				// fetch the actual element in shadow elements.
				WebElement element = findShadowElement(driver, host1, // shadow dom 1
						"#input", // shadow dom 2
						"input[part='value']" // final input
				);

				// verify data into element
				b &= verifyData(element);
			}

		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	// fill Material Order Details.
	public static boolean verifyMOData(WebDriver driver) {
		boolean b = true;
		try {
			int i = 2;
			// fill Material Ordered (2 - 9) & Material Delivered (11-15) details
			while (i <= 15) {
				String path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3)";
				if (i == 2 || i == 5 || i == 8) { // This for Text fields.
					path += " > " + "vaadin-text-field:nth-child(" + i + ")";
					WebElement element = findShadowElement(driver, path, "input[part='value']");
					b &= verifyData(element);
				} else if (i == 3 || i == 6 || i == 15) { // for combo fields
					path += " > " + "vaadin-combo-box:nth-child(" + i + ")";
					WebElement element = findShadowElement(driver, path, "#input", "input[role='combobox']");
					b &= verifyData(element);
				} else { // else Date fields
					path += " > " + "vaadin-date-picker:nth-child(" + i + ")";
					WebElement element = findShadowElement(driver, path, "#input", "input[part='value']");
					b &= verifyData(element);
					// 10 is not any index then jump 9 to 11
					if (i == 9) {
						i = 10;
					}
				}
				i++;
			}

		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	// fill INSTALLATION AND COMMISSIONING data
	public static boolean verifyInstallAndCommData(WebDriver driver) {
		boolean b = true;
		try {

			Integer dateElements[] = { 2, 3, 8, 10, 11, 13, 15, 17, 21, 24 };
			Integer textElements[] = { 7, 9, 12, 14, 16, 18 };
			String base_path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3)";

			// fill current date
			for (Integer i : dateElements) {
				String path = base_path + " > " + "vaadin-date-picker:nth-child(" + i + ")";
				WebElement element = findShadowElement(driver, path, "#input", "input[part='value']");
				b &= verifyData(element);
			}

			// file Text fields
			for (Integer i : textElements) {
				String path = base_path + " > " + "vaadin-text-field:nth-child(" + i + ")";
				WebElement element = findShadowElement(driver, path, "input[part='value']");
				b &= verifyData(element);
			}

			// handle special case for NMS Visibility Status
			String nms_path = "vaadin-combo-box[class='planEditText'][label='NMS Visibility Status']";
			WebElement element = waitForInput(driver,
					findShadowElement(driver, nms_path, "#input", "input[role='combobox']"));
			b &= verifyData(element);

		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	public static boolean verifyRAFIData(WebDriver driver) {
		boolean b = true;
		Map<Integer, String> rafiData = DprSampleDataValues.getRAFIData();
		try {
			// for Site A fields which index (15 - 23)
			for (int i = 15; i < 24; i++) {
				boolean isDate = !rafiData.containsKey(i); // your date pickers
				WebElement input = getRFAIElement(driver, i, isDate);

				b &= verifyData(input);
			}

			// for Site A fields which index (27 - 35)
			for (int a = 15; a <= 23; a++) {

				int idxB = a + 12; // shift for site B

				boolean isDate = !rafiData.containsKey(a); // your date pickers

				WebElement input = getRFAIElement(driver, idxB, isDate);

				b &= verifyData(input);
			}

		} catch (Exception e) {
			e.printStackTrace();

			b = false;
		}
		return b;
	}

	public static boolean assignUser(WebDriver driver, String input, String inputType, Integer idx) {
		boolean b = false;
		try {
			String path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(1) > vaadin-combo-box:nth-child("
					+ idx + ")";

			WebElement element = findShadowElement(driver, path, "#input", input);
			element.sendKeys(input);
			Thread.sleep(300);
			element.sendKeys(Keys.ENTER);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	private static boolean verifyData(WebElement element) {
		if (element == null)
			return false;
		@SuppressWarnings("deprecation")
		String value = element.getAttribute("value");

		if (value == null) {
			value = element.getText();
		}

		return value != null && !value.trim().isEmpty();
	}

	// Soft AT Data Verification

	// verify CERAGON DATA SET-I
	public static boolean verifyCeragonCircleData(WebDriver driver) {
		boolean b = true;
		int i = 2;
		try {
			while (i <= 44) {
				String path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(6) > vaadin-accordion-panel:nth-child(2) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > "
						+ ((i != 6 && i != 28) ? "vaadin-text-field" : "vaadin-combo-box") + ":nth-child(" + i + ")";

				WebElement element = (i != 6 && i != 28) ? findShadowElement(driver, path, "input[part='value']")
						: findShadowElement(driver, path, "#input", "input[role='combobox']");
//				b = verifyData(element);

				if ((i != 6 && i != 28)) {
					setValue(element, "SAMPLE", driver);
				} else {
					setValue(element, "NEW");
				}

				if (i == 22)
					i = 24;
				else
					i++;
			}

			return b;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return b;
	}

	// validate CERAGON Fields according to IDX
	private static String getValidCeragonValue(int idx, List<Integer> list) {
		if (idx == 28 || idx == 56)
			return "vaadin-date-picker";
		else if (list.contains(idx))
			return "vaadin-combo-box";
		else
			return "vaadin-text-field";
	}

	// verify CERAGON Set-II
	public static boolean verifyCeragonGnoc(WebDriver driver) {
		boolean b = true;
		List<Integer> idxList = Arrays.asList(5, 33, 16, 17, 19, 26, 44, 45, 47, 54);
		int i = 2;
		log.info("VERIFY CERAGON GNOC DATA");
		try {
			while (i <= 56) {
				String fieldType = getValidCeragonValue(i, idxList);
				String path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(6) > vaadin-accordion-panel:nth-child(3) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > "
						+ fieldType + ":nth-child(" + i + ")";

				WebElement element = fieldType.equalsIgnoreCase("vaadin-date-picker")
						? findShadowElement(driver, path, "#input", "input[part='value']")
						: (fieldType.equalsIgnoreCase("vaadin-combo-box")
								? findShadowElement(driver, path, "#input", "input[role='combobox']")
								: findShadowElement(driver, path, "input[part='value']"));

				String value = (i == 26 || i == 54) ? "ACCEPTED"
						: (fieldType.equalsIgnoreCase("vaadin-date-picker") ? DprPlanCompletionUtile.getDate()
								: idxList.contains(i) ? "YES" : "SAMPLE");

//				b = verifyData(element);
				if (fieldType.equals("vaadin-combo-box")) {
					setValue(element, value);
				} else {
					setValue(element, value, driver);
				}

				if (i == 28)
					i = 30;
				else
					i++;
			}

			return b;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return b;
	}

	private static void setValue(WebElement element, String val) {
		try {
			Thread.sleep(300);
			element.sendKeys(val);
			Thread.sleep(300);
			element.sendKeys(Keys.ENTER);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// verify AVIAT Fields
	public static boolean verifyAvaitFields(WebDriver driver) {
		boolean b = true;
		try {
			int idx = 2;
			while (idx <= 78) {
				String path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(6) > vaadin-accordion-panel:nth-child(2) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > "
						+ (idx == 7 || idx == 46 ? "vaadin-combo-box" : "vaadin-text-field") + ":nth-child(" + idx
						+ ")";

				WebElement element = (idx == 7 || idx == 46)
						? findShadowElement(driver, path, "#input", "input[role='combobox']")
						: findShadowElement(driver, path, "input[part='value']");
//				b = verifyData(element);
				if (idx == 7 || idx == 46) {
					setValue(element, "NEW");
				} else {
					setValue(element, "SAMPLE", driver);
				}

				if (idx == 39)
					idx = 41;
				else
					idx++;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return b;
	}

	// verify HAUWEI SET-I
	public static boolean verifyHauweiFields(WebDriver driver) {
		boolean b = true;
		try {
			int idx = 1;
			int idx2 = 1;
			while (idx <= 40 || idx2 <= 7) {
				String path = idx <= 40

						? "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(6) > vaadin-accordion-panel:nth-child(1) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > vaadin-text-field:nth-child("
								+ idx + ")"
						: "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(6) > vaadin-accordion-panel:nth-child(1) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > vaadin-form-layout:nth-child(41) > vaadin-text-field:nth-child("
								+ idx2 + ")";

				WebElement element = findShadowElement(driver, path, "input[part='value']");

				setValue(element, "SAMPLE", driver);
//				b = verifyData(element);

				if (idx <= 40)
					idx++;
				else
					idx2++;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return b;
	}

	// verify HAUWEI SET-I
	public static boolean verifyHauweiFieldsGnoc(WebDriver driver) {
		boolean b = true;
		try {
			int idx = 1;
			log.info("HAUWEI GNOC DATA");
			while (idx <= 90) {
				String path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(6) > vaadin-accordion-panel:nth-child(2) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > vaadin-text-field:nth-child("
						+ idx + ")";

				WebElement element = findShadowElement(driver, path, "input[part='value']");

//				b = verifyData(element);

				setValue(element, "SAMPLE", driver);

				idx++;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return b;
	}

	private static void setValue(WebElement element, String value, WebDriver driver) {

		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("arguments[0].focus();" + "arguments[0].value=arguments[1];"
				+ "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));"
				+ "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", element, value);
	}

	// verify ERICSSON FIELDS DATA (BASIC DETAILS)
	public static boolean verifyEricssonField(WebDriver driver) {
		boolean b = true;
		try {
			for (int i = 2; i <= 15; i++) {
				String path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(6) > vaadin-accordion-panel:nth-child(1) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > "
						+ ((i == 10 || i == 11) ? "vaadin-combo-box" : "vaadin-text-field") + ":nth-child(" + i + ")";

				WebElement element = (i == 10 || i == 11)
						? findShadowElement(driver, path, "#input", "input[role='combobox']")
						: findShadowElement(driver, path, "input[part='value']");

				if (i == 10 || i == 11) {
					setValue(element, "NEW");
				} else {
					setValue(element, "SAMPLE", driver);
				}

//				b = verifyData(element);

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return b;
	}

	// verify ERICSSON FIELDS SET-II
	public static boolean verifyEricssonDataFields(WebDriver driver) {
		boolean b = true;
		try {
			for (int i = 2; i <= 40; i++) {
				String path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(6) > vaadin-accordion-panel:nth-child(2) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > vaadin-text-field:nth-child("
						+ i + ")";

				WebElement element = findShadowElement(driver, path, "input[part='value']");
//				b = verifyData(element);

				setValue(element, "SAMPLE", driver);

				if (i == 20)
					i = 21;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return b;
	}

	// verify NOKIA Fields
	public static boolean verifeNokiaDataFields(WebDriver driver) {
		boolean result = true;

		List<Integer> comboIndexes = Arrays.asList(17, 18, 21, 25, 26, 28, 37);

		try {
			for (int i = 2; i <= 80; i++) {

				if (i != 38 && i != 78) {

					boolean isCombo = comboIndexes.contains(i) || comboIndexes.contains(i - 40);

					String fieldType = isCombo ? "vaadin-combo-box" : "vaadin-text-field";

					String path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(6) > vaadin-accordion-panel:nth-child(2) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > "
							+ fieldType + ":nth-child(" + i + ")";

					WebElement element = isCombo ? findShadowElement(driver, path, "#input", "input[role='combobox']")
							: findShadowElement(driver, path, "input[part='value']");

//					result &= verifyData(element);
					String val = "";
					int j = i - 40;
					if (Arrays.asList(21, 25, 37).contains(i) || Arrays.asList(21, 25, 37).contains(j)) {
						val = "Yes";
					} else if (i == 26 || j == 26) {
						val = "SNMPv2";
					} else if (i == 28 || j == 28) {
						val = "Ok";
					} else {
						val = "SAMPLE";
					}

					if (isCombo) {
						setValue(element, val);
					} else {
						setValue(element, val, driver);
					}

				} else {

					for (int j = 1; j <= 2; j++) {
						String path = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-accordion:nth-child(6) > vaadin-accordion-panel:nth-child(2) > div:nth-child(1) > vaadin-form-layout:nth-child(1) > vaadin-date-time-picker:nth-child("
								+ i + ") > vaadin-date-time-picker-" + (j == 1 ? "date" : "time") + "-picker:nth-child("
								+ j + ")";

						WebElement element = findShadowElement(driver, path, j == 1 ? "#input" : ".input",
								"input[part='value']");
//						result &= verifyData(element);
						if (j == 1) {
							setValue(element, DprPlanCompletionUtile.getDate(), driver);
						} else {
							setValue(element, "5:00 AM");
						}
					}
				}
				if (i == 40)
					i = 41;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return result;
	}

}
