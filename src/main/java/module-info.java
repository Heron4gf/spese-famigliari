module it.unicam.cs.mpgc.jbudget125639 {
    // JavaFX requirements
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // ORMLite requirements
    requires ormlite.jdbc;
    requires java.sql;

    // Lombok for annotations
    requires static lombok;
    requires jakarta.validation;

    // Opens your packages to other modules
    opens it.unicam.cs.mpgc.jbudget125639 to javafx.fxml;
    opens it.unicam.cs.mpgc.jbudget125639.modules to javafx.fxml;
    opens it.unicam.cs.mpgc.jbudget125639.views to javafx.fxml;
    opens it.unicam.cs.mpgc.jbudget125639.entities to ormlite.jdbc;

    // Exports for application use
    exports it.unicam.cs.mpgc.jbudget125639;
    exports it.unicam.cs.mpgc.jbudget125639.modules;
    exports it.unicam.cs.mpgc.jbudget125639.modules.abstracts;
}