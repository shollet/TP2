/**
 * Module lancant HelloApplication
 */
module com.example.graphisme {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.graphisme to javafx.fxml;
    exports com.example.graphisme;
}