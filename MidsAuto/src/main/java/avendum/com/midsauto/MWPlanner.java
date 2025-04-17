package avendum.com.midsauto;
import org.openqa.selenium.WebDriver;

import java.text.ParseException;

public class MWPlanner {
    private WebDriver driver;
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
        String startStatus="SR Pending";
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
    public void MWPlannerLogin(String username, String password) throws InterruptedException {
        Login login = new Login();
        login.Login(username, password);
        driver = Base.getDriver();
    }
    public void MWPlannerDPRTrack(String start,String end) throws InterruptedException, ParseException {
        PanelTraverser panelTraverser = new PanelTraverser();
        panelTraverser.navigateToMWPlanTrackingPage(driver);

        //Test Ki mere Plan Id fetch hot Hai ya nahi
        PlanIdGetter planIdGetter = new PlanIdGetter();

        //This are working for SR-RFAI
//        String SR_P_PlanId = planIdGetter.planId( "SR Pending");
//        System.out.println("Plan Id is: "+SR_P_PlanId);
//        String SP_P_PlanId = planIdGetter.planId( "SP Pending");
//        System.out.println("Plan Id is: "+SP_P_PlanId);
//        String SO_P_PlanId = planIdGetter.planId( "SO Pending");
//        System.out.println("Plan Id is: "+SO_P_PlanId);
//        String RFAI_P_PlanId = planIdGetter.planId( "RFAI Pending");
//        System.out.println("Plan Id is: "+RFAI_P_PlanId);
        String planId= planIdGetter.planId( start);
        System.out.println(planId);
        int stageCount= planIdGetter.getStage();
        DPRAutomate dpr= new DPRAutomate();
        DPRAutomate.dprPlan(planId,stageCount);
        dpr.dprStage(start,end,"Soft",planId);

    }
}
