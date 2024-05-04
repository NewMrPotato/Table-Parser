package com.rather.parsoftables.FileManagement;

//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Objects;
//
//public class ExcelManager {
//    public void convertTableToExcel(
//            String PathOfSaveFile, String nameOfSaveFile,
//            String reference, String XPath,
//            int rows, int columns, int indexTableOnSite
//    ) {
//        String finalNameOfSaveFile = "";
//
//        // Checking and control of input file name
//        if (Objects.equals(nameOfSaveFile, "") || nameOfSaveFile == null){
//            finalNameOfSaveFile = "NewTable";
//        } else {
//            finalNameOfSaveFile = nameOfSaveFile;
//        }
//
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("NewTable");
//
//            Document doc = null;
//
//            try{
//                doc = Jsoup.connect(reference).userAgent("Mozilla").get(); // получаем HTML-код страницы
//            }catch (Exception exception1){
//                try {
//                    doc = Jsoup.connect(reference).get(); // получаем HTML-код страницы
//                } catch (IOException exception2){
//                    try {
//                        throw exception1;
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//
//            Element table = doc.selectXpath(XPath).get(indexTableOnSite); // выбираем таблицу по CSS-селектору и индексу на сайте
//            Elements rowsElements = table.select("tr"); // выбираем все строки таблицы
//
//            Element thead = table.selectFirst("thead");
//            if (thead == null)
//                thead = rowsElements.get(0);
//            Elements theadElements = thead.select("tr");
//
//            Row headerRow = sheet.createRow(0);
//
//            Element theadElement = theadElements.get(0);
//            Elements theadColumnsElements = theadElement.select("th");
//            for (int i = 0; i < columns && i < theadColumnsElements.size(); i++) {
//                Element column = theadColumnsElements.get(i);
//                String cellText = column.text();
//                Cell cell = headerRow.createCell(i);
//                cell.setCellValue(cellText);
//            }
//
//
//            Row bodyRow = null;
//            Cell bodyCell = null;
//
//            for (int i = 0; i < rows && i < rowsElements.size(); i++) { // перебираем нужное количество строк
//                Element row = rowsElements.get(i); // получаем очередную строку таблицы
//                Elements columnsElements = row.select("td"); // выбираем все ячейки строки
//
//                bodyRow = sheet.createRow(i);
//
//                //result.append((i+1) + ")  ");
//
//                for (int j = 0; j < columns && j < columnsElements.size(); j++) { // перебираем нужное количество столбцов
//                    Element column = columnsElements.get(j); // получаем очередную ячейку
//                    String cellText = column.text(); // получаем текст ячейки
//
//                    bodyCell = bodyRow.createCell(j);
//                    bodyCell.setCellValue(cellText);
//
//                    //result.append(cellText + " | "); // выводим текст ячейки и табуляцию для разделения
//                }
//            }
//
//        try (FileOutputStream fileOut = new FileOutputStream("excel_tables/" + finalNameOfSaveFile + ".xlsx")){
//            workbook.write(fileOut);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        if (!Objects.equals(PathOfSaveFile, "")){
//            try (FileOutputStream fileCustomOut = new FileOutputStream(PathOfSaveFile + finalNameOfSaveFile + ".xlsx")){
//                workbook.write(fileCustomOut);
//                workbook.close();
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//    }
//}
//


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
