package com.avendum.midsautomate.selenium.TestGenerator;

import com.avendum.midsautomate.selenium.seleniumconfig.Base;
import com.avendum.midsautomate.selenium.seleniumpages.Login;
import com.avendum.midsautomate.selenium.utils.PanelTraverser;
import com.avendum.midsautomate.selenium.utils.PlanIdGetter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.usermodel.WorkbookFactory;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class PlanUploadSheetGenerator {

    private static final Logger logger = Logger.getLogger(PlanUploadSheetGenerator.class.getName());
    public PlanUploadSheetGenerator() {
        // Constructor
        this.polarization=getPolarization();
        this.modemCodeSite=modemCodeMaped();
        this.mwChasisMap= mwChasisMaped();
        this.validHopTypes=getMapedHopeType();
        this.bandWidth=getMapBandWidth();
    }

    // Nominal Aop Generator eg FY25_26
    public String getNominalAop() {
        Date date = new Date();
        int year = date.getYear() + 1900;
        int nextYear = year + 1;
        return "FY" + (year % 100) + "_" + (nextYear % 100);
    }
    //Nominal Quarter  Q1, Q2, Q3, Q4
    //Q1- 1 Apr to June 30
    //Q2- 1 July to Sep 30
    //Q3- 1 Oct to Dec 31
    //Q4- 1 Jan to Mar 31
    public String getNominalQuarter() {
        Date date = new Date();
        int month = date.getMonth() + 1;
        if (month >= 4 && month <= 6) {
            return "Q1";
        } else if (month >= 7 && month <= 9) {
            return "Q2";
        } else if (month >= 10 && month <= 12) {
            return "Q3";
        } else {
            return "Q4";
        }
    }

    //Plan Category
    String planCategory[]={"new deployment hybrid/fo","New Deployment XPIC","MIMO Upgrade","Existing 1+0 to XPIC Upgrade","capacity upgrade soft/dc","1+0 to 1 Gig Upgrade","Dual XPIC Upgrade","E Band","Frequency Change","Card Upgrade"};

    //Network Re-Engineering, Junoon 2, New Rollout, NEW ROLLOUT_ULS, Relocation, E-Band, USOF (1+0),USOF (XPIC), MW utilization & TWAMP, Owner Issue, LOS Block, Site Locked, RSL Issue, Frequency Change, Card Upgrade, UBR to Microwave Upgrade, E-Band SDB, MW Link Swap
    String finalProjects[]={"Network Re-Engineering","Junoon 2","New Rollout","NEW ROLLOUT_ULS","Relocation","E-Band","USOF (1+0)","USOF (XPIC)","MW utilization & TWAMP","Owner Issue","LOS Block","Site Locked","RSL Issue","Frequency Change","Card Upgrade","UBR to Microwave Upgrade","E-Band SDB"};//"MW Link Swap"

    //Plan status
    String planStatus[]={"Deployment done","Deployment Pending"};

    //Plan Stage Left Empty(Auto fill)

    //Deploement Date/Moth (Auto fill)

    //Equipment make
    String equipmentMake[]={"Ceragon","Huawei","Ericsson","Aviat"};//"NOKIA","NEC","Radwin"

    //Nomenclature should not be empty and link id can be any value
    String hopNomenclatureA="Test_A_Nomenclature";
    String hopNomenclatureB="Test_B_Nomenclature";

    //Important
    String circle="JK";

    //Channel taken fixed for testing
    String channel="JK_70G_250M_F1";

    private Map<String, List<String>> polarization;
    //Polarization H or V
    public Map<String, List<String>> getPolarization() {
        Map<String, List<String>> hopeTypePolarisationMap = new HashMap<>();
        hopeTypePolarisationMap.put("1+0".toLowerCase(), Arrays.asList("H","V"));
        hopeTypePolarisationMap.put("1+0 DC".toLowerCase(), Arrays.asList("H","V"));
        hopeTypePolarisationMap.put("XPIC".toLowerCase(), Arrays.asList("H+V"));
        hopeTypePolarisationMap.put("XPIC+DC".toLowerCase(), Arrays.asList("H+V"));
        hopeTypePolarisationMap.put("D-XPIC".toLowerCase(), Arrays.asList("H+V"));
        hopeTypePolarisationMap.put("D-XPIC+DC".toLowerCase(), Arrays.asList("H+V"));
        hopeTypePolarisationMap.put("MIMO".toLowerCase(), Arrays.asList("H","V"));
        hopeTypePolarisationMap.put("E Band".toLowerCase(), Arrays.asList("H","V"));
        hopeTypePolarisationMap.put("E-Band SDB".toLowerCase(), Arrays.asList("H","V"));
        hopeTypePolarisationMap.put("E-Band SDB(New)".toLowerCase(), Arrays.asList("H","V"));
        hopeTypePolarisationMap.put("E-Band SDB(Existing)".toLowerCase(), Arrays.asList("H","V"));
        hopeTypePolarisationMap.put("Hard Upgrade XPIC".toLowerCase(), Arrays.asList("H+V"));
        hopeTypePolarisationMap.put("Soft XPIC".toLowerCase(), Arrays.asList("H+V"));
        hopeTypePolarisationMap.put("Soft DC".toLowerCase(), Arrays.asList("H","V","H+V"));
        return hopeTypePolarisationMap;
    }
    public String getPolarization(String projectCategory) {
        List<String> polarizations = polarization.get(projectCategory.toLowerCase());
        if (polarizations != null && !polarizations.isEmpty()) {
            Random random = new Random();
            return polarizations.get(random.nextInt(polarizations.size())); // Return a random polarization
        } else {
            return "Invalid Project Category";
        }
    }
    //For site A
    String sitALat="34.050466";
    String siteALong="74.343606";
    String txElevation="3820.64";
    String ber10e6TxPower="24";
    String txFrequency="81250";
    String ber10e6RxLevel="-34.15";
    String txAntAzimuth="180.6";//(0-360) till one decimal
    String txAntHeight="30";
    String txAntenna[]={"0.3","0.6","0.9","1.2","1.8","2.4","3.0"};
    String txAntGain="41.1";
    String getBer10e6EIRP="56.1";



    //For site B
    String siteBLat="34.050466";
    String siteBLong="74.343606";
    String rxElevation="3944";
    String rxFrequency="71250";
    String rxAntAzimuth="360";//(0-360
    String rxAntHeight="30";
    String rxAntenna[]={"0.3","0.6","0.9","1.2","1.8","2.4","3.0"};
    List<String> fiberPopMwChasisList=Arrays.asList("MUX","EPT","NPT","CSR","CEN");
    public String getFiberPopMwChasis(String popID) {
        if(popID.equals("Yes")){
            return "MUX";
        }else{
            return "General";
        }
    }
    String rxAntGain="41.1";
    public Map<String,String[]> getValidTxRadio(String equipmentMake) {
        Map<String, String[]> txRadio = new HashMap<>();
        if (equipmentMake.equalsIgnoreCase("Ceragon")) {
            txRadio.put("IP20", new String[]{"IP20", "IP50"});
        } else if (equipmentMake.equalsIgnoreCase("Aviat")) {
            txRadio.put("W", new String[]{"W", "WT"});
        } else if (equipmentMake.equalsIgnoreCase("Ericsson")) {
            txRadio.put("ERI_", new String[]{"ERI_", "ML"});
        } else if (equipmentMake.equalsIgnoreCase("Huawei")) {
            txRadio.put("XMC", new String[]{"XMC"});
        } else if (equipmentMake.equalsIgnoreCase("Nokia")) {
            txRadio.put("Any value", new String[]{"Any value"});
        } else {
            txRadio.put("Invalid Equipment Make", new String[]{"Invalid Equipment Make"});
        }
        return txRadio;
    }
    private String distance="5.7";
    private Map<String,String> bandWidth;
    private  Map<String,String> getMapBandWidth(){
        Map<String, String> hopeTypeBandWidthMap = new HashMap<>();
        hopeTypeBandWidthMap.put("1+0".toLowerCase(), "28");
        hopeTypeBandWidthMap.put("1+0 DC".toLowerCase(), "56");
        hopeTypeBandWidthMap.put("XPIC".toLowerCase(), "28");
        hopeTypeBandWidthMap.put("XPIC+DC".toLowerCase(), "56");
        hopeTypeBandWidthMap.put("D-XPIC".toLowerCase(), "28");
        hopeTypeBandWidthMap.put("D-XPIC+DC".toLowerCase(), "56");
        hopeTypeBandWidthMap.put("MIMO".toLowerCase(), "28");
        hopeTypeBandWidthMap.put("E Band".toLowerCase(), "250");
        hopeTypeBandWidthMap.put("E-Band SDB".toLowerCase(), "250");
        hopeTypeBandWidthMap.put("E-Band SDB(New)".toLowerCase(), "250");
        hopeTypeBandWidthMap.put("E-Band SDB(Existing)".toLowerCase(), "250");
        hopeTypeBandWidthMap.put("Hard Upgrade XPIC".toLowerCase(), "28/56");
        hopeTypeBandWidthMap.put("Soft XPIC".toLowerCase(), "28/56");
        hopeTypeBandWidthMap.put("Soft DC".toLowerCase(), "56");
        return hopeTypeBandWidthMap;
    }
    public String getBandW(String hop){
        return bandWidth.get(hop.toLowerCase());
    }
    //Valid Hop Types
    Map<String, List<String>> validHopTypes;
    public Map<String,List<String>> getMapedHopeType(){
        Map<String, List<String>> validHopTypes = new HashMap<>();
        validHopTypes.put("new deployment hybrid/fo".toLowerCase(), Arrays.asList("1+0", "1+0 DC"));
//        validHopTypes.put("FO".toLowerCase(), Arrays.asList("1+0", "1+0 DC"));
        validHopTypes.put("New Deployment XPIC".toLowerCase(), Arrays.asList("XPIC", "D-XPIC", "XPIC+DC"));
        validHopTypes.put("QAM Upgrade".toLowerCase(), Arrays.asList("QAM upgrade".toUpperCase()));
        validHopTypes.put("MIMO Upgrade".toLowerCase(), Arrays.asList("MIMO"));
        validHopTypes.put("Existing 1+0 to XPIC Upgrade".toLowerCase(), Arrays.asList("Hard Upgrade XPIC".toUpperCase()));
        validHopTypes.put("capacity upgrade soft/dc".toLowerCase(), Arrays.asList("Soft XPIC".toUpperCase(), "SOFT DC"));
        validHopTypes.put("1+0 to 1 Gig Upgrade".toLowerCase(), Arrays.asList("D-XPIC", "D-XPIC+DC", "XPIC+DC"));
        validHopTypes.put("Rerouting".toLowerCase(), Arrays.asList("1+0", "1+0 DC", "XPIC", "D-XPIC", "XPIC+DC", "D-XPIC+DC", "MIMO", "E Band".toUpperCase(), "E-Band SDB".toUpperCase(), "Hard Upgrade XPIC".toUpperCase(), "Soft XPIC".toUpperCase(), "Soft DC".toUpperCase()));
        validHopTypes.put("Dual XPIC Upgrade".toLowerCase(), Arrays.asList("D-XPIC", "D-XPIC+DC"));
        validHopTypes.put("E Band".toLowerCase(), Arrays.asList("E BAND", "E-BAND SDB(NEW)","E-BAND SDB(EXISTING)"));
        validHopTypes.put("Frequency Change".toLowerCase(), Arrays.asList("1+0", "1+0 DC", "XPIC", "D-XPIC", "XPIC+DC", "D-XPIC+DC"));
        validHopTypes.put("Card Upgrade".toLowerCase(), Arrays.asList("1+0", "1+0 DC", "XPIC", "D-XPIC", "XPIC+DC", "D-XPIC+DC"));
        return validHopTypes;
    }
    public String getValidHop(String category) {
        List<String> hops = validHopTypes.get(category.toLowerCase());
        if (hops != null && !hops.isEmpty()) {
            Random random = new Random();
            return hops.get(random.nextInt(hops.size())); // Return a random hop type
        } else {
            return "Invalid Category";
        }
    }
    private double[] ber10e6EffMarginDb={20,20.1};
    private double[] BER10e6RainInterference={1,4,3.9};//1 to 4
    private int availability =120;
    private String acmStatus="Enable";
    private String getAcmMin(String equipmentMake) {
        String acmMin = "";
        if (equipmentMake.equalsIgnoreCase("Ericsson")) {
            acmMin = "4QAM";
        } else {
            acmMin = "QPSK";
        }
        return acmMin;
    }
    private String getAcmMax(String projectCategory) {
        if (projectCategory.equalsIgnoreCase("E band")) {
            return "64QAM";
        } else {
            // Possible options
            String[] options = {"32QAM", "64QAM", "128QAM", "256QAM", "512QAM", "1024QAM", "2048QAM", "4096QAM"};
            Random random = new Random();
            return options[random.nextInt(options.length)]; // Return a random option
        }
    }

    private String radioCodeLow="1234abc-XMC";
    private String radioCodeHigh="1234abc-XMC";


    //Modem Code site A,MDU Code Site-A, Chassis Code Site-A same for Site B Antenna Code Site B



    public String getATPCStatus(String equipmentMake) {
        if(equipmentMake.equalsIgnoreCase("Ericsson") || equipmentMake.equalsIgnoreCase("Ceragon") || equipmentMake.equalsIgnoreCase("Huawei")) {
            return "Disable";
        }else{
            return "Enable";
        }
    }
    public String getATPCMin(String equipmentMake) {
        if(equipmentMake.equalsIgnoreCase("Ericsson") || equipmentMake.equalsIgnoreCase("Ceragon") || equipmentMake.equalsIgnoreCase("Huawei")) {
            return "NA";
        }else{
            return "16.4";
        }
    }

    public String getATPCMax(String equipmentMake) {
        if(equipmentMake.equalsIgnoreCase("Ericsson") || equipmentMake.equalsIgnoreCase("Ceragon") || equipmentMake.equalsIgnoreCase("Huawei")) {
            return "NA";
        }else{
            return "18";
        }
    }

    //(MODEM(IF Card)/Ethernet Port) Site-A/B
    private Map<String,String[]>  modemCodeSite;
    public Map<String,String[]> modemCodeMaped(){
        Map<String,String[]> map=new HashMap<>();
        map.put("Aviat",new String[]{"WTM 4800","IP-50CX","WTM 4200","WT420"});
        map.put("Ericsson",new String[]{"ML6363","MINI-LINK 6651/2","MINI-LINK 6366/4","ML6365","MMU2 H","MMU 1002","MMU4 A","MMU3 A"});
        map.put("Huawei",new String[]{"OPTIX RTN 320","Huawei_RTN_905","ISM6","ISV3","ISU2","ISM8"});
        map.put("Ceragon",new String[]{"IP-50CX","IP-20S","IP-20C","IP-50E","IP-20G","RMC-B"});
        return map;
    }
    //Modem Code Site and MDU Code are same
    public String getModemCodeSite(String equipmentMake) {
        List<String> modemCodes = Arrays.asList(modemCodeSite.get(equipmentMake));
        if (modemCodes != null && !modemCodes.isEmpty()) {
            Random random = new Random();
            return modemCodes.get(random.nextInt(modemCodes.size()));
        } else {
            return "Invalid Equipment Make";
        }
    }

    //(MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-A/B
    private Map<String,String[]>  mwChasisMap;
    public Map<String,String[]> mwChasisMaped(){
        Map<String,String[]> map=new HashMap<>();
        map.put("RMC-B",new String[]{"IP-20N 1RU", "IP-20N 2RU" , "IP-20A 1RU" , "IP-20A 2RU" , "IP-20GX"});
        map.put("MMU3 A",new String[]{"AMM 2P" , "AMM20p" , "MINI-LINK 6691" , "MINI-LINK 6692" , "AMM6PC" , "AMM20PB" , "AMM 6P" , "MINI-LINK 6693" , "AMM6pD" , "AMM 2P B"});
        map.put("MMU4 A",new String[]{"AMM6PC" , "AMM20p" , "AMM 2P"});
        map.put("MMU 1002",new String[]{"AMM20p","MINI-LINK 6693","MINI-LINK 6691","MINI-LINK 6692","AMM6PC","AMM20PB","AMM 2P B","AMM6pD","AMM 6P","AMM 2P"});
        map.put("MMU2 H",new String[]{"AMM6PC" , "AMM20p" , "AMM 2P"});
        map.put("WT420",new String[]{"BBU"});
        map.put("ISM6",new String[]{"OPTIX RTN 910","RTN950II","Optix RTN 950","RTN910II","OPTIX RTN 980"});
        map.put("ISV3",new String[]{"RTN910II","OPTIX RTN 910","OPTIX RTN 980","RTN950II","Optix RTN 950"});
        map.put("ISU2",new String[]{"OPTIX RTN 980","OPTIX RTN 910","RTN950II","Optix RTN 950","RTN910II"});
        map.put("ISM8",new String[]{"OPTIX RTN 980","RTN950II","Optix RTN 950","OPTIX RTN 910","RTN910II"});
        return map;
    }
    public String getMwChasis(String name) {
        try {
            List<String> mwChasis = Arrays.asList(mwChasisMap.get(name));
            if (mwChasis != null && !mwChasis.isEmpty()) {
                Random random = new Random();
                return mwChasis.get(random.nextInt(mwChasis.size()));
            } else {
                return "Failed";
            }
        }catch (Exception e){
            return  "Failed";
        }
    }
    private String[] mwChasisNeworExisting={"New","Existing"};

    //Free Slot/Port Interface Detail Site A/B


    private String popID="TESTPRA1Y120905";//Sample Pop id
    private String getHopType(String projectCategory) {
        return projectCategory;
    }

    //Site A and B end Ip
    String ip4A="10.0.0.2";
    String ip4="10.0.1.0";
    String ip6A="2001:0db8:85a3:0000:0000:8a2e:0370:7334";
    String ip6="2001:0db8:85a3:0000:0000:8a2e:0370:7934";

    //Genertate sample from above data in format Circle	Nominal Aop	Nominal Quarter	Plan Release Date	Ageing	Ageing Range	Plan category(New Deployment hybrid/FO/New Deployment XPIC/MIMO Upgrade/Existing 1+0 to XPIC Upgrade/Capacity Upgrade soft/DC/1+0 to 1 Gig Upgrade/Dual XPIC Upgrade/E Band/Frequency Change/Card Upgrade)	Final Project	Plan status ( Deployment done / Deployment Pending )	Plan Stage	Deployment Date/Month	Equipment Make	Hop Nomenclature Site-A	Hop Nomenclature Site-B	Link ID	Channel	Polarization	Site ID-A	Site A Lat	Site A Long	Tx Site Elevation (m)	BER10e6 Tx Power (dBm)	Tx Frequency (MHz)	BER10e6 Rx Level (dBm)	Tx Ant. Azimuth (°)	Tx Ant. Height (m)	Tx Antenna	Tx Ant. Gain (dB)	BER10e6 EIRP (dBm)	Site ID -B	Site B Lat	Site B Long	Rx Site Elevation (m)	Rx Frequency (MHz)	Rx Ant. Azimuth (°)	Rx Ant. Height (m)	Rx Antenna	Rx Ant. Gain (dB)	Tx Radio	Distance (km)	Bandwidth (MHz)	BER10e6 Eff. Margin (dB)	BER10e6 Rain Interference Thr. Deg. (dB)	Availability	ACM Status	ACM Min QAM	ACM Max QAM	Radio Code (High)	Radio Code (Low)	Modem Code Site-A	MDU Code Site-A	Chassis Code Site-A	Antenna Code Site-A	Modem Code Site-B	MDU Code Site-B	Chassis Code Site-B	Antenna Code Site-B	ATPC Status	ATPC MIN	ATPC MAX	(MODEM(IF Card)/Ethernet Port) Site-A	(MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-A	MW Chasis New or Existing Site-A	Free Slot/Port Interface Detail Site A	(MODEM(IF Card)/Ethernet Port) Site-B	(MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-B	MW Chasis New or Existing Site-B	Free Slot/Port Interface Detail Site B	Hop Dismantle Y / N	Dismantle Hop ID	Dismantle Hop Type	Dismantle Hop Modem A	Dismantle Hop Chassis A	Antenna Size Site-A	Dismantle Hop Modem B	Dismantle Hop Chassis B	Antenna Size Site-B	Remarks	Fiber POP Id	PCM PATH	Hop Type	DCN RA Number	DCN VLAN	GATEWAY IP	Site A End IP	Site B End IP	Antenna Beam Width	Node ID Site A	Node ID Site B	MO Number Site A	MO Number Site B	For Site ID
//    public String generateSampleData() {
//            return "Hello";
//    }

    // Define headers
    String[] headers = {
            "Circle", "Nominal Aop", "Nominal Quarter", "Plan Release Date", "Ageing", "Ageing Range",
            "Plan category(New Deployment hybrid/FO/New Deployment XPIC/MIMO Upgrade/Existing 1+0 to XPIC Upgrade/Capacity Upgrade soft/DC/1+0 to 1 Gig Upgrade/Dual XPIC Upgrade/E Band/Frequency Change/Card Upgrade)", "Final Project", "Plan status ( Deployment done / Deployment Pending )", "Plan Stage", "Deployment Date/Month",
            "Equipment Make", "Hop Nomenclature Site-A", "Hop Nomenclature Site-B", "Link ID", "Channel",
            "Polarization", "Site ID-A", "Site A Lat", "Site A Long", "Tx Site Elevation (m)",
            "BER10e6 Tx Power (dBm)", "Tx Frequency (MHz)", "BER10e6 Rx Level (dBm)", "Tx Ant. Azimuth (°)",
            "Tx Ant. Height (m)", "Tx Antenna", "Tx Ant. Gain (dB)", "BER10e6 EIRP (dBm)", "Site ID -B",
            "Site B Lat", "Site B Long", "Rx Site Elevation (m)", "Rx Frequency (MHz)", "Rx Ant. Azimuth (°)",
            "Rx Ant. Height (m)", "Rx Antenna", "Rx Ant. Gain (dB)", "Tx Radio", "Distance (km)",
            "Bandwidth (MHz)", "BER10e6 Eff. Margin (dB)", "BER10e6 Rain Interference Thr. Deg. (dB)",
            "Availability", "ACM Status", "ACM Min QAM", "ACM Max QAM", "Radio Code (High)",
            "Radio Code (Low)", "Modem Code Site-A", "MDU Code Site-A", "Chassis Code Site-A",
            "Antenna Code Site-A", "Modem Code Site-B", "MDU Code Site-B", "Chassis Code Site-B",
            "Antenna Code Site-B", "ATPC Status", "ATPC MIN", "ATPC MAX", "(MODEM(IF Card)/Ethernet Port) Site-A",
            "(MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-A", "MW Chasis New or Existing Site-A",
            "Free Slot/Port Interface Detail Site A", "(MODEM(IF Card)/Ethernet Port) Site-B",
            "(MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-B", "MW Chasis New or Existing Site-B",
            "Free Slot/Port Interface Detail Site B", "Hop Dismantle Y / N", "Dismantle Hop ID",
            "Dismantle Hop Type", "Dismantle Hop Modem A", "Dismantle Hop Chassis A", "Antenna Size Site-A",
            "Dismantle Hop Modem B", "Dismantle Hop Chassis B", "Antenna Size Site-B", "Remarks",
            "Fiber POP Id", "PCM PATH", "Hop Type", "DCN RA Number", "DCN VLAN", "GATEWAY IP",
            "Site A End IP", "Site B End IP", "Antenna Beam Width", "Node ID Site A", "Node ID Site B",
            "MO Number Site A", "MO Number Site B", "For Site ID"
    };
    public String generateSampleData() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Plan Upload Sheet");

        // Create header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        int aa=planCategory.length;
        int bb=equipmentMake.length;
        int cc=planStatus.length;
        int dd=txAntenna.length;
        int ee=rxAntenna.length;
        int ff=ber10e6EffMarginDb.length;
        int gg=BER10e6RainInterference.length;
        int hh=finalProjects.length;
        int ii=fiberPopMwChasisList.size();
        boolean first=true;
        String tempLinkId[]=new String[2];


        while (aa-- > 0 || bb-- > 0 || cc-- > 0 || dd-- > 0 || ee-- > 0 || ff-- > 0 || gg-- > 0 || hh-- > 0 || ii-- > 0) {
            int count = (int) (Math.random() * 10000);
            String siteAId = "Test_A_ID" + new SimpleDateFormat("ddMMyy_HHmmssSSS").format(new Date()) + count;
            String siteBID = "Test_B_ID" + new SimpleDateFormat("ddMMyy_HHmmssSSS").format(new Date()) + (count + 1);
            if(first){
                tempLinkId[0]=siteAId;
                tempLinkId[1]=siteBID;
                first=false;
            }

            // Determine values for each parameter
            String plan_Category = (aa <= 0) ? planCategory[new Random().nextInt(planCategory.length - 1)] : planCategory[aa - 1];
            String equipment_Make = (bb <= 0) ? equipmentMake[new Random().nextInt(equipmentMake.length-1)] : equipmentMake[bb - 1];
            String plan_Status = (cc <= 0) ? planStatus[new Random().nextInt(planStatus.length-1)] : planStatus[cc - 1];
            String antenna_Size = (dd <= 0) ? txAntenna[new Random().nextInt(txAntenna.length-1)] : txAntenna[dd - 1];
            String rx_Antenna = (ee <= 0) ? rxAntenna[new Random().nextInt(rxAntenna.length-1)] : rxAntenna[ee - 1];
            String ber10e6_Eff_Margin = (ff <= 0) ? String.valueOf(ber10e6EffMarginDb[new Random().nextInt(ber10e6EffMarginDb.length-1)]) : String.valueOf(ber10e6EffMarginDb[ff - 1]);
            String ber10e6_Rain_Interference = (gg <= 0) ? String.valueOf(BER10e6RainInterference[new Random().nextInt(BER10e6RainInterference.length-1)]) : String.valueOf(BER10e6RainInterference[gg - 1]);
            String finalProj = (hh <= 0) ? finalProjects[new Random().nextInt(finalProjects.length-1)] : finalProjects[hh - 1];
            String aChais = (ii <= 0) ? fiberPopMwChasisList.get(new Random().nextInt(fiberPopMwChasisList.size()-1)) : fiberPopMwChasisList.get(ii - 1);
            String hop= getValidHop(plan_Category.toLowerCase());
            if(hop.equalsIgnoreCase("D-XPIC") || hop.equalsIgnoreCase("D-XPIC+DC") ){
                rxFrequency="71250/81250";
                txFrequency="81250/71250";//JK_70G_250M_F1
                channel="JK_70G_250M_F1/JK_70G_250M_F1";
            }else{
                rxFrequency="71250";
                txFrequency="81250";
                channel="JK_70G_250M_F1";
            }
            if(hop.equalsIgnoreCase("E-Band SDB(New)")){
                siteAId=tempLinkId[0];
                siteBID=tempLinkId[1];
            }
            String linkId = siteAId + "-" + siteBID;
            // Create a new row and populate cells
            Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
            createRowCells(row, siteAId, siteBID, linkId, plan_Category, equipment_Make, plan_Status, antenna_Size, rx_Antenna, ber10e6_Eff_Margin, ber10e6_Rain_Interference, finalProj, aChais,rxFrequency, txFrequency,hop);
        }
        //Add other special case where site id B is as pop Id then implement
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to file

        try (FileOutputStream fileOut = new FileOutputStream("excelDoc/PlanUploadSheetSampleData.xlsx")) { // ✅ Fixed extension
            workbook.write(fileOut);
            // Update the filename here too
            logger.info("Absolute path: " + new java.io.File("PlanUploadSheetSampleData.xlsx").getAbsolutePath());
            return "Sample data generated successfully in PlanUploadSheetSampleData.xlsx";
        } catch (IOException e) {
            return "Error generating sample data: " + e.getMessage();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                System.err.println("Error closing workbook: " + e.getMessage());
            }
        }

    }

    //Function to pass the value using parameter
    private void createRowCells(Row row, String siteAId, String siteBID, String linkId, String plan_Category, String equipment_Make, String plan_Status, String antenna_Size, String rx_Antenna, String ber10e6_Eff_Margin, String ber10e6_Rain_Interference, String finalProj, String aChais,String rxFreq,String txFreq,String hop) {
        row.createCell(0).setCellValue(circle); // Circle: JK
        row.createCell(1).setCellValue(getNominalAop()); // Nominal Aop: FY25_26
        row.createCell(2).setCellValue(getNominalQuarter()); // Nominal Quarter: Q1
        row.createCell(3).setCellValue(""); // Plan Release Date
        row.createCell(4).setCellValue(""); // Ageing
        row.createCell(5).setCellValue(""); // Ageing Range
        row.createCell(6).setCellValue(plan_Category); // Plan category
        row.createCell(7).setCellValue(finalProj); // Final Project
        row.createCell(8).setCellValue(plan_Status); // Plan status
        row.createCell(9).setCellValue(""); // Plan Stage
        row.createCell(10).setCellValue(""); // Deployment Date/Month
        row.createCell(11).setCellValue(equipment_Make); // Equipment Make
        row.createCell(12).setCellValue(hopNomenclatureA); // Hop Nomenclature Site-A
        row.createCell(13).setCellValue(hopNomenclatureB); // Hop Nomenclature Site-B
        row.createCell(14).setCellValue(linkId); // Link ID
        row.createCell(15).setCellValue(channel); // Channel
        row.createCell(16).setCellValue(getPolarization(hop)); // Polarization
        row.createCell(17).setCellValue(siteAId); // Site ID-A
        row.createCell(18).setCellValue(sitALat); // Site A Lat
        row.createCell(19).setCellValue(siteALong); // Site A Long
        row.createCell(20).setCellValue(txElevation); // Tx Site Elevation (m)
        row.createCell(21).setCellValue(ber10e6TxPower); // BER10e6 Tx Power (dBm)
        row.createCell(22).setCellValue(txFreq); // Tx Frequency (MHz)
        row.createCell(23).setCellValue(ber10e6RxLevel); // BER10e6 Rx Level (dBm)
        row.createCell(24).setCellValue(txAntAzimuth); // Tx Ant. Azimuth (°)
        row.createCell(25).setCellValue(txAntHeight); // Tx Ant. Height (m)
        row.createCell(26).setCellValue(antenna_Size); // Tx Antenna
        row.createCell(27).setCellValue(txAntGain); // Tx Ant. Gain (dB)
        row.createCell(28).setCellValue(getBer10e6EIRP); // BER10e6 EIRP (dBm)
        row.createCell(29).setCellValue(siteBID); // Site ID -B
        row.createCell(30).setCellValue(siteBLat); // Site B Lat
        row.createCell(31).setCellValue(siteBLong); // Site B Long
        row.createCell(32).setCellValue(rxElevation); // Rx Site Elevation (m)
        row.createCell(33).setCellValue(rxFreq); // Rx Frequency (MHz)
        row.createCell(34).setCellValue(rxAntAzimuth); // Rx Ant. Azimuth (°)
        row.createCell(35).setCellValue(rxAntHeight); // Rx Ant. Height (m)
        row.createCell(36).setCellValue(rx_Antenna); // Rx Antenna
        row.createCell(37).setCellValue(rxAntGain); // Rx Ant. Gain (dB)
        Map<String, String[]> txRadioMap = getValidTxRadio(equipment_Make);
        String txRadio = txRadioMap.values().iterator().next()[0];
        row.createCell(38).setCellValue(txRadio); // Tx Radio
        row.createCell(39).setCellValue(distance); // Distance (km)
        row.createCell(40).setCellValue(getBandW(hop)); // Bandwidth (MHz)
        row.createCell(41).setCellValue(ber10e6_Eff_Margin); // BER10e6 Eff. Margin (dB)
        row.createCell(42).setCellValue(ber10e6_Rain_Interference); // BER10e6 Rain Interference Thr. Deg. (dB)
        row.createCell(43).setCellValue(String.valueOf(availability)); // Availability
        row.createCell(44).setCellValue(acmStatus); // ACM Status
        row.createCell(45).setCellValue(getAcmMin(equipment_Make)); // ACM Min QAM
        row.createCell(46).setCellValue(getAcmMax(plan_Category)); // ACM Max QAM
        row.createCell(47).setCellValue(radioCodeHigh); // Radio Code (High)
        row.createCell(48).setCellValue(radioCodeLow); // Radio Code (Low)
        row.createCell(49).setCellValue("1234abc-XMC"); // Modem Code Site-A
        row.createCell(50).setCellValue("1234abc-XMC"); // MDU Code Site-A
        row.createCell(51).setCellValue("1234abc-XMC"); // Chassis Code Site-A
        row.createCell(52).setCellValue("1234abc-XMC"); // Antenna Code Site-A
        row.createCell(53).setCellValue("1234abc-XMC"); // Modem Code Site-B
        row.createCell(54).setCellValue("1234abc-XMC"); // MDU Code Site-B
        row.createCell(55).setCellValue("1234abc-XMC"); // Chassis Code Site-B
        row.createCell(56).setCellValue("1234abc-XMC"); // Antenna Code Site-B
        row.createCell(57).setCellValue(getATPCStatus(equipment_Make)); // ATPC Status
        row.createCell(58).setCellValue(getATPCMin(equipment_Make)); // ATPC MIN
        row.createCell(59).setCellValue(getATPCMax(equipment_Make)); // ATPC MAX
        String modemCardSiteA=getModemCodeSite(equipment_Make);
        if(!getMwChasis(modemCardSiteA).equalsIgnoreCase("Failed")){
            aChais= getMwChasis(modemCardSiteA);
        }
        row.createCell(60).setCellValue(modemCardSiteA); // (MODEM(IF Card)/Ethernet Port) Site-A
        row.createCell(61).setCellValue(aChais); // (MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-A
        row.createCell(62).setCellValue(new Random().nextInt(2)); // MW Chasis New or Existing Site-A
        row.createCell(63).setCellValue(""); // Free Slot/Port Interface Detail Site A
        String modemCardSiteB=getModemCodeSite(equipment_Make);
        String bChais="BBU";
        if(!getMwChasis(modemCardSiteB).equalsIgnoreCase("Failed")){
            bChais= getMwChasis(modemCardSiteB);
        }
        row.createCell(64).setCellValue(modemCardSiteB); // (MODEM(IF Card)/Ethernet Port) Site-B
        row.createCell(65).setCellValue(bChais); // (MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-B
        row.createCell(66).setCellValue(new Random().nextInt(2)); // MW Chasis New or Existing Site-B
        row.createCell(67).setCellValue(""); // Free Slot/Port Interface Detail Site B
        row.createCell(68).setCellValue(""); // Hop Dismantle Y / N
        row.createCell(69).setCellValue(""); // Dismantle Hop ID
        row.createCell(70).setCellValue(""); // Dismantle Hop Type
        row.createCell(71).setCellValue(""); // Dismantle Hop Modem A
        row.createCell(72).setCellValue(""); // Dismantle Hop Chassis A
        row.createCell(73).setCellValue(antenna_Size); // Antenna Size Site-A
        row.createCell(74).setCellValue(""); // Dismantle Hop Modem B
        row.createCell(75).setCellValue(""); // Dismantle Hop Chassis B
        row.createCell(76).setCellValue(rx_Antenna); // Antenna Size Site-B
        row.createCell(77).setCellValue(""); // Remarks
        row.createCell(78).setCellValue(siteAId); // Fiber POP Id
        row.createCell(79).setCellValue(siteAId + "|" + siteBID); // PCM PATH
        row.createCell(80).setCellValue(hop); // Hop Type
        row.createCell(81).setCellValue(""); // DCN RA Number
        row.createCell(82).setCellValue(""); // DCN VLAN
        row.createCell(83).setCellValue(""); // GATEWAY IP
        row.createCell(84).setCellValue(ip4A); // Site A End IP
        row.createCell(85).setCellValue(ip4); // Site B End IP
        row.createCell(86).setCellValue(""); // Antenna Beam Width
        row.createCell(87).setCellValue(""); // Node ID Site A
        row.createCell(88).setCellValue(""); // Node ID Site B
        row.createCell(89).setCellValue(""); // MO Number Site A
        row.createCell(90).setCellValue(""); // MO Number Site B
        row.createCell(91).setCellValue(""); // For Site ID
    }



