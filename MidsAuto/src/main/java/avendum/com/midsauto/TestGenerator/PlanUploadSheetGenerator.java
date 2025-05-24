package avendum.com.midsauto.TestGenerator;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
public class PlanUploadSheetGenerator {
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
                return mwChasis.get(0); // Return the first mw chasis
            } else {
                return "BBU";
            }
        }catch (Exception e){
            return  "CEN";
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


    public String generateSampleData() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Plan Upload Sheet");

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

        // Create header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        //Enter Correct Final Project Value :-Network Re-Engineering, Junoon 2, New Rollout, NEW ROLLOUT_ULS, Relocation, E-Band, USOF (1+0),USOF (XPIC), MW utilization &amp; TWAMP, Owner Issue, LOS Block, Site Locked, RSL Issue, Frequency Change, Card Upgrade, UBR to Microwave Upgrade, E-Band SDB, MW Link Swap
        //Sample Data generate for xlsx file
//        for(int a=0;a<planCategory.length;a++){
//            String plan_Category=planCategory[a];
//            for(int b=0;b<equipmentMake.length;b++){
//                String equipment_Make=equipmentMake[b];
//                for(int c=0;c<planStatus.length;c++){
//                    for (int d=0;d<txAntenna.length;d++){
//                        String antenna_Size=txAntenna[d];
//                        for(int e=0;e<rxAntenna.length;e++){
//                            String rx_Antenna=rxAntenna[e];
//                            for(int f=0;f<ber10e6EffMarginDb.length;f++){
//                                String ber10e6_Eff_Margin=String.valueOf(ber10e6EffMarginDb[f]);
//                                for(int g=0;g<BER10e6RainInterference.length;g++){
//                                    String ber10e6_Rain_Interference=String.valueOf(BER10e6RainInterference[g]);
//                                    for(int h=0;h<finalProjects.length;h++) {
//                                        String finalProj=finalProjects[h];
//                                        //Cells populate
//
//                                        Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
//                                        row.createCell(0).setCellValue(circle); // Circle: JK
//                                        row.createCell(1).setCellValue(getNominalAop()); // Nominal Aop: FY25_26
//                                        row.createCell(2).setCellValue(getNominalQuarter()); // Nominal Quarter: Q1
//                                        row.createCell(3).setCellValue(""); // Plan Release Date
//                                        row.createCell(4).setCellValue(""); // Ageing
//                                        row.createCell(5).setCellValue(""); // Ageing Range
//                                        row.createCell(6).setCellValue(plan_Category); // Plan category
//                                        row.createCell(7).setCellValue(finalProj); // Final Project
//                                        row.createCell(8).setCellValue(planStatus[c]); // Plan status
//                                        row.createCell(9).setCellValue(""); // Plan Stage
//                                        row.createCell(10).setCellValue(""); // Deployment Date/Month
//                                        row.createCell(11).setCellValue(equipment_Make); // Equipment Make
//                                        row.createCell(12).setCellValue(hopNomenclatureA); // Hop Nomenclature Site-A
//                                        row.createCell(13).setCellValue(hopNomenclatureB); // Hop Nomenclature Site-B
//                                        row.createCell(14).setCellValue(linkId); // Link ID
//                                        row.createCell(15).setCellValue(channel); // Channel
//                                        row.createCell(16).setCellValue(getPolarization(getValidHop(plan_Category))); // Polarization
//                                        row.createCell(17).setCellValue(siteAId); // Site ID-A
//                                        row.createCell(18).setCellValue(sitALat); // Site A Lat
//                                        row.createCell(19).setCellValue(siteALong); // Site A Long
//                                        row.createCell(20).setCellValue(txElevation); // Tx Site Elevation (m)
//                                        row.createCell(21).setCellValue(ber10e6TxPower); // BER10e6 Tx Power (dBm)
//                                        row.createCell(22).setCellValue(String.valueOf(txFrequency)); // Tx Frequency (MHz)
//                                        row.createCell(23).setCellValue(ber10e6RxLevel); // BER10e6 Rx Level (dBm)
//                                        row.createCell(24).setCellValue(txAntAzimuth); // Tx Ant. Azimuth (°)
//                                        row.createCell(25).setCellValue(txAntHeight); // Tx Ant. Height (m)
//                                        row.createCell(26).setCellValue(antenna_Size); // Tx Antenna
//                                        row.createCell(27).setCellValue(txAntGain); // Tx Ant. Gain (dB)
//                                        row.createCell(28).setCellValue(getBer10e6EIRP); // BER10e6 EIRP (dBm)
//                                        row.createCell(29).setCellValue(siteBID); // Site ID -B
//                                        row.createCell(30).setCellValue(siteBLat); // Site B Lat
//                                        row.createCell(31).setCellValue(siteBLong); // Site B Long
//                                        row.createCell(32).setCellValue(rxElevation); // Rx Site Elevation (m)
//                                        row.createCell(33).setCellValue(String.valueOf(rxFrequency)); // Rx Frequency (MHz)
//                                        row.createCell(34).setCellValue(rxAntAzimuth); // Rx Ant. Azimuth (°)
//                                        row.createCell(35).setCellValue(rxAntHeight); // Rx Ant. Height (m)
//                                        row.createCell(36).setCellValue(rx_Antenna); // Rx Antenna
//                                        row.createCell(37).setCellValue(rxAntGain); // Rx Ant. Gain (dB)
//                                        Map<String, String[]> txRadioMap = getValidTxRadio(equipment_Make);
//                                        String txRadio = txRadioMap.values().iterator().next()[0];
//                                        row.createCell(38).setCellValue(txRadio); // Tx Radio
//                                        row.createCell(39).setCellValue(distance); // Distance (km)
//                                        row.createCell(40).setCellValue(getBandW(getValidHop(plan_Category))); // Bandwidth (MHz)
//                                        row.createCell(41).setCellValue(ber10e6_Eff_Margin); // BER10e6 Eff. Margin (dB)
//                                        row.createCell(42).setCellValue(ber10e6_Rain_Interference); // BER10e6 Rain Interference Thr. Deg. (dB)
//                                        row.createCell(43).setCellValue(String.valueOf(availability)); // Availability
//                                        row.createCell(44).setCellValue(acmStatus); // ACM Status
//                                        row.createCell(45).setCellValue(getAcmMin(equipment_Make)); // ACM Min QAM
//                                        row.createCell(46).setCellValue(getAcmMax(plan_Category)); // ACM Max QAM
//                                        row.createCell(47).setCellValue(radioCodeHigh); // Radio Code (High)
//                                        row.createCell(48).setCellValue(radioCodeLow); // Radio Code (Low)
//                                        row.createCell(49).setCellValue("1234abc-XMC"); // Modem Code Site-A
//                                        row.createCell(50).setCellValue("1234abc-XMC"); // MDU Code Site-A
//                                        row.createCell(51).setCellValue(getMwChasis("1234abc-XMC")); // Chassis Code Site-A
//                                        row.createCell(52).setCellValue("1234abc-XMC"); // Antenna Code Site-A
//                                        row.createCell(53).setCellValue("1234abc-XMC"); // Modem Code Site-B
//                                        row.createCell(54).setCellValue("1234abc-XMC"); // MDU Code Site-B
//                                        row.createCell(55).setCellValue("1234abc-XMC"); // Chassis Code Site-B
//                                        row.createCell(56).setCellValue("1234abc-XMC"); // Antenna Code Site-B
//                                        row.createCell(57).setCellValue(getATPCStatus(equipment_Make)); // ATPC Status
//                                        row.createCell(58).setCellValue(getATPCMin(equipment_Make)); // ATPC MIN
//                                        row.createCell(59).setCellValue(getATPCMax(equipment_Make)); // ATPC MAX
//                                        row.createCell(60).setCellValue(getModemCodeSite(equipment_Make)); // (MODEM(IF Card)/Ethernet Port) Site-A
//                                        row.createCell(61).setCellValue(getMwChasis(equipment_Make)); // (MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-A
//                                        row.createCell(62).setCellValue(mwChasisNeworExisting[0]); // MW Chasis New or Existing Site-A (Later)
//                                        row.createCell(63).setCellValue(""); // Free Slot/Port Interface Detail Site A
//                                        row.createCell(64).setCellValue(getModemCodeSite(equipment_Make)); // (MODEM(IF Card)/Ethernet Port) Site-B
//                                        row.createCell(65).setCellValue(getMwChasis(getModemCodeSite(equipment_Make))); // (MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-B
//                                        row.createCell(66).setCellValue(mwChasisNeworExisting[0]); // MW Chasis New or Existing Site-B
//                                        row.createCell(67).setCellValue(""); // Free Slot/Port Interface Detail Site B
//                                        row.createCell(68).setCellValue(""); // Hop Dismantle Y / N
//                                        row.createCell(69).setCellValue(""); // Dismantle Hop ID
//                                        row.createCell(70).setCellValue(""); // Dismantle Hop Type
//                                        row.createCell(71).setCellValue(""); // Dismantle Hop Modem A
//                                        row.createCell(72).setCellValue(""); // Dismantle Hop Chassis A
//                                        row.createCell(73).setCellValue(antenna_Size); // Antenna Size Site-A
//                                        row.createCell(74).setCellValue(""); // Dismantle Hop Modem B
//                                        row.createCell(75).setCellValue(""); // Dismantle Hop Chassis B
//                                        row.createCell(76).setCellValue(rx_Antenna); // Antenna Size Site-B
//                                        row.createCell(77).setCellValue(""); // Remarks
//                                        row.createCell(78).setCellValue(popID); // Fiber POP Id
//                                        row.createCell(79).setCellValue(siteAId+"|"+siteBID); // PCM PATH
//                                        row.createCell(80).setCellValue(getValidHop(plan_Category)); // Hop Type
//                                        row.createCell(81).setCellValue(""); // DCN RA Number
//                                        row.createCell(82).setCellValue(""); // DCN VLAN
//                                        row.createCell(83).setCellValue(""); // GATEWAY IP
//                                        row.createCell(84).setCellValue(ip4A); // Site A End IP
//                                        row.createCell(85).setCellValue(ip4); // Site B End IP
//                                        row.createCell(86).setCellValue(""); // Antenna Beam Width
//                                        row.createCell(87).setCellValue(""); // Node ID Site A
//                                        row.createCell(88).setCellValue(""); // Node ID Site B
//                                        row.createCell(89).setCellValue(""); // MO Number Site A
//                                        row.createCell(90).setCellValue(""); // MO Number Site B
//                                        row.createCell(91).setCellValue(""); // For Site ID
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//                System.out.println("Ok");
//        }
        int aa=planCategory.length;
        int bb=equipmentMake.length;
        int cc=planStatus.length;
        int dd=txAntenna.length;
        int ee=rxAntenna.length;
        int ff=ber10e6EffMarginDb.length;
        int gg=BER10e6RainInterference.length;
        int hh=finalProjects.length;
        int ii=fiberPopMwChasisList.size();

        while (aa-- > 0 || bb-- > 0 || cc-- > 0 || dd-- > 0 || ee-- > 0 || ff-- > 0 || gg-- > 0 || hh-- > 0 || ii-- > 0) {
            int count = (int) (Math.random() * 10000);
            String siteAId = "Test_A_ID" + new SimpleDateFormat("ddMMyy_HHmmssSSS").format(new Date()) + count;
            String siteBID = "Test_B_ID" + new SimpleDateFormat("ddMMyy_HHmmssSSS").format(new Date()) + (count + 1);
            String linkId = siteAId + "-" + siteBID;

            // Determine values for each parameter
            String plan_Category = (aa <= 0) ? planCategory[0] : planCategory[aa - 1];
            String equipment_Make = (bb <= 0) ? equipmentMake[0] : equipmentMake[bb - 1];
            String plan_Status = (cc <= 0) ? planStatus[0] : planStatus[cc - 1];
            String antenna_Size = (dd <= 0) ? txAntenna[0] : txAntenna[dd - 1];
            String rx_Antenna = (ee <= 0) ? rxAntenna[0] : rxAntenna[ee - 1];
            String ber10e6_Eff_Margin = (ff <= 0) ? String.valueOf(ber10e6EffMarginDb[0]) : String.valueOf(ber10e6EffMarginDb[ff - 1]);
            String ber10e6_Rain_Interference = (gg <= 0) ? String.valueOf(BER10e6RainInterference[0]) : String.valueOf(BER10e6RainInterference[gg - 1]);
            String finalProj = (hh <= 0) ? finalProjects[0] : finalProjects[hh - 1];
            String aChais = (ii <= 0) ? fiberPopMwChasisList.get(0) : fiberPopMwChasisList.get(ii - 1);
            if(plan_Category.equalsIgnoreCase("Dual XPIC Upgrade")|| plan_Category.equalsIgnoreCase("1+0 to 1 Gig Upgrade")){
                rxFrequency="71250/81250";
                txFrequency="81250/71250";
            }else{
                rxFrequency="71250";
                txFrequency="81250";
            }
            // Create a new row and populate cells
            Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
            createRowCells(row, siteAId, siteBID, linkId, plan_Category, equipment_Make, plan_Status, antenna_Size, rx_Antenna, ber10e6_Eff_Margin, ber10e6_Rain_Interference, finalProj, aChais,rxFrequency, txFrequency);
        }
    //Add other special case where site id B is as pop Id then implement
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream("PlanUploadSheetSampleData.xlsx")) { // ✅ Fixed extension
            workbook.write(fileOut);
            // Update the filename here too
            System.out.println("Absolute path: " + new java.io.File("PlanUploadSheetSampleData.xlsx").getAbsolutePath());
            return "Sample data generated successfully in PlanUploadSheetSampleData.xlsx";
        } catch (IOException e) {
            return "Error generating sample data: " + e.getMessage();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                // Log error if needed
            }
        }
    }

    //Function to pass the value using parameter
    private void createRowCells(Row row, String siteAId, String siteBID, String linkId, String plan_Category, String equipment_Make, String plan_Status, String antenna_Size, String rx_Antenna, String ber10e6_Eff_Margin, String ber10e6_Rain_Interference, String finalProj, String aChais,String rxFreq,String txFreq) {
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
        row.createCell(16).setCellValue(getPolarization(getValidHop(plan_Category))); // Polarization
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
        row.createCell(40).setCellValue(getBandW(getValidHop(plan_Category))); // Bandwidth (MHz)
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
        row.createCell(60).setCellValue(getModemCodeSite(equipment_Make)); // (MODEM(IF Card)/Ethernet Port) Site-A
        row.createCell(61).setCellValue(aChais); // (MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-A
        row.createCell(62).setCellValue(mwChasisNeworExisting[0]); // MW Chasis New or Existing Site-A
        row.createCell(63).setCellValue(""); // Free Slot/Port Interface Detail Site A
        row.createCell(64).setCellValue(getModemCodeSite(equipment_Make)); // (MODEM(IF Card)/Ethernet Port) Site-B
        row.createCell(65).setCellValue("BBU"); // (MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-B
        row.createCell(66).setCellValue(mwChasisNeworExisting[0]); // MW Chasis New or Existing Site-B
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
        row.createCell(80).setCellValue(getValidHop(plan_Category)); // Hop Type
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

    public static void main(String[] args) {
        PlanUploadSheetGenerator generator = new PlanUploadSheetGenerator();
        System.out.println("Hello");
       System.out.println("ok" + generator.generateSampleData());
    }
}
