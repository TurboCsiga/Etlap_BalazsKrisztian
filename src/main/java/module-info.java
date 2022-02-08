module com.example.etlap_balazskrisztian {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.etlap_balazskrisztian to javafx.fxml;
    exports com.example.etlap_balazskrisztian;
}