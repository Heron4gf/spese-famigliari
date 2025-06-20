package it.unicam.cs.mpgc.jbudget125639.gui;

import it.unicam.cs.mpgc.jbudget125639.gui.constants.UIConfig;
import it.unicam.cs.mpgc.jbudget125639.gui.constants.UILabel;
import it.unicam.cs.mpgc.jbudget125639.gui.services.DialogService;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.NonNull;
import lombok.Setter;

public class BudgetApp extends Application {

    @Setter
    @NonNull
    private static ModulesManager modulesManager;

    private final DialogService dialogService = new DialogService();

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/it/unicam/cs/mpgc/jbudget125639/gui/MainView.fxml")
            );

            Parent root = loader.load();
            MainViewController controller = loader.getController();

            controller.initializeManager(modulesManager);
            configureStage(primaryStage, root);
            primaryStage.show();

        } catch (Exception e) {
            Platform.runLater(() -> {
                dialogService.showError("Errore durante l'avvio: " + e.getMessage());
                Platform.exit();
            });
        }
    }

    private void configureStage(Stage primaryStage, Parent root) {
        primaryStage.setTitle(UILabel.WINDOW_TITLE.get());
        primaryStage.setScene(new Scene(
                root,
                UIConfig.WINDOW_WIDTH.getValue(),
                UIConfig.WINDOW_HEIGHT.getValue()
        ));
    }
}