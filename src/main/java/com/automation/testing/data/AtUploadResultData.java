package com.automation.testing.data;

import java.util.HashMap;
import java.util.Map;

public class AtUploadResultData {
	private Integer softATCount;
	private Integer phyAtCount;
	private Integer totalAtUpdate;
	private Integer errorCount;

	private Map<Integer, String> errorMessage = new HashMap<>();

	public String getMessage(Integer key) {
		if (key != null) {
			return errorMessage.get(key);
		}
		return null;
	}

	public Integer getSoftATCount() {
		return softATCount;
	}

	public void setSoftATCount(Integer softATCount) {
		this.softATCount = softATCount;
	}

	public Integer getPhyAtCount() {
		return phyAtCount;
	}

	public void setPhyAtCount(Integer phyAtCount) {
		this.phyAtCount = phyAtCount;
	}

	public Integer getTotalAtUpdate() {
		return totalAtUpdate;
	}

	public void setTotalAtUpdate(Integer totalAtUpdate) {
		this.totalAtUpdate = totalAtUpdate;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public Map<Integer, String> getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(Map<Integer, String> errorMessage) {
		this.errorMessage = errorMessage;
	}

}
