package it.unicam.cs.mpgc.jbudget125639.ui;

import it.unicam.cs.mpgc.jbudget125639.AddTransactionController;
import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.views.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public class NavigationService {

    private static final String ADD_TRANSACTION_DIALOG_FXML = "/it/unicam/cs/mpgc/jbudget125639/add-transaction-dialog.fxml";

    public Optional<Transaction> showAddTransactionDialog(Window owner, Collection<User> availableUsers, View selectedView) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ADD_TRANSACTION_DIALOG_FXML));
        VBox root = fxmlLoader.load();
        AddTransactionController controller = fxmlLoader.getController();

        controller.setAvailableUsers(availableUsers);
        if (selectedView instanceof User user) {
            controller.setTargetUser(user);
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add New Transaction");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(owner);
        dialogStage.setScene(new Scene(root));
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.setResizable(false);
        dialogStage.showAndWait();

        return controller.getNewTransaction();
    }
}