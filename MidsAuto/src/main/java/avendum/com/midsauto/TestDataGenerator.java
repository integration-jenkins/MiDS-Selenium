package avendum.com.midsauto;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class TestDataGenerator {

    // Generate basic stage data
    public static String[] generateBasicData() {
        String tocoAId = "TOCO-A-" + System.currentTimeMillis() % 10000;
        String tocoBId = "TOCO-B-" + System.currentTimeMillis() % 10000;
        String[] vendors = {"UBICO", "VIDEOCON", "AIRTEL"};
        return new String[] {
                tocoAId,
                tocoBId,
                vendors[new Random().nextInt(vendors.length)],
                vendors[new Random().nextInt(vendors.length)]
        };
    }

    //SRRFaiData Generation at different Senerio
    public static String[] generateSRRFaiData() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -9);
        String soad = sdf.format(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        String sbad = sdf.format(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        String srad = sdf.format(cal.getTime());

        return new String[] {
                "SR-No A",
                "SR-No B",
                srad, srad, sbad, sbad, soad, soad, soad, soad
        };
    }

    public static String[] generateForSPPending(String srad, String srbd) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -8);
        String soad = sdf.format(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        String sbad = sdf.format(cal.getTime());
        return new String[] {
                "SR-No A",
                "SR-No B",
                srad, srbd, sbad, sbad, soad, soad, soad, soad
        };
    }

    public static String[] generateForSOPending(String srad, String srbd,String sbad, String sbbd) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -8);
        String soad = sdf.format(cal.getTime());
        return new String[] {
                "SR-No A",
                "SR-No B",
                srad, srbd, sbad, sbbd, soad, soad, soad, soad
        };

    }
    public static String[] generateForRFPending(String existingSRAD, String existingSRBD, String existingSBAD, String existingSBBD, String existingSOAD, String existingSOBD) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -9);
        String soad = sdf.format(cal.getTime());
        return new String[] {
                "SR-No A",
                "SR-No B",
                existingSRAD,
                existingSRBD,
                existingSBAD, existingSBBD,existingSOAD, existingSOBD,
                soad,soad
        };
    }


    // Generate material data with valid sequence
    public static String[] genMDMOPending() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        String dd = sdf.format(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        String mdd = sdf.format(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        String mod = sdf.format(cal.getTime());
        return new String[] {
                "MOA",
                "MOB",
                mod, mod,
                "Fresh", "Fresh",
                mdd, mdd,
                dd, dd,
                "Fully Delivered"
        };
    }
    public static String[] genMDMDPending(String moAd,String moBd) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -5);
        String dd = sdf.format(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        String mdd = sdf.format(cal.getTime());
        return new String[] {
                "MOA",
                "MOB",
                moAd, moBd,
                "Fresh", "Fresh",
                mdd, mdd,
                dd, dd,
                "Fully Delivered"
        };
    }

    //InC data Generation at different different Senerio
    public static String[] generateINCData() {
        // inandCommStage(driver,"8/4/2025","8/4/2025",INC_Half,"8/4/2025","8/4/2025");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        String alignSiteD = sdf.format(cal.getTime());//NMS date also same as it it max data and nms consider max date
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        String siteD = sdf.format(cal.getTime());
        String incPartnerName = "INC Partner";
        String nsmStatus="Yes";
        return new String[] {
                siteD, siteD,incPartnerName,
                alignSiteD,alignSiteD,nsmStatus,
        };
    }



}