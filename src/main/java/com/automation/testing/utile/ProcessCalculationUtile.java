package com.automation.testing.utile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessCalculationUtile {

	private static final Logger log = LoggerFactory.getLogger(ProcessCalculationUtile.class);

	private Map<String, Long> startTimes = new HashMap<>();

	public void start(String key) {
		startTimes.put(key, System.currentTimeMillis());
	}

	public long end(String key) {
		Long start = startTimes.get(key);
		if (start == null) {
			log.info("Invaid Process "+key);
			return 0;
		}
		long duration = System.currentTimeMillis() - start;
		startTimes.put(key, duration);
		return duration;
	}

	public void endAndPrint(String key) {
		long durationMs = startTimes.get(key);
		String timeFormatted = formatTime(durationMs);
		log.info(key + " : Time taken : " + timeFormatted);
	}

	public String getExecutionTime(String key) {
		return formatTime(startTimes.get(key));
	}

	public static String formatTime(long durationMs) {
		if (durationMs < 0)
			durationMs = 0;

		long totalSeconds = Math.round(durationMs / 1000.0);
		long minutes = totalSeconds / 60;
		long seconds = totalSeconds % 60;

		return minutes > 0 ? String.format("%02d min : %02d sec", minutes, seconds)
				: String.format("%02d sec", seconds);
	}

	public void getAllProcessDetails() {
		for (String key : List.copyOf(startTimes.keySet())) {
			endAndPrint(key);
		}
	}

	public Map<String, Long> getAllProcess() {
		return startTimes;
	}

}
