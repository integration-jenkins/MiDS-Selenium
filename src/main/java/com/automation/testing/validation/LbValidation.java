package com.automation.testing.validation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automation.testing.data.BulkUploadError;
import com.automation.testing.data.BulkUploadWrapper;
import com.automation.testing.enums.ProcessName;

public class LbValidation {

	public static final Logger log = LoggerFactory.getLogger(LbValidation.class);

	public static boolean verifyLBInvalidInput(BulkUploadWrapper bulkUploadWrapper,
			Map<Integer, String> expextedResultMap) {
		if (bulkUploadWrapper.getErrorList().isEmpty())
			return false;
		for (BulkUploadError i : bulkUploadWrapper.getErrorList()) {
			String message = expextedResultMap.get(Integer.parseInt(i.getRowNumber()));
			if (message != null && !message.isEmpty()) {
				log.info(message + " = " + isMatched(i.getErrorMessage(), message));
				if (!isMatched(i.getErrorMessage(), message))
					return false;
			}
		}
		return true;
	}

	private static boolean isMatched(String a, String b) {

		List<String> aList = splitBySlash(a);
		List<String> bList = splitBySlash(b);

		log.info("A List = " + aList);
		log.info("B List = " + bList);

		if (aList.size() == bList.size()) {
			for (String i : aList) {
				if (!bList.contains(i)) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	private static List<String> splitBySlash(String text) {

		if (text == null || text.isEmpty()) {
			return List.of();
		}

		return Arrays.stream(text.split("\\s*/\\s*")) // <-- FIX
				.map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
	}

	public static Map<ProcessName, Map<String, String>> getLBAttributeTestList() {

		Map<ProcessName, Map<String, String>> mp = new HashMap<>();

		mp.put(ProcessName.LB_ATTRIBUTE_SET_A, Map.ofEntries(
				Map.entry("ROW 2", "HOP TYPE|*|Hop Type is EMPTY when plan Category is New Deployment Hybrid/FO"),
				Map.entry("ROW 3", "HOP TYPE|*|Hop Type is EMPTY when plan Category is New Deployment XPIC"),
				Map.entry("ROW 4", "HOP TYPE|*|Hop Type is EMPTY when plan Category is MIMO Upgrade"),
				Map.entry("ROW 5", "HOP TYPE|*|Hop Type is EMPTY when plan Category is Existing 1+0 to XPIC Upgrade"),
				Map.entry("ROW 6", "HOP TYPE|*|Hop Type is EMPTY when plan Category is Capacity Upgrade soft/DC"),
				Map.entry("ROW 7", "HOP TYPE|*|Hop Type is EMPTY when plan Category is 1+0 to 1 Gig Upgrade"),
				Map.entry("ROW 8", "HOP TYPE|*|Hop Type is EMPTY when plan Category is E Band"),
				Map.entry("ROW 9", "HOP TYPE|*|Hop Type is EMPTY when plan Category is FREQUENCY CHANGE"),
				Map.entry("ROW 10", "HOP TYPE|*|Hop Type is EMPTY when plan Category is CARD UPGRADE"),
				Map.entry("ROW 11", "HOP TYPE|*|Hop Type is EMPTY when plan Category is DUAL XPIC NEW H1"),
				Map.entry("ROW 12", "HOP TYPE|*|Hop Type is EMPTY when plan Category is DUAL XPIC NEW H2"),
				Map.entry("ROW 13",
						"HOP TYPE|*|Hop Type is EMPTY when plan Category is DUAL XPIC NEW H1 (Invalid Inputes)"),
				Map.entry("ROW 14",
						"HOP TYPE|*|Hop Type is EMPTY when plan Category is DUAL XPIC NEW H2 (Invalid Inputes)"),
				Map.entry("ROW 15", "HOP TYPE|*|Hop Type is EMPTY when plan Category is E BAND SDB(EXISTING)"),
				Map.entry("ROW 16", "HOP TYPE|*|Hop Type is EMPTY when plan Category is E BAND SDB(New)"),
				Map.entry("ROW 17", "HOP TYPE|*|Hop Type is INVALID when plan category is New Deployment hybrid/FO"),
				Map.entry("ROW 18", "HOP TYPE|*|Hop Type is INVALID when plan category is New Deployment XPIC"),
				Map.entry("ROW 19", "HOP TYPE|*|Hop Type is INVALID when plan category is MIMO Upgrade"),
				Map.entry("ROW 20",
						"HOP TYPE|*|Hop Type is INVALID when plan category is Existing 1+0 to XPIC Upgrade"),
				Map.entry("ROW 21", "HOP TYPE|*|Hop Type is INVALID when plan category is Capacity Upgrade soft/DC")));

		mp.put(ProcessName.LB_ATTRIBUTE_SET_B, Map.ofEntries(
				Map.entry("ROW 2", "PROJECT CATEGORY|*|Project Category is BLANK"),
				Map.entry("ROW 3", "PROJECT CATEGORY|*| Project category is INVALID"),
				Map.entry("ROW 4", "Nominal Quarter / Nominal Aop|*|Fill Nominal Quarter value Q3 & empty Nominal Aop"),
				Map.entry("ROW 5",
						"Nominal Quarter / Nominal Aop|*|Fill Nominal Quarter value Q2 & Set INVALID as Nominal Aop"),
				Map.entry("ROW 6",
						"Nominal Quarter / Nominal Aop |*|Fill Nominal Quarter value Q1 & set FY20_21(Invalid) as Nominal Aop"),
				Map.entry("ROW 7", "Nominal Quarter|*|Fill Nominal Quarter value EMPTY"),
				Map.entry("ROW 8", "Nominal Quarter|*|Fill Nominal Quarter value INVALID"),
				Map.entry("ROW 9",
						"HOP TYPE / Plan Release Date|*|Hop Type is INVALID when plan category is 1+0 to 1 Gig Upgrade & Set Invalid inputes as Plan Release Date"),
				Map.entry("ROW 10", "HOP TYPE|*|Hop Type is INVALID when plan category is E Band"),
				Map.entry("ROW 11", "HOP TYPE|*|Hop Type is INVALID when plan category is FREQUENCY CHANGE"),
				Map.entry("ROW 12", "HOP TYPE|*|Hop Type is INVALID when plan category is CARD UPGRADE"),
				Map.entry("ROW 13", "HOP TYPE|*|Hop Type is INVALID when plan category is Dual XPIC NEW"),
				Map.entry("ROW 14", "HOP TYPE|*|Hop Type is INVALID when plan category is Dual XPIC NEW"),
				Map.entry("ROW 15", "HOP TYPE|*|Hop Type is INVALID when plan category is Dual XPIC NEW"),
				Map.entry("ROW 16", "HOP TYPE|*|Hop Type is INVALID when plan category is Dual XPIC NEW"),
				Map.entry("ROW 17", "HOP TYPE|*|Hop Type is INVALID when plan category is E BAND SDB(EXISTING)"),
				Map.entry("ROW 18", "HOP TYPE|*|Hop Type is INVALID when plan category is E BAND SDB(New)")));

		mp.put(ProcessName.LB_ATTRIBUTE_SET_C, Map.ofEntries(
				Map.entry("ROW 2", "FINAL PROJECT|*|Final Project Fills EMPTY"),
				Map.entry("ROW 3", "FINAL PROJECT|*|Final Project Fills INVALID"),
				Map.entry("ROW 4", "EQUIPMENT MAKE|*|EQUIPMENT MAKE Fills EMPTY"),
				Map.entry("ROW 5", "EQUIPMENT MAKE|*|EQUIPMENT MAKE Fills INVALID"),
				Map.entry("ROW 6", "LINK ID|*|Link Id EMPTY"), Map.entry("ROW 7", "LINK ID|*|LINK id is INVALID"),
				Map.entry("ROW 8", "LINK ID|*|SiteA is INVALID and SiteB is VALID"),
				Map.entry("ROW 9", "LINK ID|*|SiteB is INVALID and SiteA is VALID"),
				Map.entry("ROW 10", "LINK ID|*|Both fills INVALID"), Map.entry("ROW 11", "SITE A/ SITE B|*|BOTH EMPTY"),
				Map.entry("ROW 12", "SITE A/SITE B|*|SiteA EMPTY and SitB VALID"),
				Map.entry("ROW 13", "SITE A/SITE B|*|SiteA VALID and SiteB EMPTY"),
				Map.entry("ROW 14", "SITE A/SITE B|*|SiteA INVALID and SiteB VALID"),
				Map.entry("ROW 15", "SITE A/SITE B|*|SiteA VALID and SiteB INVALID")));

		mp.put(ProcessName.LB_ATTRIBUTE_SET_D, Map.ofEntries(Map.entry("ROW 2", "Channel|*|Fills EMPTY"),
				Map.entry("ROW 3", "Channel|*|Fills INVALID"),
				Map.entry("ROW 4", "Channel|*|Circle is JK but Fills KK_70G_250M_F2"),
				Map.entry("ROW 5",
						"Channel|*|Fill JK_15G_56M_F12F13 different bindwidth when Plan category is New Deployment Hybrid/FO"),
				Map.entry("ROW 6", "TX/RX Frequency|*|TX Freq. is EMPTY"),
				Map.entry("ROW 7", "TX/RX Frequency|*|TX Freq. is INVALID"),
				Map.entry("ROW 8", "TX/RX Frequency|*|RX Freq. is EMPTY"),
				Map.entry("ROW 9", "TX/RX Frequency|*|RX Freq. is INVALID"),
				Map.entry("ROW 10", "TX/RX Frequency|*|Fills RX Freq. Value In TX Freq."),
				Map.entry("ROW 11", "TX/RX Frequency|*|Fills TX Freq. Value In RX Freq."),
				Map.entry("ROW 12", "TX/RX Frequency|*|TX Freq. is EMPTY and RX Freq. is VALID"),
				Map.entry("ROW 13", "TX/RX Frequency|*|RX Freq. is EMPTY and TX Freq. is VALID"),
				Map.entry("ROW 14", "TX/RX Antenna|*|Fills Empty at Both"),
				Map.entry("ROW 15", "TX/RX Antenna|*|Fills INVALID at Both"),
				Map.entry("ROW 16", "TX/RX Antenna|*|Fills as value 12.2.3 at Both"),
				Map.entry("ROW 17", "BER10e6 Eff. Margin (dB) |*| Fills as EMPTY"),
				Map.entry("ROW 18", "BER10e6 Eff. Margin (dB) |*|Fills as value 18"),
				Map.entry("ROW 19", "BER10e6 Eff. Margin (dB) |*|Fills as INVALID "),
				Map.entry("ROW 20", "Polarization|*|Fills EMPTY"),
				Map.entry("ROW 21", "Polarization|*|Fills INVALID")));

		mp.put(ProcessName.LB_ATTRIBUTE_SET_E, Map.ofEntries(
				Map.entry("ROW 2", "BER10e6 Rain Interference |*| Fills as 5.0"),
				Map.entry("ROW 3", "BER10e6 Rain Interference |*| Fills as INVALID"),
				Map.entry("ROW 4", "Availability|*|Fills INVALID"),
				Map.entry("ROW 5", "Availability|*|Fills 90 as value"),
				Map.entry("ROW 6", "Availability|*|Fills 99.9031909 as value"),
				Map.entry("ROW 7", "ACM Status/ Min Max QAM|*|ACM STATUS Fills as INVALID"),
				Map.entry("ROW 8", "ACM Status/ Min Max QAM|*|ACM Min QAM Fills as INVALID"),
				Map.entry("ROW 9", "ACM Status/ Min Max QAM|*|ACM Max QAM Fills as INVALID"),
				Map.entry("ROW 10", "ACM Status/ Min Max QAM|*|ACM STATUS Fills as EMPTY"),
				Map.entry("ROW 11", "ACM Status/ Min Max QAM|*|ACM Min QAM Fills as EMPTY"),
				Map.entry("ROW 12", "ACM Status/ Min Max QAM|*|ACM Max QAM Fills as EMPTY"),
				Map.entry("ROW 13",
						"ACM Status/ Min Max QAM|*|fills ACM MIN as QPSK and ACM MAX as 4096QAM when Equipment Make is ERICSSON"),
				Map.entry("ROW 14",
						"ATPC Status/ ATPC MIN/ ATPC MAX value|*|Equip. Make: ERICSSON; when fill invalid values(Enable, 20, 20)"),
				Map.entry("ROW 15",
						"ATPC Status/ ATPC MIN/ ATPC MAX value|*|Equip. Make: HUAWEI; when fill invalid values(Enable, 20, 20)"),
				Map.entry("ROW 16",
						"ATPC Status/ ATPC MIN/ ATPC MAX value|*|Equip. Make: CERAGON; when fill invalid values(Enable, 20, 20)"),
				Map.entry("ROW 17",
						"ATPC Status/ ATPC MIN/ ATPC MAX value|*|Equip. Make: AVIAT; when fill invalid values(Disable, 20, 20)"),
				Map.entry("ROW 18", "ATPC Status/ ATPC MIN/ ATPC MAX value|*|Equip. Make: NOKIA; when fill EMPTY"),
				Map.entry("ROW 19",
						"ATPC Status/ ATPC MIN/ ATPC MAX value|*|Equip. Make: HUAWEI; when fill invalid values(INVALID, INVALID, INVALID)")));

		mp.put(ProcessName.LB_ATTRIBUTE_SET_F, Map.ofEntries(
				Map.entry("ROW 2",
						"Tx Radio / Hop Nomenclature Site-A |*|Fills EMPTY and set empty as Hop Nomenclature Site-A"),
				Map.entry("ROW 3",
						"Tx Radio / Hop Nomenclature Site-B |*|Fills INVALID & set empty as Hop Nomenclature Site-B"),
				Map.entry("ROW 4", "Chassis Code Site-A|*|Fills EMPTY"),
				Map.entry("ROW 5", "Chassis Code Site-B|*|Fills EMPTY"),
				Map.entry("ROW 6", "(MODEM(IF Card)/Ethernet Port) Site-A|*|Fills EMPTY"),
				Map.entry("ROW 7", "(MODEM(IF Card)/Ethernet Port) Site-A|*|Fills INVALID"),
				Map.entry("ROW 8", "(MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-A|*|Fills EMPTY"),
				Map.entry("ROW 9", "(MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-A|*|FILLS INVALID"),
				Map.entry("ROW 10", "(MODEM(IF Card)/Ethernet Port) Site-B|*|Fills EMPTY"),
				Map.entry("ROW 11", "(MODEM(IF Card)/Ethernet Port) Site-B|*|Fills INVALID"),
				Map.entry("ROW 12", "(MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-B|*|Fills EMPTY"),
				Map.entry("ROW 13", "Fiber POP Id|*|Fills Empty"), Map.entry("ROW 14", "Fiber POP Id|*|Fills INVALID"),
				Map.entry("ROW 15", "PCM PATH|*|PCM PATH IS EMPTY"),
				Map.entry("ROW 16", "PCM PATH|*|SiteA Invalid and SiteB VALID"),
				Map.entry("ROW 17", "PCM PATH|*|SiteA valid and SiteB Invalid")));

		mp.put(ProcessName.LB_ATTRIBUTE_SET_G, Map.ofEntries(
				Map.entry("ROW 2", "Site A Lat / Site A Long|*| set Empty & set Invalid"),
				Map.entry("ROW 3", "Site A Lat / Site A Long|*| set Invalid & set Empty"),
				Map.entry("ROW 4", "Tx Site Elevation (m)|*|Set empty"),
				Map.entry("ROW 5", "Tx Site Elevation (m)|*|Set Invalid"),
				Map.entry("ROW 6", "BER10e6 Tx Power (dBm) / BER10e6 Rx Level (dBm)|*|Set both empty"),
				Map.entry("ROW 7", "BER10e6 Tx Power (dBm) / BER10e6 Rx Level (dBm)|*|set both invalid"),
				Map.entry("ROW 8", "Tx Ant. Azimuth (°)	/ Tx Ant. Height (m)|*|set both empty"),
				Map.entry("ROW 9", "Tx Ant. Azimuth (°)	/ Tx Ant. Height (m)|*|set both Invalid"),
				Map.entry("ROW 10", "Tx Antenna	/ Tx Ant. Gain (dB)	/ BER10e6 EIRP (dBm)|*|all set empty"),
				Map.entry("ROW 11", "Tx Antenna	/ Tx Ant. Gain (dB)	/ BER10e6 EIRP (dBm)|*|all set invalid"),
				Map.entry("ROW 12", "Site B Lat / Site B Long / Rx Site Elevation (m)|*|all set empty"),
				Map.entry("ROW 13", "Site B Lat / Site B Long / Rx Site Elevation (m)|*|all set invalid"),
				Map.entry("ROW 14",
						"Rx Ant. Azimuth (°) / Rx Ant. Height (m) / Rx Antenna / Rx Ant. Gain (dB)|*|set all empty"),
				Map.entry("ROW 15",
						"Rx Ant. Azimuth (°) / Rx Ant. Height (m) / Rx Antenna / Rx Ant. Gain (dB)|*|set all Invalid"),
				Map.entry("ROW 16",
						"Distance (km) / Bandwidth (MHz) / BER10e6 Eff. Margin (dB)	/ BER10e6 Rain Interference Thr. Deg. (dB)|*|Set all empty"),
				Map.entry("ROW 17",
						"Distance (km) / Bandwidth (MHz) / BER10e6 Eff. Margin (dB)	/ BER10e6 Rain Interference Thr. Deg. (dB)|*|Set all invalid")));

		mp.put(ProcessName.LB_ATTRIBUTE_SET_CIRCLE,
				Map.ofEntries(Map.entry("ROW 2", "Circle|*| Take 3 plans and set value [empty, invalid, UM]")));

		return mp;

	}

}
