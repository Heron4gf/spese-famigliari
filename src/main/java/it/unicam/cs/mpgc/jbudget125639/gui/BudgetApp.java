package it.unicam.cs.mpgc.jbudget125639.gui;

import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;

/**
 * Classe principale dell'applicazione JavaFX.
 * Si occupa di inizializzare la finestra e caricare la vista principale.
 */
public class BudgetApp extends Application {

    @Setter
    @NonNull
    private static ModulesManager modulesManager;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Carica la vista dal file FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unicam/cs/mpgc/jbudget125639/gui/MainView.fxml"));
        Parent root = loader.load();

        // Inietta il ModulesManager nel controller dopo aver caricato l'FXML
        MainViewController controller = loader.getController();
        controller.initializeManager(modulesManager);

        // Imposta la scena e mostra la finestra
        primaryStage.setTitle("JBudget");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
}