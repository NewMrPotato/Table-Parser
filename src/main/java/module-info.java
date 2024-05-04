module com.rather.parsofmaterialtable {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.logging.log4j;
    requires jxl;


    opens com.rather.parsoftables to javafx.fxml;
    exports com.rather.parsoftables;
    exports com.rather.parsoftables.Controllers;
    opens com.rather.parsoftables.Controllers to javafx.fxml;
}