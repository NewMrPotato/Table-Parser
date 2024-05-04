package com.rather.parsoftables.Web.Parsing;

import com.rather.parsoftables.Web.AbstractClasses.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TableUniversalParser extends Parser {
    @Override
    public Elements parse(String reference, String XPath, int rows, int columns, int indexTableOnSite) {

        if (reference == null || XPath == null || rows == 0 || columns == 0){
            throw new RuntimeException();
        }

        try {
            Document doc = Jsoup.connect(reference).get(); // получаем HTML-код страницы
            Element table = doc.selectXpath(XPath).get(indexTableOnSite); // выбираем таблицу по xpath и индексу на сайте
            Elements rowsElements = table.select("tr"); // выбираем все строки таблицы

            if (rowsElements.toString().equals("")){ // Ничего не обнаружил
                throw new RuntimeException("The table wasn't found");
            }

            return rowsElements;

        } catch (IOException exception1) {
            try {
                Document doc = Jsoup.connect(reference).userAgent("Mozilla").get(); // получаем HTML-код страницы
                Element table = doc.select(XPath).get(indexTableOnSite); // выбираем таблицу по CSS-селектору и индексу на сайте
                Elements rowsElements = table.select("tr"); // выбираем все строки таблицы

                if (rowsElements.toString().equals("")){ // Ничего не обнаружил
                    throw new RuntimeException("The table wasn't found");
                }

                return rowsElements;

            } catch (IOException exception2){
                try {
                    throw exception1;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
