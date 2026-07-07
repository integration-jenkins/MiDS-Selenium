package com.automation.testing;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import com.automation.testing.dismantle.DismantleBulkUpload;
import com.automation.testing.dismantle.DownloadDismantleReports;
import com.automation.testing.testcase.AtBulkUploadRaiseAccept;
import com.automation.testing.testcase.BulkRevisionTest;
import com.automation.testing.testcase.BulkUploadFunctionalityTest;
import com.automation.testing.testcase.DprBulkUploadTest;
import com.automation.testing.testcase.DprPlanCompletionTest;
import com.automation.testing.testcase.LbBulkUploadTest;
import com.automation.testing.testcase.ManualAssignment;
import com.automation.testing.testcase.PhyAtTest;
import com.automation.testing.testcase.RequestForCancellationTest;
import com.automation.testing.testcase.SoftAtBulkUploadTest;
import com.automation.testing.testcase.VerifyAllPages;
import com.automation.testing.testcase.VerifyNewDeploymentReports;
import com.automation.testing.traffic.BulkOptionsTest;
import com.automation.testing.traffic.DownloadTrafficShiftingReports;
import com.automation.testing.traffic.TrafficShiftingBulkUploadTest;
import com.automation.testing.traffic.TrafficShiftingPlanTest;

@SpringBootApplication
@ComponentScan(basePackages = "com.automation.testing")
@EnableAsync
public class TestingApplication {

	private static final Logger log = LoggerFactory.getLogger(TestingApplication.class);

	public static void main(String[] args) {

		var context = SpringApplication.run(TestingApplication.class, args);

		String[] testList = { "Download All reports.", "Complete One Dpr Plan SR Pending To TS Complete.",
				"LB Bulk Upload Testing.", "Dpr Bulk upload Testing", "Soft AT Bulk Upload Testing.",
				"AT Raise & Accept Bulk Upload Testing.", "Download All Traffic Shifting reports",
				"Verify TS plan process", "Traffic Shifting BUlK UPLOAD", "Download All Dismantle Reports",
				"Manual Assignment Test", "Verify Bulk Upload (Upload Assignment, Cancellation and soft Upgrade",
				"TS Bulk Options Test", "Dismantle Bulk Upload Test", "Verify Request For cancellations",
				"Verify All Pages", "Verify Phy AT (Bulk + Manual)", "Bulk Revision Test" };

		boolean b = true;
		System.out.println("Select the testing.");
		for (int i = 0; i < testList.length; i++) {
			System.out.println((i + 1) + ". " + testList[i]);
		}
		System.out.println("Enter any Input to exit.");

		while (b) {
			@SuppressWarnings("resource")
			String input = new Scanner(System.in).next();
			switch (input) {
			case "1": {
				VerifyNewDeploymentReports test = context.getBean(VerifyNewDeploymentReports.class);
				test.run();
				break;
			}
			case "2": {
				DprPlanCompletionTest dprTest = context.getBean(DprPlanCompletionTest.class);
				dprTest.run();
				break;
			}
			case "3": {
				LbBulkUploadTest bulkTest = context.getBean(LbBulkUploadTest.class);
				bulkTest.run();
				break;
			}
			case "4": {
				DprBulkUploadTest bulkTest = context.getBean(DprBulkUploadTest.class);
				bulkTest.run();
				break;
			}
			case "5": {
				SoftAtBulkUploadTest softAtBulkUploadTest = context.getBean(SoftAtBulkUploadTest.class);
				softAtBulkUploadTest.run();
				break;
			}
			case "6": {
				AtBulkUploadRaiseAccept atBulkUpload = context.getBean(AtBulkUploadRaiseAccept.class);
				atBulkUpload.run();
				break;
			}
			case "7": {
				DownloadTrafficShiftingReports download = context.getBean(DownloadTrafficShiftingReports.class);
				download.run();
				break;
			}
			case "8": {
				TrafficShiftingPlanTest test = context.getBean(TrafficShiftingPlanTest.class);
				test.run();
				break;
			}
			case "9": {
				TrafficShiftingBulkUploadTest test = context.getBean(TrafficShiftingBulkUploadTest.class);
				test.run();
				break;
			}
			case "10": {
				DownloadDismantleReports test = context.getBean(DownloadDismantleReports.class);
				test.run();
				break;
			}
			case "11": {
				ManualAssignment test = context.getBean(ManualAssignment.class);
				test.run();
				break;
			}
			case "12": {
				BulkUploadFunctionalityTest test = context.getBean(BulkUploadFunctionalityTest.class);
				test.run();
				break;
			}
			case "13": {
				BulkOptionsTest test = context.getBean(BulkOptionsTest.class);
				test.run();
				break;
			}
			case "14": {
				DismantleBulkUpload test = context.getBean(DismantleBulkUpload.class);
				test.run();
				break;
			}
			case "15": {
				RequestForCancellationTest test = context.getBean(RequestForCancellationTest.class);
				test.run();
				break;
			}
			case "16": {
				VerifyAllPages test = context.getBean(VerifyAllPages.class);
				test.run();
				break;
			}
			case "17": {
				PhyAtTest test = context.getBean(PhyAtTest.class);
				test.run();
				break;
			}
			case "18": {
				BulkRevisionTest bulkTest = context.getBean(BulkRevisionTest.class);
				bulkTest.run();
				break;
			}

			default:
				b = false;
				log.info("Test is Stop...");
			}
			System.out.println("Select the testing.");
		}

	}

}
