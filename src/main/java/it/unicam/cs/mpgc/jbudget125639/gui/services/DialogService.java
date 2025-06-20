package it.unicam.cs.mpgc.jbudget125639.gui.services;

import it.unicam.cs.mpgc.jbudget125639.gui.constants.UILabel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import lombok.NonNull;

import java.util.Optional;

/**
 * Service responsible for managing dialogs and user interactions.
 * Follows Single Responsibility Principle by handling only dialog operations.
 */
public class DialogService {
    
    /**
     * Shows an error dialog with the given throwable.
     * 
     * @param throwable the error to display
     */
    public void showError(@NonNull Throwable throwable) {
        showError(throwable.getMessage());
    }
    
    /**
     * Shows an error dialog with the given message.
     * 
     * @param message the error message to display
     */
    public void showError(@NonNull String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(UILabel.ERROR_TITLE.get());
        alert.setHeaderText(UILabel.ERROR_HEADER.get());
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Shows a confirmation dialog with the given message.
     * 
     * @param message the confirmation message
     * @return true if user confirmed, false otherwise
     */
    public boolean showConfirmation(@NonNull String message) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                message,
                ButtonType.YES, ButtonType.NO
        );
        alert.setTitle(UILabel.CONFIRMATION_TITLE.get());
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }
    
    /**
     * Shows a text input dialog for creating a new user.
     * 
     * @return Optional containing the entered name, empty if cancelled
     */
    public Optional<String> showNewUserDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(UILabel.NEW_USER_TITLE.get());
        dialog.setHeaderText(UILabel.NEW_USER_HEADER.get());
        dialog.setContentText(UILabel.NEW_USER_PROMPT.get());
        
        return dialog.showAndWait()
                .map(String::trim)
                .filter(name -> !name.isEmpty());
    }
}
