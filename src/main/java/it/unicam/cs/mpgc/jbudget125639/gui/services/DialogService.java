package it.unicam.cs.mpgc.jbudget125639.gui.services;

import it.unicam.cs.mpgc.jbudget125639.gui.constants.UILabel;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import lombok.NonNull;

import java.util.Optional;

/**
 * Servizio responsabile della gestione delle finestre di dialogo
 * e delle interazioni con l'utente.
 * Segue il principio di responsabilità singola gestendo solo operazioni di dialogo.
 */
public class DialogService {

    /**
     * Mostra una finestra di dialogo di errore con l'eccezione fornita.
     *
     * @param throwable l'errore da visualizzare
     */
    public void showError(@NonNull Throwable throwable) {
        showError(throwable.getMessage());
    }

    /**
     * Mostra una finestra di dialogo di errore con il messaggio fornito.
     *
     * @param message il messaggio di errore da visualizzare
     */
    public void showError(@NonNull String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(UILabel.ERROR_TITLE.get());
        alert.setHeaderText(UILabel.ERROR_HEADER.get());
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Mostra una finestra di dialogo per l'inserimento del nome di un nuovo utente.
     *
     * @return Optional contenente il nome inserito, vuoto se annullato
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