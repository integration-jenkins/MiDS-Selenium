package com.automation.testing.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.automation.testing.data.BulkUploadError;
import com.automation.testing.data.ProcessData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CreateResultSheet {

	private static final String BASE_PATH = "D:\\Automation_result\\" + LocalDate.now() + "\\";

	private static final Logger log = LoggerFactory.getLogger(CreateResultSheet.class);

	public boolean createResult(String moduleName, List<ProcessData> data) {
		boolean sheetCreated = false;
		try {
			// Prepare directory
			File folder = new File(BASE_PATH);
			if (!folder.exists()) {
				folder.mkdirs(); // Create folder if missing
			}

			// Generate file name
			String fileName = getUniqueFileName(moduleName);
			File file = new File(BASE_PATH + fileName);

			// Write CSV
			try (FileWriter writer = new FileWriter(file)) {

				// Write header
				List<String> header = Arrays.asList("PROCESS NO.", "PROCESS NAME", "USER DEPARTMENT", "TEST STATUS",
						"EXECUTION TIME", "EXECUTION DATE", "REMARK");
				writeLine(writer, header);

				int idx = 1;

				for (ProcessData entry : data) {

					List<String> row = Arrays.asList(String.valueOf(idx), entry.getName(),
							entry.getUserDepartment() != null && !entry.getUserDepartment().isEmpty()
									? entry.getUserDepartment()
									: "",
							String.valueOf(entry.getExecutionStatus() == true ? "PASS" : "FAIL"),
							entry.getExecutionTime(), String.valueOf(entry.getExecutionDate()), entry.getRemark());

					writeLine(writer, row);
					idx++;
				}

				writer.flush();
				sheetCreated = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sheetCreated;
	}

	public synchronized boolean createResultForAssignment(String moduleName, List<ProcessData> data) {
		boolean sheetCreated = false;
		try {
			// Prepare directory
			File folder = new File(BASE_PATH);
			if (!folder.exists()) {
				folder.mkdirs(); // Create folder if missing
			}

			// Generate file name
			String fileName = getUniqueFileName(moduleName);
			File file = new File(BASE_PATH + fileName);

			// Write CSV
			try (FileWriter writer = new FileWriter(file)) {

				// Write header
				List<String> header = Arrays.asList("PROCESS NO.", "PLAN STAGE", "USER DEPARTMENT", "ASSIGNED USER",
						"NOTIFICATION MESSAGE", "TEST STATUS", "EXECUTION TIME", "EXECUTION DATE", "REMARK");
				writeLine(writer, header);

				int idx = 1;

				for (ProcessData entry : data) {
					List<String> row = Arrays.asList(String.valueOf(idx),
							entry.getStage() != null ? entry.getStage() : "",
							entry.getUserDepartment() != null && !entry.getUserDepartment().isEmpty()
									? entry.getUserDepartment()
									: "",
							entry.getAssignedDep() != null && !entry.getAssignedDep().isEmpty() ? entry.getAssignedDep()
									: "",
							entry.getNotification() != null && !entry.getNotification().isEmpty()
									? entry.getNotification()
									: "",
							String.valueOf(entry.getExecutionStatus() == true ? "PASS" : "FAIL"),
							entry.getExecutionTime(), String.valueOf(entry.getExecutionDate()), entry.getRemark());

					writeLine(writer, row);
					idx++;
				}

				writer.flush();
				sheetCreated = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sheetCreated;
	}

	public boolean createResultBulkUpload(String moduleName, List<ProcessData> data) {
		boolean sheetCreated = false;
		try {
			// Prepare directory
			File folder = new File(BASE_PATH);
			if (!folder.exists()) {
				folder.mkdirs(); // Create folder if missing
			}

			// Generate file name
			String fileName = getUniqueFileName(moduleName);
			File file = new File(BASE_PATH + fileName);

			// Write CSV
			try (FileWriter writer = new FileWriter(file)) {

				// Write header
				List<String> header = Arrays.asList("PROCESS NO.", "PROCESS NAME", "USER DEPARTMENT", "TEST STATUS",
						"EXECUTION TIME", "EXECUTION DATE", "ATTRIBUTE NAME", "TEST SCNARIO", "REMARK");
				writeLine(writer, header);

				int idx = 1;

				for (ProcessData entry : data) {

					List<String> row = Arrays.asList(String.valueOf(idx), entry.getName(),
							entry.getUserDepartment() != null && !entry.getUserDepartment().isEmpty()
									? entry.getUserDepartment()
									: "",
							String.valueOf(entry.getExecutionStatus() == true ? "PASS" : "FAIL"),
							entry.getExecutionTime(), String.valueOf(entry.getExecutionDate()),
							entry.getAttributeName(), entry.getAttributeRemark(), entry.getRemark());

					writeLine(writer, row);
					idx++;
				}

				writer.flush();
				sheetCreated = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sheetCreated;
	}

	public synchronized boolean createResultForDownloadReport(String moduleName, List<ProcessData> data) {
		boolean sheetCreated = false;
		try {
			// Prepare directory
			File folder = new File(BASE_PATH);
			if (!folder.exists()) {
				folder.mkdirs(); // Create folder if missing
			}

			// Generate file name
			String fileName = getUniqueFileName(moduleName);
			File file = new File(BASE_PATH + fileName);

			// Write CSV
			try (FileWriter writer = new FileWriter(file)) {

				// Write header
				List<String> header = Arrays.asList("PROCESS NO.", "PROCESS NAME", "USER DEPARTMENT", "TEST STATUS",
						"EXECUTION TIME", "EXECUTION DATE", "REMARK", "TOTAL COUNT");
				writeLine(writer, header);

				int idx = 1;

				for (ProcessData entry : data) {

					List<String> row = Arrays.asList(String.valueOf(idx), entry.getName(),
							entry.getUserDepartment() != null ? entry.getUserDepartment() : "",
							String.valueOf(entry.getExecutionStatus() == true ? "PASS" : "FAIL"),
							entry.getExecutionTime(), String.valueOf(entry.getExecutionDate()), entry.getRemark(),
							String.valueOf(entry.getReportCount()));

					writeLine(writer, row);
					idx++;
				}

				writer.flush();
				sheetCreated = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sheetCreated;
	}

	// Write a CSV line
	private synchronized void writeLine(FileWriter writer, List<String> values) throws IOException {
		StringBuilder line = new StringBuilder();

		for (int i = 0; i < values.size(); i++) {
			try {
				String value = values.get(i) != null ? values.get(i) : "";

				// Escape quotes
				value = value.replace("\"", "\"\"");

				// Add quotes if needed
				if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
					value = "\"" + value + "\"";
				}

				line.append(value);

				if (i < values.size() - 1) {
					line.append(",");
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		line.append("\n");
		writer.write(line.toString());
	}

	// Generate unique file name
	private static String getUniqueFileName(String prefix) {
		String extension = "csv";
		String time = java.time.LocalDateTime.now()
				.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));

		int random = (int) (Math.random() * 100000);

		return prefix + "_" + time + "_" + random + "." + extension;
	}

	public ProcessData createResultData(String processName, String excTime, boolean status, String remark,
			String department) {
		ProcessData data = new ProcessData();

		data.setExecutionDate(LocalDate.now());
		data.setName(processName);
		data.setExecutionTime(excTime);
		data.setRemark(remark);
		data.setExecutionStatus(status);
		data.setUserDepartment(department);

		return data;
	}

	public ProcessData createResultData(String processName, String excTime, String remark, boolean status,
			String department, String assignedDep, String notification, String stage) {
		ProcessData data = new ProcessData();

		data.setExecutionDate(LocalDate.now());
		data.setName(processName);
		data.setExecutionTime(excTime);
		data.setExecutionStatus(status);
		data.setUserDepartment(department);
		data.setAssignedDep(assignedDep);
		data.setNotification(notification);
		data.setRemark(remark);
		data.setStage(stage);

		return data;
	}

	public ProcessData createResultData(String processName, String excTime, boolean status, String remark,
			String department, String attribute, String attributeRemark) {
		ProcessData data = new ProcessData();

		data.setExecutionDate(LocalDate.now());
		data.setName(processName);
		data.setExecutionTime(excTime);
		data.setRemark(remark);
		data.setExecutionStatus(status);
		data.setUserDepartment(department);
		data.setAttributeName(attribute);
		data.setAttributeRemark(attributeRemark);

		return data;
	}

	public ProcessData createResultData(String processName, String excTime, boolean status, String remark,
			String department, Integer count) {
		ProcessData data = new ProcessData();

		data.setExecutionDate(LocalDate.now());
		data.setName(processName);
		data.setExecutionTime(excTime);
		data.setRemark(remark);
		data.setExecutionStatus(status);
		data.setUserDepartment(department);
		data.setReportCount(count);

		return data;
	}

	// read the attributes sheet and return the output
	public Map<Integer, String> readAttributeValidation(String path) {
		Map<Integer, String> invalidAttributes = new HashMap<>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			invalidAttributes = mapper.readValue(new File(path), new TypeReference<Map<Integer, String>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Issue in read Attribute validation.");
		}

		return invalidAttributes;
	}

	public boolean readDownloadFiles(String path) {
		File folder = new File(path);

		if (!folder.exists() || !folder.isDirectory()) {
			System.out.println("Invalid folder path");
			return false;
		}

		File[] files = folder.listFiles(File::isFile);
		if (files == null || files.length == 0) {
			System.out.println("No files found");
			return false;
		}

		for (File file : files) {
			System.out.println(file.getName());
		}
		return true;
	}

	public static Map<String, Map<String, BulkUploadError>> getDismantleValidation(String path) {
		Map<String, Map<String, BulkUploadError>> resultMap = new LinkedHashMap<>();
		log.info("Start to parse CSV");
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {

			String line;

			while ((line = br.readLine()) != null) {

				if (line.trim().isEmpty())
					continue;

				// Split by |
				String[] parts = line.split(",");

				String module = parts[0].trim();
				String rowNumber = parts[1].trim();
				String attribute = parts[2].trim();
				String validation = parts[3].trim();

				// Create object
				BulkUploadError av = new BulkUploadError(attribute, validation);

				resultMap.computeIfAbsent(module, k -> new LinkedHashMap<>()).put(rowNumber, av);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultMap;
	}

}
