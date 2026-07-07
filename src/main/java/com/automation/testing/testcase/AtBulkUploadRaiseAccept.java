package com.automation.testing.testcase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.automation.testing.service.AtService;

@Component
public class AtBulkUploadRaiseAccept {

	@Autowired
	private AtService atService;

	private final String AT_DETAILS_PATH = "D:\\testingProject\\testing\\src\\main\\resources\\project_file\\json_files\\AT_UPLOAD_SAMPLE_DETAIL.json";

	public void run() {
		atService.verifyAtRaiseAccept(AT_DETAILS_PATH);
	}
}
