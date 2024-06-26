package com.rather.parsoftables.FileManagement;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.Objects;

public class ExcelManager {
    public void convertTableToExcel(
            String PathOfSaveFile, String nameOfSaveFile,
            String reference, String XPath,
            int rows, int columns, int indexTableOnSite
    ) {
        String finalNameOfSaveFile = "";

        // Checking and control of input file name
        if (Objects.equals(nameOfSaveFile, "") || nameOfSaveFile == null){
            finalNameOfSaveFile = "NewTable";
        } else {
            finalNameOfSaveFile = nameOfSaveFile;
        }

        WritableWorkbook workbook = null;

        try {
            // Create a new Excel workbook
            if (PathOfSaveFile.equals("")) {
                workbook = Workbook.createWorkbook(new File("excel_tables/" + finalNameOfSaveFile + ".xls"));
            } else {
                workbook = Workbook.createWorkbook(new File(PathOfSaveFile + finalNameOfSaveFile + ".xls"));
            }

            // Create a new sheet in the workbook
            WritableSheet sheet = workbook.createSheet("NewTable", 0);

            // Connect to the web page and get the HTML document
            Document doc = Jsoup.connect(reference).userAgent("Mozilla").get();

            // Select the target HTML table
            Element table = doc.selectXpath(XPath).get(indexTableOnSite);

            // Get the header row
            Element thead = table.selectFirst("thead");
            if (thead == null)
                thead = table.select("tr").get(0);

            // Create the header row in the Excel sheet
            int rowIndex = 0;
            int columnIndex = 0;
            for (Element column : thead.select("th")) {
                sheet.addCell(new Label(columnIndex, rowIndex, column.text()));
                if (columnIndex != columns){
                    columnIndex++;
                } else {
                    break;
                }
            }

            // Get the body rows
            Elements rowsElements = table.select("tr");

            // Iterate through the body rows and create rows in the Excel sheet
            for (Element row : rowsElements) {
                if (rowIndex != rows){
                    rowIndex++;
                } else {
                    break;
                }

                // Reset the column index for each row
                columnIndex = 0;

                // Get the cells in the row
                Elements columnsElements = row.select("td");

                // Iterate through the cells and create cells in the Excel sheet
                for (Element column : columnsElements) {
                    sheet.addCell(new Label(columnIndex, rowIndex, column.text()));
                    if (columnIndex != columns){
                        columnIndex++;
                    } else {
                        break;
                    }
                }
            }

            // Write the workbook to a file
            workbook.write();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
