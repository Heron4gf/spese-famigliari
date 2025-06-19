package it.unicam.cs.mpgc.jbudget125639.gui;

import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.modules.GlobalModule;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import it.unicam.cs.mpgc.jbudget125639.views.View;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.stream.Collectors;

/**
 * Controller per la vista principale (MainView.fxml).
 * Gestisce la visualizzazione dei dati e le interazioni dell'utente.
 */
public class MainViewController {

    @FXML
    private ListView<String> viewsListView;

    @FXML
    private Label detailsLabel;

    private ModulesManager modulesManager;
    private GlobalModule globalModule;

    /**
     * Metodo chiamato da BudgetApp per fornire l'accesso al ModulesManager.
     * Dopo l'iniezione, inizializza i dati.
     */
    public void initializeManager(ModulesManager manager) {
        this.modulesManager = manager;
        this.globalModule = modulesManager.getModule(GlobalModule.class);

        if (globalModule == null || globalModule.getGlobal() == null) {
            detailsLabel.setText("Errore: GlobalModule non è stato caricato correttamente.");
            return;
        }

        populateViewsList();
        setupListViewListener();
    }

    /**
     * Popola la ListView con i nomi delle View trovate nel GlobalModule.
     */
    private void populateViewsList() {
        viewsListView.setItems(
                globalModule.getGlobal().getViews()
                        .stream()
                        .map(View::getName)
                        .collect(Collectors.toCollection(FXCollections::observableArrayList))
        );
    }

    /**
     * Imposta un listener per mostrare dettagli quando un elemento della lista viene selezionato.
     */
    private void setupListViewListener() {
        viewsListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        View selectedView = globalModule.getGlobal().getView(newVal);
                        if (selectedView instanceof User) {
                            detailsLabel.setText("Dettagli Utente: " + selectedView.getName());
                        } else {
                            detailsLabel.setText("Dettagli Vista: " + selectedView.getName());
                        }
                    }
                }
        );
    }
}