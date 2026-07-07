package com.automation.testing.utile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.automation.testing.data.BulkUploadError;
import com.automation.testing.data.ProcessData;
import com.automation.testing.enums.ProcessName;
import com.automation.testing.service.CreateResultSheet;
import com.automation.testing.testcase.BulkUploadFunctionalityTest;

@Component
public class ResultDataUtile {

	private static final Logger log = LoggerFactory.getLogger(ResultDataUtile.class);

	@Autowired
	private CreateResultSheet createResultSheet;

	private List<ProcessData> data = new ArrayList<>();
	private ProcessCalculationUtile process = new ProcessCalculationUtile();

	public void createResult(String name) {
		createResultSheet.createResult(name, data);
	}

	public void runStep(ProcessName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			List<BulkUploadError> errors, String dep, String attribute, String scenario) {

		process.start(name.name());
		boolean result = step.get(); // run the actual process
		process.end(name.name());

		String time = process.getExecutionTime(name.name());

		if (result) {
			data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, dep, attribute, scenario));
			log.info(name.name() + " -> " + time + " execute");
		} else {
			if (errors != null && !errors.isEmpty()) {
				// multiple error rows
				for (BulkUploadError err : errors) {
					String fullMsg = "Row " + err.getRowNumber() + " → " + err.getErrorMessage();
					data.add(createResultSheet.createResultData(name.name(), time, false, fullMsg, dep, attribute,
							scenario));
				}
			} else {
				// single generic error
				data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, dep, attribute,
						scenario));
			}
			log.info(name.name() + " -> " + time + " FAILED");
		}
	}

}
