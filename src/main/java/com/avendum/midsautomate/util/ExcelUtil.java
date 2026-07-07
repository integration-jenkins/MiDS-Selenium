package com.avendum.midsautomate.util;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.util.*;

public class ExcelUtil {

    public static void main(String[] arr) throws Exception {
        List<String> keys = Arrays.asList("Site ID");
        String devPath = "D:\\KARTIK LOHATE\\TS_DEV.xlsx";
        String uatPath = "D:\\KARTIK LOHATE\\TS_UAT.xlsx";

        List<String> result = compareExcel(
                devPath,
                uatPath,
                keys
        );

        if(result.isEmpty()){
            System.out.println("Both sheet is equals");
        }

        result.forEach(System.out::println);
    }

    public static List<String> compareExcel(
            String excelAPath,
            String excelBPath,
            List<String> keyColumns
    ) throws Exception {

        Map<String, Map<String, String>> excelA = readExcel(excelAPath, keyColumns);
        Map<String, Map<String, String>> excelB = readExcel(excelBPath, keyColumns);

        List<String> mismatches = new ArrayList<>();

        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(excelA.keySet());
        allKeys.addAll(excelB.keySet());

        for (String rowKey : allKeys) {

            if (!excelA.containsKey(rowKey)) {
                mismatches.add("❌ Row missing in Excel A: " + rowKey);
                continue;
            }

            if (!excelB.containsKey(rowKey)) {
                mismatches.add("❌ Row missing in Excel B: " + rowKey);
                continue;
            }

            Map<String, String> rowA = excelA.get(rowKey);
            Map<String, String> rowB = excelB.get(rowKey);

            Set<String> columns = new HashSet<>();
            columns.addAll(rowA.keySet());
            columns.addAll(rowB.keySet());

            for (String col : columns) {
                String valA = rowA.getOrDefault(col, "");
                String valB = rowB.getOrDefault(col, "");

                if (!valA.equals(valB)) {
                    mismatches.add(
                            "⚠ Mismatch | RowKey=" + rowKey +
                                    " | Column=" + col +
                                    " | ExcelA='" + valA +
                                    "' | ExcelB='" + valB + "'"
                    );
                }
            }
        }

        return mismatches;
    }

    private static Map<String, Map<String, String>> readExcel(
            String path,
            List<String> keyColumns
    ) throws Exception {

        Map<String, Map<String, String>> data = new HashMap<>();

        try (Workbook workbook = WorkbookFactory.create(new File(path))) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            Row headerRow = rows.next();
            Map<Integer, String> headers = new HashMap<>();

            for (Cell cell : headerRow) {
                headers.put(cell.getColumnIndex(), cell.getStringCellValue().trim());
            }

            while (rows.hasNext()) {
                Row row = rows.next();
                Map<String, String> rowData = new HashMap<>();
                StringBuilder keyBuilder = new StringBuilder();

                for (Cell cell : row) {
                    String columnName = headers.get(cell.getColumnIndex());
                    String cellValue = getCellValue(cell);
                    rowData.put(columnName, cellValue);

                    if (keyColumns.contains(columnName)) {
                        keyBuilder.append(cellValue).append("|");
                    }
                }

                String rowKey = keyBuilder.toString();
                data.put(rowKey, rowData);
            }
        }
        return data;
    }

    private static String getCellValue(Cell cell) {

        if (cell == null) return "";

        CellType type = cell.getCellType();

        if (type == CellType.STRING) {
            return cell.getStringCellValue().trim();
        }

        if (type == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return new DataFormatter().formatCellValue(cell).trim();
            }

            double value = cell.getNumericCellValue();
            return (value == Math.floor(value))
                    ? String.valueOf((long) value)
                    : String.valueOf(value);
        }

        if (type == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        }

        if (type == CellType.BLANK) {
            return "";
        }

        return "";
    }







}
