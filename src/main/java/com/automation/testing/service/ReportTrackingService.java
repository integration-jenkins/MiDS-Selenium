package com.automation.testing.service;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.automation.testing.data.ReportExeutionData;
import com.automation.testing.enums.ProcessName;
import com.automation.testing.utile.ProcessCalculationUtile;

import jakarta.annotation.PostConstruct;

@Service
public class ReportTrackingService {

	private static final Logger log = LoggerFactory.getLogger(ReportTrackingService.class);

	private ScheduledExecutorService scheduler;
	private volatile boolean paused = true;

	private final File folder = new File("D:\\Downloads");

//	@PostConstruct
	public void init() {
		scheduler = Executors.newSingleThreadScheduledExecutor();

		// schedule ONCE
		scheduler.scheduleAtFixedRate(this::verifySafely, 0, 5, TimeUnit.SECONDS);

		log.info("Report tracking scheduler initialized");
	}

	public void start() {
		paused = false;
		log.info("Report tracking STARTED");
	}

	public void pause() {
		paused = true;
		log.info("Report tracking PAUSED");
	}

	public void stop() {
		paused = true;
		scheduler.shutdownNow();
		log.info("Report tracking STOPPED");
	}

	private Map<ProcessName, ReportExeutionData> reportMap;

	public void bind(Map<ProcessName, ReportExeutionData> reportMap) {
		this.reportMap = reportMap;
	}

	private void verifySafely() {
		try {
			log.info("Download Verify Thread RUNNINNG.....");
			if (paused || reportMap == null || reportMap.isEmpty()) {
				log.info("Map Is Empty");
				return;
			}
			verifyDownloadReports();
		} catch (Exception e) {
			log.error("Verify thread error", e);
		}
	}

	private void verifyDownloadReports() {

		for (ReportExeutionData data : reportMap.values()) {

			if (!data.isCompleted() && findReport(data.getReport())) {

				data.setCompleted(true);

				String time = ProcessCalculationUtile.formatTime(System.currentTimeMillis() - data.getStartTime());

				data.setExecutionTime(time);

				log.info("{} REPORT DOWNLOADED in {}", data.getReportName(), time);
			}
		}
	}

	private boolean findReport(String reportName) {

		File[] files = folder.listFiles(File::isFile);
		if (files == null)
			return false;

		for (File file : files) {
			if (file.getName().contains(reportName) && !file.getName().endsWith(".part")) {
				file.delete();
				return true;
			}
		}
		return false;
	}

	public static void createRequest(ProcessName name, String reportName, Map<ProcessName, ReportExeutionData> map) {

		ReportExeutionData data = new ReportExeutionData();
		data.setReportName(name);
		data.setReport(reportName);
		data.setStartTime(System.currentTimeMillis());
		map.put(name, data);
	}

	public boolean removeDownloadFiles() {

		if (!folder.exists() || !folder.isDirectory()) {
			log.info("Invalid folder path");
			return false;
		}

		File[] files = folder.listFiles(File::isFile);
		if (files == null || files.length == 0) {
			log.info("No files found");
			return true;
		}

		boolean allDeleted = true;

		for (File file : files) {

			// skip in-progress downloads
			if (file.getName().endsWith(".part")) {
				log.info("Skipping in-progress file: {}", file.getName());
				continue;
			}

			boolean deleted = false;

			// retry delete (Windows file lock issue)
			for (int i = 0; i < 3; i++) {
				if (file.delete()) {
					log.info("Deleted file: {}", file.getName());
					deleted = true;
					break;
				}
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}

			if (!deleted) {
				log.warn("Failed to delete file: {}", file.getName());
				allDeleted = false;
			}
		}

		return allDeleted;
	}

}
