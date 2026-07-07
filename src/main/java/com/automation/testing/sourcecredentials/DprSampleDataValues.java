package com.automation.testing.sourcecredentials;

import java.util.HashMap;
import java.util.Map;

public class DprSampleDataValues {
	
	public static Map<Integer,String> getRAFIData(){
		Map<Integer,String> rafiData = new HashMap<>();
		
		rafiData.put(18,"ST202512345678");
		rafiData.put(19, "S202512345678");
		rafiData.put(23, "P/AFW/BBY/1248274/2025");
		
		return rafiData;
	}

}
