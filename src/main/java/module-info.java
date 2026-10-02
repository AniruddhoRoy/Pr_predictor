module com.aniruddho_roy.delete.delete {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.prefs;


    requires com.fasterxml.jackson.databind;

    opens com.aniruddho_roy.delete.delete to javafx.fxml;
    exports com.aniruddho_roy.delete.delete;
}