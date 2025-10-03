package com.avendum.midsautomate.validation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrafficShiftingValidations {

    public static boolean validateCircle(String circle, String selectedCircle) {
        return (circle != null && selectedCircle != null) && circle.equals(selectedCircle);
    }

    public static boolean validateProject(String projectVal) {
        List<String> project = Arrays.asList("JUNNON 1", "OPERATIONAL ISSUES", "E-BAND",
                "OTHERS", "NEW ROLLOUT", "RELOCATION", "POP LOADING", "JUNNON 2", "MW Utilization & TWAMP",
                "CSR", "FTTH", "UBR");

        return projectVal != null && project.contains(projectVal);
    }

    public static void getRaValidationList(Map<String,List<String>> orderLists){
        orderLists.put("LEH724", Arrays.asList("ORDER223", "ORDER224", "ORDER225", "ORDER126"));
        orderLists.put("LEH790", Arrays.asList("ORDER223", "ORDER224", "ORDER225", "ORDER126"));
        orderLists.put("JMU086", Arrays.asList("ORDER223", "ORDER224", "ORDER225", "ORDER226", "ORDER126"));

    }

    public static boolean validateRelocation(String relVal) {
        if (relVal != null && !relVal.isEmpty()) {
            return Arrays.asList("JMU086", "LEH724", "LEH790").contains(relVal);
        }
        return true;
    }

    public static boolean validatePlannedPop(String popVal, String connectivityVal) {
        if (!connectivityVal.equals("FTTH")) {
            return Arrays.asList("JMU086", "LEH724", "LEH790").contains(popVal);
        }
        return true;
    }

    public static boolean validateConnectivity(String connectivityVal) {
        if (connectivityVal != null && !connectivityVal.isEmpty()) {
            List<String> i = Arrays.asList(" POP Colo", "MW", "FTTH", "CSR", "UBR");
            return i.contains(connectivityVal);
        }
        return false;
    }

    public static boolean validatePlannedPCM(String planPcm, String val) {
        return planPcm != null && !planPcm.isEmpty() && val.equalsIgnoreCase(planPcm);
    }

    public static boolean validateNewHopToBeInstalled(String newHop) {
        return newHop != null && !newHop.isEmpty() && Arrays.asList("Yes", "No").contains(newHop);
    }

    public static boolean validateHopToBeDismantled(String dismantleEdHop) {
        return dismantleEdHop != null && (dismantleEdHop.isEmpty() || dismantleEdHop.contains("-"));
    }

    public static boolean validateDismantleHopOEM(String hopOem) {
        return hopOem != null && (hopOem.isEmpty() || Arrays.asList("Ericsson", "Aviat", "Huawei", "Ceragon").contains(hopOem));
    }

    public static boolean validateDismantleHopType(String hopType) {
        return hopType != null && (hopType.isEmpty() || Arrays.asList("PDH", "Split", "Full-outdoor", "UBR", "UBR-EXT").contains(hopType));
    }

    public static boolean validateTSPlanReleased(String tsPlan) {
        return tsPlan != null && !tsPlan.isEmpty() && Arrays.asList("Yes", "No").contains(tsPlan);
    }

    public static boolean validateCategory(String category) {
        List<String> allowedCategories = Arrays.asList(
                "Colo", "Remote", "FTTH Colo", "Fiberized Colo", "FTTH", "CSR", "UBR"
        );
        return category != null && !category.isEmpty() && allowedCategories.contains(category);
    }


    public static boolean validateDcnRA(String dcnRa, String project) {
        if (!"CSR".equals(project)) {
            return dcnRa != null && dcnRa.matches("^[a-zA-Z0-9]+$");
        }
        return true;
    }

    public static boolean validateDcnVlan(String vlan, String project) {
        if (!"CSR".equals(project)) {
            try {
                int vlanValue = Integer.parseInt(vlan);
                return vlanValue >= 1 && vlanValue <= 4094;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateAlphaNumericalData(String val) {
        return val != null && val.matches("^[a-zA-Z0-9]+$");
    }

    public static boolean validateByCategoryAndConnectivity(String category,String conn){
        return "MW".equals(conn) && "Remote".equals(category);
    }

    public static boolean validateVlanRange(String vlan) {
        try {
            if(vlan.isEmpty()) return false;
            double vlanDouble = Double.parseDouble(vlan);
            int vlanValue = (int) vlanDouble; // Truncate decimal part
            return vlanValue >= 1 && vlanValue <= 4094;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isIpV4OrV6(String value) {
        return isValidIpV4(value) || isValidIpv6(value);
    }

    private static boolean isValidIpV4(String value) {
        return value != null && value.matches(
                "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}"
                        + "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$"
        );
    }


    private static boolean isValidIpv6(String value) {
        return value != null && value.matches(
                "^(?:[\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$"     // full form
                        + "|^(?:[\\da-fA-F]{1,4}:){1,7}:$"               // :: at end
                        + "|^:(?::[\\da-fA-F]{1,4}){1,7}$"               // :: at beginning
                        + "|^(?:[\\da-fA-F]{1,4}:){1,6}:[\\da-fA-F]{1,4}$" // single ::
                        + "|^(?:[\\da-fA-F]{1,4}:){1,5}(?::[\\da-fA-F]{1,4}){1,2}$"
                        + "|^(?:[\\da-fA-F]{1,4}:){1,4}(?::[\\da-fA-F]{1,4}){1,3}$"
                        + "|^(?:[\\da-fA-F]{1,4}:){1,3}(?::[\\da-fA-F]{1,4}){1,4}$"
                        + "|^(?:[\\da-fA-F]{1,4}:){1,2}(?::[\\da-fA-F]{1,4}){1,5}$"
                        + "|^[\\da-fA-F]{1,4}:(?::[\\da-fA-F]{1,4}){1,6}$"
                        + "|^:(?::[\\da-fA-F]{1,4}){1,7}$"
                        + "|^(?:[\\da-fA-F]{1,4}:){1,7}:$"
        );
    }

    public static boolean isValidIpWithCidr(String input) {
        if (input == null || !input.contains("/")) {
            return isValidIpV4(input)||isValidIpv6(input);
        }

        String[] parts = input.split("/");
        if (parts.length != 2) {
            return false;
        }

        String ip = parts[0].trim();
        String subnet = parts[1].trim();

        try {
            int subnetInt = Integer.parseInt(subnet);

            if (isValidIpV4(ip)) {
                return subnetInt >= 1 && subnetInt <= 32;
            } else if (isValidIpv6(ip)) {
                return subnetInt >= 1 && subnetInt <= 128;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

}
