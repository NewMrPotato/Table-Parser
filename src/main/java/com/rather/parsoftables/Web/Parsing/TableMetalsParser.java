package com.rather.parsoftables.Web.Parsing;

import com.rather.parsoftables.Web.AbstractClasses.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class TableMetalsParser extends Parser {
    @Override
    public Elements parse(String reference, String XPath, int rows, int columns, int indexTableOnSite) {
        try {
            /*tring finalCssQuery = "table";
            for (String par: cssQueryForTable) { finalCssQuery = finalCssQuery + "[" + par + "]"; }*/

            Document document = (Document) Jsoup.connect(reference).get();
            //Elements tableElements = ((Element) document).selectXpath("//*[@id='mm-0']/div[2]/div[2]/div[3]/div[2]/table");
            //Elements tableElements = ((Element) document).selectXpath("/html/body/div[1]/div[2]/div[2]/div[3]/div[2]/table");
            Element tableElements = ((Element) document).selectXpath(XPath).get(indexTableOnSite);
            Elements elementsMetals = tableElements.select("tr");

            return filterElements(elementsMetals);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Elements filterElements(Elements elementsMetals) {
        Elements resultElements = elementsMetals;

        resultElements.remove(0);
        resultElements.remove(0);
        resultElements.remove(38);
        resultElements.remove(174);
        resultElements.remove(174);
        resultElements.select("td[colspan=4]").remove();
        resultElements.select("tr[align=center]").remove();
        for (int i = 0; i < 40; i++) {
            resultElements.select("td[rowspan="+i+"]").remove();
        }

        return resultElements;
    }
}