//Generate sample data for wrong Data Testing
    private void wrongDataRowCells(Row row, String circle, String nominalAop, String nominalQuarter, String releaseDate, String ageing, String ageingRange, String planCategory, String finalProject, String planStatus, String planStage, String deploymentDate, String equipmentMake, String hopNomenclatureA, String hopNomenclatureB, String linkId, String channel, String polarization, String siteAId, String siteALat, String siteALong, String txElevation, String ber10e6TxPower, String txFrequency, String ber10e6RxLevel, String txAntAzimuth, String txAntHeight, String antennaSize, String txAntGain, String ber10e6EIRP, String siteBID, String siteBLat, String siteBLong, String rxElevation, String rxFrequency, String rxAntAzimuth, String rxAntHeight, String rxAntenna, String rxAntGain, String txRadio, String distance, String bandwidth, String ber10e6EffMargin, String ber10e6RainInterference, String availability, String acmStatus, String acmMinQAM, String acmMaxQAM, String radioCodeHigh, String radioCodeLow, String modemCodeSiteA, String mduCodeSiteA, String chassisCodeSiteA, String antennaCodeSiteA, String modemCodeSiteB, String mduCodeSiteB, String chassisCodeSiteB, String antennaCodeSiteB, String atpcStatus, String atpcMin, String atpcMax, String modemCardSiteA, String mwChasisSiteA, String mwChasisNewOrExistingSiteA, String freeSlotPortInterfaceDetailSiteA, String modemCardSiteB, String mwChasisSiteB, String mwChasisNewOrExistingSiteB, String freeSlotPortInterfaceDetailSiteB,String hopDismantleYN,String dismantleHopId,String dismantleHopType,String dismantleHopModemA,String dismantleHopChassisA,String antennaSizeSiteA,String dismantleHopModemB,String dismantleHopChassisB,String antennaSizeSiteB,String remark,String fiberPopId,String pcmPath,String hopType,String dcnRaNumber,String dcnVlan,String gatewayIp,String siteAEndIp,String siteBEndIp,String antennaBeamWidth,String nodeIdSiteA,String nodeIdSiteB,String moNumberSiteA,String moNumberSiteB,String forSiteId) {
        row.createCell(0).setCellValue(circle); // Circle
        row.createCell(1).setCellValue(nominalAop); // Nominal Aop
        row.createCell(2).setCellValue(nominalQuarter); // Nominal Quarter
        row.createCell(3).setCellValue(releaseDate); // Plan Release Date
        row.createCell(4).setCellValue(ageing); // Ageing
        row.createCell(5).setCellValue(ageingRange); // Ageing Range
        row.createCell(6).setCellValue(planCategory); // Plan category
        row.createCell(7).setCellValue(finalProject); // Final Project
        row.createCell(8).setCellValue(planStatus); // Plan status
        row.createCell(9).setCellValue(planStage); // Plan Stage
        row.createCell(10).setCellValue(deploymentDate); // Deployment Date/Month
        row.createCell(11).setCellValue(equipmentMake); // Equipment Make
        row.createCell(12).setCellValue(hopNomenclatureA); // Hop Nomenclature Site-A
        row.createCell(13).setCellValue(hopNomenclatureB); // Hop Nomenclature Site-B
        row.createCell(14).setCellValue(linkId); // Link ID
        row.createCell(15).setCellValue(channel); // Channel
        row.createCell(16).setCellValue(polarization); // Polarization
        row.createCell(17).setCellValue(siteAId); // Site ID-A
        row.createCell(18).setCellValue(siteALat); // Site A Lat
        row.createCell(19).setCellValue(siteALong); // Site A Long
        row.createCell(20).setCellValue(txElevation); // Tx Site Elevation (m)
        row.createCell(21).setCellValue(ber10e6TxPower); // BER10e6 Tx Power (dBm)
        row.createCell(22).setCellValue(txFrequency); // Tx Frequency (MHz)
        row.createCell(23).setCellValue(ber10e6RxLevel); // BER10e6 Rx Level (dBm)
        row.createCell(24).setCellValue(txAntAzimuth); // Tx Ant. Azimuth (°)
        row.createCell(25).setCellValue(txAntHeight); // Tx Ant. Height (m)
        row.createCell(26).setCellValue(antennaSize); // Tx Antenna
        row.createCell(27).setCellValue(txAntGain); // Tx Ant. Gain (dB)
        row.createCell(28).setCellValue(ber10e6EIRP); // BER10e6 EIRP (dBm)
        row.createCell(29).setCellValue(siteBID); // Site ID -B
        row.createCell(30).setCellValue(siteBLat); // Site B Lat
        row.createCell(31).setCellValue(siteBLong); // Site B Long
        row.createCell(32).setCellValue(rxElevation); // Rx Site Elevation (m)
        row.createCell(33).setCellValue(rxFrequency); // Rx Frequency (MHz)
        row.createCell(34).setCellValue(rxAntAzimuth); // Rx Ant. Azimuth (°)
        row.createCell(35).setCellValue(rxAntHeight); // Rx Ant. Height (m)
        row.createCell(36).setCellValue(rxAntenna); // Rx Antenna
        row.createCell(37).setCellValue(rxAntGain); // Rx Ant. Gain (dB)
        row.createCell(38).setCellValue(txRadio); // Tx Radio
        row.createCell(39).setCellValue(distance); // Distance (km)
        row.createCell(40).setCellValue(bandwidth); // Bandwidth (MHz)
        row.createCell(41).setCellValue(ber10e6EffMargin); // BER10e6 Eff. Margin (dB)
        row.createCell(42).setCellValue(ber10e6RainInterference); // BER10e6 Rain Interference Thr. Deg. (dB)
        row.createCell(43).setCellValue(availability); // Availability
        row.createCell(44).setCellValue(acmStatus); // ACM Status
        row.createCell(45).setCellValue(acmMinQAM); // ACM Min QAM
        row.createCell(46).setCellValue(acmMaxQAM); // ACM Max QAM
        row.createCell(47).setCellValue(radioCodeHigh); // Radio Code (High)
        row.createCell(48).setCellValue(radioCodeLow); // Radio Code (Low)
        row.createCell(49).setCellValue(modemCodeSiteA); // Modem Code Site-A
        row.createCell(50).setCellValue(mduCodeSiteA); // MDU Code Site-A
        row.createCell(51).setCellValue(chassisCodeSiteA); // Chassis Code Site-A
        row.createCell(52).setCellValue(antennaCodeSiteA); // Antenna Code Site-A
        row.createCell(53).setCellValue(modemCodeSiteB); // Modem Code Site-B
        row.createCell(54).setCellValue(mduCodeSiteB); // MDU Code Site-B
        row.createCell(55).setCellValue(chassisCodeSiteB); // Chassis Code Site-B
        row.createCell(56).setCellValue(antennaCodeSiteB); // Antenna Code Site-B
        row.createCell(57).setCellValue(atpcStatus); // ATPC Status
        row.createCell(58).setCellValue(atpcMin); // ATPC MIN
        row.createCell(59).setCellValue(atpcMax); // ATPC MAX
        row.createCell(60).setCellValue(modemCardSiteA); // (MODEM(IF Card)/Ethernet Port) Site-A
        row.createCell(61).setCellValue(mwChasisSiteA); // (MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-A
        row.createCell(62).setCellValue(mwChasisNewOrExistingSiteA); // MW Chasis New or Existing Site-A
        row.createCell(63).setCellValue(freeSlotPortInterfaceDetailSiteA); // Free Slot/Port Interface Detail Site A
        row.createCell(64).setCellValue(modemCardSiteB); // (MODEM(IF Card)/Ethernet Port) Site-B
        row.createCell(65).setCellValue(mwChasisSiteB); // (MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-B
        row.createCell(66).setCellValue(mwChasisNewOrExistingSiteB); // MW Chasis New or Existing Site-B
        row.createCell(67).setCellValue(freeSlotPortInterfaceDetailSiteB); // Free Slot/Port Interface Detail Site B
        row.createCell(68).setCellValue(hopDismantleYN); // Hop Dismantle Y / N
        row.createCell(69).setCellValue(dismantleHopId); // Dismantle Hop ID
        row.createCell(70).setCellValue(dismantleHopType); // Dismantle Hop Type
        row.createCell(71).setCellValue(dismantleHopModemA); // Dismantle Hop Modem A
        row.createCell(72).setCellValue(dismantleHopChassisA); // Dismantle Hop Chassis A
        row.createCell(73).setCellValue(antennaSizeSiteA); // Antenna Size Site-A
        row.createCell(74).setCellValue(dismantleHopModemB); // Dismantle Hop Modem B
        row.createCell(75).setCellValue(dismantleHopChassisB); // Dismantle Hop Chassis B
        row.createCell(76).setCellValue(antennaSizeSiteB); // Antenna Size Site-B
        row.createCell(77).setCellValue(remark); // Remarks
        row.createCell(78).setCellValue(fiberPopId); // Fiber POP Id
        row.createCell(79).setCellValue(pcmPath); // PCM PATH
        row.createCell(80).setCellValue(hopType); // Hop Type
        row.createCell(81).setCellValue(dcnRaNumber); // DCN RA Number
        row.createCell(82).setCellValue(dcnVlan); // DCN VLAN
        row.createCell(83).setCellValue(gatewayIp); // GATEWAY IP
        row.createCell(84).setCellValue(siteAEndIp); // Site A End IP
        row.createCell(85).setCellValue(siteBEndIp); // Site B End IP
        row.createCell(86).setCellValue(antennaBeamWidth); // Antenna Beam Width
        row.createCell(87).setCellValue(nodeIdSiteA); // Node ID Site A
        row.createCell(88).setCellValue(nodeIdSiteB); // Node ID Site B
        row.createCell(89).setCellValue(moNumberSiteA); // MO Number Site A
        row.createCell(90).setCellValue(moNumberSiteB); // MO Number Site B
        row.createCell(91).setCellValue(forSiteId); // For Site ID
    }


    public void generateWrongSample(){
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Wrong Data Plan Upload Sheet");
        // Create header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        // Generate wrong data rows
        // JK	FY25_26	Q1				E Band	UBR to Microwave Upgrade	Deployment Pending			AVIAT	TESTPRA1Y120905	TESTPRA12Z230905	TESTPRA1Y120905-TESTPRA12Z230905	JK_70G_250M_F1	V	TESTPRA1Y120905	34.050466	74.262428	3820.64	24	81250	-34.15	180.6	30	0.9	41.1	56.1	TESTPRA12Z230905	33.99908	74.26221	3944	71250	360	30	0.9	41.1	W4800_250M_1120	5.7	250	24	4.0	23	Enable	QPSK	64QAM	1234abc-XMC	1234abc-XMC	1234abc-XMC	1234abc-XMC	1234abc-XMC	1234abc-XMC	1234abc-XMC	1234abc-XMC	1234abc-XMC	1234abc-XMC	Enable	NA	NA	WTM 4800	MUX	New		WTM 4800	BBU	New		N										TESTPRA1Y120905	TESTPRA1Y120905|TESTPRA12Z230905	E BAND				10.0.0.0	10.0.0.1	1.8	MWJKCHM947	MWJKHMT948

        Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        //Wrong Format Data Uploading and Testing
//      wrongDataRowCells(row, circle, getNominalAop(), getNominalQuarter(), "Wrong Data", "Wrong Data", "Wrong Data", "E Band", "UBR to Microwave Upgrade", "Deployment Pending", "Wrong Data", "Wrong Data", "AVIAT", "Wrong Data", "Wrong Data", "TESTPRA1Y120905-TESTPRA12Z230905", "JK_70G_250M_F1", "V", "TESTPRA1Y120905", "34.050466", "74.262428", "Wrong data", "Wrong Data", "81250", "Wrong Data", "180.6", "Wrong Data", "0.9", "Wrong Data", "56.1", "TESTPRA12Z230905", "33.99908", "74.26221", "Wrong Data", "71250", "360", "Wrong Data", "0.9", "Wrong Data", "Wrong Data", "Wrong Data", "250", "24", "4.0", "Wrong Data", "Enable", "QPSK", "64QAM", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Enable", "Wrong Data", "Wrong Data", "WTM 4800", "MUX", "Wrong Data", "Wrong Data", "WTM 4800", "BBU", "Wrong Data","WTM 4800", "Wrong Data", "Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","TESTPRA1Y120905","TESTPRA1Y120905|TESTPRA12Z230905","E BAND","Wrong Data","Wrong Data","Wrong Data","10.0.0.0","10.0.0.1","1.8","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data");
        wrongDataRowCells(row, circle, "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "E Band", "UBR to Microwave Upgrade", "Deployment Pending", "Wrong Data", "Wrong Data", "AVIAT", "Wrong Data", "Wrong Data", "TESTPRA1Y120905-TESTPRA12Z230905", "Wrong Data", "Wrong Data", "TESTPRA1Y120905", "Wrong Data", "Wrong Data", "Wrong data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "TESTPRA12Z230905", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data","Wrong Data", "Wrong Data", "Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","TESTPRA1Y120905","TESTPRA1Y120905|TESTPRA12Z230905","E BAND","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data");
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        wrongDataRowCells(row, circle, getNominalAop(), getNominalQuarter(), "", "", "", "E Band", "UBR to Microwave Upgrade", "Deployment Pending", "", "", "AVIAT", "", "", "TESTPRA1Y120905-TESTPRA12Z230905", "", "", "TESTPRA1Y120905", "", "", "", "", "", "", "", "", "", "", "", "TESTPRA12Z230905", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "","", "", "","","","","","","","","","TESTPRA1Y12090","TESTPRA1Y120905|TESTPRA12Z230905","E BAND","","","","","","","","","","","");
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        wrongDataRowCells(row, circle, getNominalAop(), getNominalQuarter(), "", "", "", "E Band", "Wrong Project Naam", "Wrong  ", "", "", "AVIAT", "Any Value", "Any Value", "TESTPRA1Y120905-TESTPRA12Z230905", "Wrong Channel", "V", "TESTPRA1Y120905", "34.050466", "74.262428", "Wrong data", "Wrong Data", "81250", "Wrong Data", "180.6", "Wrong Data", "0.9", "Wrong Data", "56.1", "TESTPRA12Z230905", "33.99908", "74.26221", "Wrong Data", "71250", "360", "Wrong Data", "0.9", "Wrong Data", "Wrong Data", "Wrong Data", "250", "24", "4.0", "Wrong Data", "Enable", "QPSK", "64QAM", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Enable", "Wrong Data", "Wrong Data", "WTM 4800", "MUX", "Wrong Data", "Wrong Data", "WTM 4800", "BBU", "Wrong Data","WTM 4800", "Wrong Data", "Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","","","Wrong Data","","TESTPRA1Ok","Mera Naam Joker|TESTPRA12Z230905","E BAND","","","Wrong Data","10.0.0.0","10.0.0.1","1.8","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data");

//        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
//        wrongDataRowCells(row, circle, getNominalAop(), getNominalQuarter(), "Wrong Data", "Wrong Data", "Wrong Data", "E Band", "UBR to Microwave Upgrade", "Deployment Pending", "Wrong Data", "Wrong Data", "AVIAT", "Wrong Data", "Wrong Data", "TESTPRA1Y120905-TESTPRA12Z230905", "JK_70G_250M_F1", "V", "TESTPRA1Y120905", "34.050466", "74.262428", "Wrong data", "Wrong Data", "81250", "Wrong Data", "180.6", "Wrong Data", "0.9", "Wrong Data", "56.1", "TESTPRA12Z230905", "33.99908", "74.26221", "Wrong Data", "71250", "360", "Wrong Data", "0.9", "Wrong Data", "Wrong Data", "Wrong Data", "250", "24", "4.0", "Wrong Data", "Enable", "QPSK", "64QAM", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Enable", "Wrong Data", "Wrong Data", "WTM 4800", "MUX", "Wrong Data", "Wrong Data", "WTM 4800", "BBU", "Wrong Data","WTM 4800", "Wrong Data", "Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data","TESTPRA1Y120905","TESTPRA1Y120905|TESTPRA12Z230905","E BAND","Wrong Data","Wrong Data","Wrong Data","10.0.0.0","10.0.0.1","1.8","Wrong Data","Wrong Data","Wrong Data","Wrong Data","Wrong Data");


        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream("excelDoc/WrongDataPlanUploadSheetSampleData.xlsx")) { // ✅ Fixed extension
            workbook.write(fileOut);
            // Update the filename here too
            logger.info("Absolute path: " + new java.io.File("excelDoc/WrongDataPlanUploadSheetSampleData.xlsx").getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error generating wrong sample data: " + e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                System.err.println("Error closing workbook: " + e.getMessage());
            }
        }
    }
    public String planSuccessSheetPath(){
        try {
            String path = new java.io.File("excelDoc/PlanUploadSheetSampleData.xlsx").getAbsolutePath();
        }catch (Exception e){
            generateSampleData();
        }
        return  new java.io.File("excelDoc/PlanUploadSheetSampleData.xlsx").getAbsolutePath();
    }

    public String planFailedSheetPath() {
        try {
            String path = new java.io.File("excelDoc/WrongDataPlanUploadSheetSampleData.xlsx").getAbsolutePath();
        } catch (Exception e) {
            generateWrongSample();
        }
        return new java.io.File("excelDoc/WrongDataPlanUploadSheetSampleData.xlsx").getAbsolutePath();
    }


    //MW Links Id special Case Data Generation in Which Plan Id generate only when Link Id >= AT Accepted
    public void generateMWLinkDataTable(String username,String password) throws InterruptedException {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Plan Upload Sheet");
            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            //Get the Link Id status>=AT Accepted
            Login login = new Login();
            login.Login(username, password);
            PanelTraverser panelTraverser=new PanelTraverser();
            panelTraverser.navigateToMWPlanTrackingPage(Base.getDriver());
            PlanIdGetter planIdGetter = new PlanIdGetter();
            String atAcptedLinkId=planIdGetter.linkId("AT ACCEPTED");
            String trafficLinkId=planIdGetter.linkId("TS Completed");

            if (atAcptedLinkId == null || trafficLinkId == null) {
                logger.info("No Link Id found with status AT Accepted or TS Completed");
                return;
            }
            String[] tempAtLinkId = atAcptedLinkId.split("-");
            String[] tempTrafficLinkId = trafficLinkId.split("-");
            Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
            //It just function for implementing row
            wrongDataRowCells(row, circle, getNominalAop(), getNominalQuarter(), "", "", "", "E Band", "UBR to Microwave Upgrade", "Deployment Pending", "", "", "AVIAT", "Nome A", "Nome B", atAcptedLinkId, "JK_70G_250M_F1", "V", tempAtLinkId[0], "34.050466", "74.262428", "23", "12", "81250", "Wrong Data", "180.6", "Wrong Data", "0.9", "Wrong Data", "56.1", tempAtLinkId[1], "33.99908", "74.26221", "Wrong Data", "71250", "360", "Wrong Data", "0.9", "Wrong Data", "Wrong Data", "Wrong Data", "250", "24", "4.0", "Wrong Data", "Enable", "QPSK", "64QAM", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Enable", "Wrong Data", "Wrong Data", "WTM 4800", "MUX", "Wrong Data", "Wrong Data", "WTM 4800", "BBU", "Wrong Data", "WTM 4800", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", tempAtLinkId[0], tempAtLinkId[0] + "|" + tempAtLinkId[1], "E BAND", "Wrong Data", "Wrong Data", "Wrong Data", "10.0.0.0", "10.0.0.1", "1.8", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data");
            row = sheet.createRow(sheet.getPhysicalNumberOfRows());
            wrongDataRowCells(row, circle, getNominalAop(), getNominalQuarter(), "", "", "", "E Band", "UBR to Microwave Upgrade", "Deployment Pending", "", "", "AVIAT", "Nome A", "Nome B", trafficLinkId, "JK_70G_250M_F1", "V", tempTrafficLinkId[0], "34.050466", "74.262428", "23", "12", "81250", "Wrong Data", "180.6", "Wrong Data", "0.9", "Wrong Data", "56.1", tempTrafficLinkId[1], "33.99908", "74.26221", "Wrong Data", "71250", "360", "Wrong Data", "0.9", "Wrong Data", "Wrong Data", "Wrong Data", "250", "24", "4.0", "Wrong Data", "Enable", "QPSK", "64QAM", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Enable", "Wrong Data", "Wrong Data", "WTM 4800", "MUX", "Wrong Data", "Wrong Data", "WTM 4800", "BBU", "Wrong Data", "WTM 4800", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", tempTrafficLinkId[0], tempTrafficLinkId[0] + "|" + tempTrafficLinkId[1], "E BAND", "Wrong Data", "Wrong Data", "Wrong Data", "10.0.0.0", "10.0.0.1", "1.8", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data", "Wrong Data");

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream("excelDoc/MWLinkPlanUploadSheetSampleData.xlsx")) { // ✅ Fixed extension
                workbook.write(fileOut);
                // Update the filename here too
                logger.info("Absolute path: " + new java.io.File("excelDoc/MWLinkPlanUploadSheetSampleData.xlsx").getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error generating MW Link data: " + e.getMessage());
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    System.err.println("Error closing workbook: " + e.getMessage());
                }
            }
        }catch (Exception e){
            logger.info("Error is: "+ e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        PlanUploadSheetGenerator generator = new PlanUploadSheetGenerator();
//        logger.info("Hello");
//        logger.info("ok" + generator.generateSampleData());
        logger.info("Wrong Data Generation");
        generator.generateMWLinkDataTable("Z_Bhanu","adm@123");

    }
}
