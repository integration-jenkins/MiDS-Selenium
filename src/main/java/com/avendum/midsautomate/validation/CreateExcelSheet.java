package com.avendum.midsautomate.validation;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class CreateExcelSheet {

    public static void main(String[] args) throws Exception {

        // ----- DO NOT CHANGE PATHS -----
        String frePath = "C:\\Users\\Kartik Lohate\\Downloads\\MIDS_AUTOMTION_JAN\\FREQUENCY.xlsx";
        String lbPath  = "C:\\Users\\Kartik Lohate\\Downloads\\MIDS_AUTOMTION_JAN\\LB UPLOAD SMAPLE.xlsx";
        String newPath = "C:\\Users\\Kartik Lohate\\Downloads\\MIDS_AUTOMTION_JAN\\LB UPLOAD SMAPLE2.xlsx";

        updateLbUploadWithFrequency(frePath, lbPath, newPath);
    }

    public static void updateLbUploadWithFrequency(
            String frequencyFilePath,
            String lbUploadFilePath,
            String outputFilePath
    ) throws Exception {

        try (
                Workbook freqWb = WorkbookFactory.create(new FileInputStream(frequencyFilePath));
                Workbook lbWb   = WorkbookFactory.create(new FileInputStream(lbUploadFilePath))
        ) {

            Sheet freqSheet = freqWb.getSheetAt(0);
            Sheet lbSheet   = lbWb.getSheetAt(0);

            // ---------------- READ HEADERS ----------------
            Map<String, Integer> freqCols = getHeaderMap(freqSheet);
            Map<String, Integer> lbCols   = getHeaderMap(lbSheet);

            System.out.println("Frequency Headers: " + freqCols);
            System.out.println("LB Headers: " + lbCols);

            // ---------------- TEMPLATE ROW ----------------
            Row templateRow = lbSheet.getRow(1);
            if (templateRow == null) {
                throw new RuntimeException("LB template row (row 1) is missing");
            }

            int lbRowIndex = 1; // start creating from row 1
            int createdRows = 0;

            // ---------------- CREATE LB ROWS ----------------
            for (int i = 1; i <= freqSheet.getLastRowNum(); i++) {

                Row freqRow = freqSheet.getRow(i);
                if (freqRow == null) continue;

                String circle = getCellValue(freqRow.getCell(freqCols.get("CIRCLE")));
                String channel = getCellValue(freqRow.getCell(freqCols.get("CHANNEL NAME")));
                String slot = getCellValue(freqRow.getCell(freqCols.get("FREQUENCY SLOT")));

                if (circle.isEmpty() || channel.isEmpty() || !slot.contains("-")) {
                    System.out.println("Skipping frequency row " + i);
                    continue;
                }

                String[] split = slot.split("-");
                String tx = split[0].trim();
                String rx = split[1].trim();

                // Create new LB row
                Row newLbRow = lbSheet.getRow(lbRowIndex);
                if (newLbRow == null) {
                    newLbRow = lbSheet.createRow(lbRowIndex);
                }

                // Copy ALL template cells
                for (int c = 0; c < templateRow.getLastCellNum(); c++) {
                    Cell src = templateRow.getCell(c);
                    if (src == null) continue;

                    Cell dest = newLbRow.createCell(c);
                    dest.setCellStyle(src.getCellStyle());

                    switch (src.getCellType()) {
                        case STRING:
                            dest.setCellValue(src.getStringCellValue());
                            break;
                        case NUMERIC:
                            dest.setCellValue(src.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            dest.setCellValue(src.getBooleanCellValue());
                            break;
                        default:
                            dest.setCellValue(src.toString());
                    }
                }

                // Update REQUIRED fields only
                newLbRow.getCell(lbCols.get("Circle")).setCellValue(circle);
                newLbRow.getCell(lbCols.get("Channel")).setCellValue(channel);
                newLbRow.getCell(lbCols.get("Tx Frequency (MHz)")).setCellValue(tx);
                newLbRow.getCell(lbCols.get("Rx Frequency (MHz)")).setCellValue(rx);

                createdRows++;
                System.out.println("✔ Created LB Row " + lbRowIndex +
                        " | " + circle + " | " + channel +
                        " | TX=" + tx + " RX=" + rx);

                lbRowIndex++;
            }

            // ---------------- WRITE OUTPUT ----------------
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                lbWb.write(fos);
            }

            System.out.println("====================================");
            System.out.println("✅ LB Rows Created Successfully");
            System.out.println("✅ Total Rows Created: " + createdRows);
            System.out.println("📄 Output File: " + outputFilePath);
            System.out.println("====================================");
        }
    }

    // ---------------- UTIL METHODS ----------------

    private static Map<String, Integer> getHeaderMap(Sheet sheet) {
        Map<String, Integer> map = new HashMap<>();
        Row header = sheet.getRow(0);
        for (Cell cell : header) {
            map.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
        }
        return map;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return cell.getStringCellValue().trim();
    }
}
