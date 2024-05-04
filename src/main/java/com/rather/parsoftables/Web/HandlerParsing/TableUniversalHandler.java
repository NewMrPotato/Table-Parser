package com.rather.parsoftables.Web.HandlerParsing;

import com.rather.parsoftables.Web.AbstractClasses.Handler;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TableUniversalHandler extends Handler {
    @Override
    public String handle(Elements rowsElements, int rows, int columns) {
        StringBuilder result = new StringBuilder();

        try {
            // Checking if some parameters are null
            if (rowsElements == null || rows == 0 || columns == 0){
                throw new RuntimeException();
            }
            for (int i = 0; i < rows && i < rowsElements.size(); i++) { // перебираем нужное количество строк
                Element row = rowsElements.get(i); // получаем очередную строку таблицы
                Elements columnsElements = row.select("td"); // выбираем все ячейки строки

                result.append((i+1) + ")  ");

                for (int j = 0; j < columns && j < columnsElements.size(); j++) { // перебираем нужное количество столбцов
                    Element column = columnsElements.get(j); // получаем очередную ячейку
                    String cellText = column.text(); // получаем текст ячейки
                    result.append(cellText + " | "); // выводим текст ячейки и табуляцию для разделения
                }

                result.append("\n");
            }
        }catch (Exception exception) {
            throw exception;
        }

        return result.toString();
    }
}
