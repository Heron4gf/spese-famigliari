module SpeseFamigliari.main {

    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires static lombok;
    requires ormlite.jdbc;
    requires java.sql;
    requires jakarta.validation;

    // These should already be here
    opens it.unicam.cs.mpgc.jbudget125639 to javafx.fxml;
    exports it.unicam.cs.mpgc.jbudget125639;

    exports it.unicam.cs.mpgc.jbudget125639.views;
    exports it.unicam.cs.mpgc.jbudget125639.entities;
    exports it.unicam.cs.mpgc.jbudget125639.filters;
    exports it.unicam.cs.mpgc.jbudget125639.money;
    exports it.unicam.cs.mpgc.jbudget125639.filters.tags;
    exports it.unicam.cs.mpgc.jbudget125639.filters.dates; // <--- IMPORTANT: Ensure this is present
}