package com.automation.testing.testcase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.automation.testing.data.BulkUploadValidationData;
import com.automation.testing.enums.TestType;
import com.automation.testing.service.BulkUploadTestService;

@Component
public class BulkUploadTesting {

	@Autowired
	private BulkUploadTestService bulkUploadTestService;
	

	public void testLbPlanUpload() {
		String circle = "JK";
		String path = "D:\\testingProject\\testing\\src\\main\\resources\\project_file\\LB_Attributes_Test.xlsx";
		String linkId = "";
		
		BulkUploadValidationData bulkTestData = new BulkUploadValidationData();

		bulkTestData.setCircle(circle);
		bulkTestData.setPlanId(linkId);
		bulkTestData.setRemark("Test Lb Upload Plan");
		bulkTestData.setSheetPath(path);
		bulkTestData.setTestType(TestType.POSITIVE_TEST);

//		bulkUploadTestService.testBulkUpload(bulkTestData);
		bulkUploadTestService.verifyAttributesValidationLB(bulkTestData);

	}

}
