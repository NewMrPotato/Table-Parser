package com.rather.parsoftables.Web.HandlerParsing;

import com.rather.parsoftables.Web.AbstractClasses.Handler;
import org.jsoup.select.Elements;

public class TableMetalsHandler extends Handler {
    final int ROWS_OF_TABLE_METALS = 174;

    @Override
    public String handle(Elements elementsMaterials, int rows, int columns) {
        String resultText = "";
        int counter = 1;

        for (int i = 0; i < ROWS_OF_TABLE_METALS; i++) {

            Elements metal = elementsMaterials.get(i).getAllElements();

            try {
                if(metal.get(0).text()!="") {
                    resultText = resultText + (counter) + ") " + "Материал: " + metal.get(1).text() + "    "
                            + "Плотность: " + Float.toString(Float.parseFloat(metal.get(2).text().replace(",", ".")) * 1000)
                            .substring(0, Float.toString(Float.parseFloat(metal.get(2).text().replace(",", ".")) * 1000).length() - 2) + " кг/м3\n";
                    ++counter;
                } else{
                    continue;
                }
            } catch (Exception e) {
                try {
                    String[] matTir = metal.get(2).text().replace(",", ".").split("-");
                    resultText = resultText + (counter) + ") "
                            + "Материал: " + metal.get(1).text() + "    "
                            + "Плотность: " + Float.toString(Float.parseFloat(matTir[0]) * 1000)
                            .substring(0, Float.toString(Float.parseFloat(matTir[0]) * 1000).length() - 2)
                            + "-" + Float.toString(Float.parseFloat(matTir[1]) * 1000)
                            .substring(0, Float.toString(Float.parseFloat(matTir[1]) * 1000).length() - 2) + " кг/м3\n";
                    ++counter;
                } catch (Exception exception){
                    --counter;
                    continue;
                }
            }
        }

        return resultText;
    }
}
