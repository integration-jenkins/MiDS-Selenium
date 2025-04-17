package avendum.com.midsauto;
import org.openqa.selenium.WebDriver;

public class Launch  {
    public static void MSPartner(String username, String password) throws InterruptedException {
        Login login = new Login();
        login.Login(username, password);
        WebDriver driver = Base.getDriver();
        Manual_AT_Acpt_Rjt mat= new Manual_AT_Acpt_Rjt();


        //Phy Acceptance testing
//        mat.Accept_Reject_MSPartner(driver,"MW-N-JK-07022025-6","Antenna Installation issue","Kuh toh kaho","Soft","Reject");


    }
    public static void MWPlanner(String username, String password) throws InterruptedException {
        Login login = new Login();
        login.Login(username, password);
        WebDriver driver = Base.getDriver();
//        Manual_AT_Raise mat= new Manual_AT_Raise();
//        mat.MWPlanner_Raise(driver,"MW-U-JK-07022025-5","Soft");
//        PlanCancelation plan=new PlanCancelation();
//        plan.cancelPlan(driver,"MW-U-JK-07022025-5","Char Bhej Gaya","Material not available","Yes");
//         plan.CanceledPlanRC(driver,"MW-U-JK-07022025-5","Cancel");
//          countValidate cc= new countValidate();
//          cc.countValidate(driver);
        DPRAutomate dpr= new DPRAutomate();
//        String startStage="Basic Details";
//        String tarrgetStatus="Phy Acceptance";
//        dpr.dprPlan(driver,"MW-N-JK-06012025-34","SR-RFAI");
//        Thread.sleep(5000);
//        dpr.dprStage(driver,"Basic Details","","ManualBoth");
        //There following senerio for which i have to generate sample

        //Possible target Status
        // AT Media Planning i have to multiple tab and for each tab multiple senerioa or say stages and
        //Get Plan Id according to the target Status
//Start Stage is following tab like SR-RFAI->(SR,SP,SO,RFAI Pending) and Material->(MO,MD Pending), INC->(HOP Align/Phy pending,Hop Align/ Phy raised,Hop Align/ Phy reject,Hop Align/ Phy accpt), AT->(Phy pending,Phy raised,Phy reject,Phy accpt similarly for soft), Traffic shifting->TS completed
        String SR_RFAI[]={"SR Pending","SP Pending","SO Pending","RFAI Pending"};
        String Material[]={"MO Pending","MD Pending"};
        String INC[]={"HOP Align/Phy Pending","HOP Align/Phy Raised","HOP Align/Phy Reject","HOP Align/Phy Accept"};
        String AT[]={"Phy + Soft Pending","Phy Raised/Soft Pending","Phy Reject/Soft Pending","Phy Accept/Soft Pending", "Phy + Soft Raised","Phy Pending/Soft Raised","Phy Reject/Soft Raised","Phy Accept/Soft Raised",
                "Phy + Soft Reject","Phy Pending/Soft Reject","Phy Raised/Soft Reject","Phy Accept/Soft Reject","AT Accepted","Phy Pending/Soft Accept","Phy Raised/Soft Accept","Phy Reject/Soft Accept"};
        String Traffic[]={"TS Completed"};
        //Total Combination is equaly to 4*2*4*16*1=512

        //Now for any of the condtion find plan id from the Website
        String startStatus="Phy + Soft Pending";
        String targetStatus="Phy Acceptance";
        //First figure out from which array startStatus is coming and targetStatus
        //Then find the plan id from the website
        //Then pass the plan id to the dprPlan method

//        if(contains(SR_RFAI, startStatus)){
//            if(contains(SR_RFAI, targetStatus)) {
//                dpr.dprPlan(driver, getPlanId(startStatus), "SR-RFAI");
//            } else if(contains(Material, targetStatus)) {
//                dpr.dprPlan(driver, getPlanId(startStatus), "Material");
//            } else if(contains(INC, targetStatus)) {
//                dpr.dprPlan(driver, getPlanId(startStatus), "INC");
//            } else if(contains(AT, targetStatus)) {
//                dpr.dprPlan(driver, getPlanId(startStatus), "AT");
//            } else if(contains(Traffic, targetStatus)) {
//                dpr.dprPlan(driver, getPlanId(startStatus), "Traffic");
//            }
//        } else if(contains(Material, startStatus)) {
//
//        } else if(contains(INC, startStatus)) {
//        } else if(contains(AT, startStatus)) {
//        } else if(contains(Traffic, startStatus)) {
//        }
        //Plan Id print for Different Senerio
        //1. SR-RFAI
        PlanIdGetter planIdGetter = new PlanIdGetter();
        String planId = planIdGetter.planId( startStatus);
        System.out.println("Plan Id is: "+planId);
        //Phy Acceptance testing
//        mat.Accept_Reject_MSPartner(driver,"MW-N-JK-07022025-6","Antenna Installation issue","Kuh toh kaho","Soft","Reject");

    }
    private static boolean contains(String[] array, String value) {
        for(String item : array) {
            if(item.equals(value)) return true;
        }
        return false;
    }
    public static   void dprPage(String startPoint, String endPoint) {
        try {
            MWPlanner mw = new MWPlanner();
            mw.MWPlannerLogin("Z_Bhanu", "adm@123");
            mw.MWPlannerDPRTrack(startPoint, endPoint);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        System.out.println("Mids Testing Started");
//        try {
////            MSPartner("JK_MS_PT_1", "adm@123");
////            MWPlanner("Z_Bhanu", "adm@123");
//            MWPlanner mw=new MWPlanner();
//            mw.MWPlannerLogin("Z_Bhanu", "adm@123");
//            mw.MWPlannerDPRTrack("SR Pending","AT Accepted");
////            DPRAutomate dpr= new DPRAutomate();
////            DPRAutomate.dprPlan("MW-N-AP-23012025-2",1);
////            dpr.dprStage("SR Pending","HOP Alignment Pending/Phy-AT Pending","ManualBoth");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
