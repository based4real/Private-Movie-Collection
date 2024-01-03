module pmc.privatemoviecollection {
    requires javafx.controls;
    requires javafx.fxml;


    opens pmc to javafx.fxml;
    exports pmc;
}