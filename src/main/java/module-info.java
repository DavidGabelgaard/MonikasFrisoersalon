module com.example.monikasfrisoersalon {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;


    opens com.example.monikasfrisoersalon to javafx.fxml;
    exports com.example.monikasfrisoersalon;
}