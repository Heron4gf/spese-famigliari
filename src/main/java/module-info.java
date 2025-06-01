module SpeseFamigliari.main {

    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires static lombok;
    requires ormlite.jdbc;
    requires java.sql;
    requires jakarta.validation;

    opens it.unicam.cs.spesefamigliari to javafx.fxml;
    exports it.unicam.cs.spesefamigliari;
}