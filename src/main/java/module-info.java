module com.aniruddho_roy.delete.delete {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;


    opens com.aniruddho_roy.delete.delete to javafx.fxml;
    exports com.aniruddho_roy.delete.delete;
}