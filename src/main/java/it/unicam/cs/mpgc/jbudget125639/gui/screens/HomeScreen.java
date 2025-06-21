package it.unicam.cs.mpgc.jbudget125639.gui.screens;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.gui.components.ComponentBuilderFactory;
import it.unicam.cs.mpgc.jbudget125639.gui.components.TransactionDialogComponent;
import it.unicam.cs.mpgc.jbudget125639.gui.components.TransactionGridComponent;
import it.unicam.cs.mpgc.jbudget125639.gui.services.ServiceFactory;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.function.Consumer;

/**
 * Schermata principale (Home) dell'applicazione che mostra lo storico delle transazioni
 * e permette di aggiungere nuove transazioni.
 */
public class HomeScreen extends AbstractScreen {
    
    public static final String SCREEN_ID = "home";
    
    private VBox container;
    private TransactionGridComponent transactionGrid;
    private Button addButton;
    private Consumer<StackPane> onShowDialog;
    
    /**
     * Costruttore della schermata Home.
     * 
     * @param services i servizi dell'applicazione
     * @param onShowDialog callback per mostrare dialoghi modali
     */
    public HomeScreen(ServiceFactory.ServiceBundle services, Consumer<StackPane> onShowDialog) {
        super(services, SCREEN_ID);
        this.onShowDialog = onShowDialog;
    }
    
    @Override
    public Node getNode() {
        return container;
    }
    
    @Override
    protected void createContent() {
        container = new VBox(20);
        container.setAlignment(Pos.TOP_CENTER);
        
        createHistorySection();
        createTransactionGrid();
        createAddButton();
    }
    
    private void createHistorySection() {
        Label historyLabel = new Label("Storico Transazioni");
        historyLabel.getStyleClass().add("section-title");
        
        HBox historyContainer = new HBox(historyLabel);
        historyContainer.setAlignment(Pos.CENTER_LEFT);
        historyContainer.setPadding(new Insets(10, 0, 0, 0));
        
        container.getChildren().add(historyContainer);
    }
    
    private void createTransactionGrid() {
        transactionGrid = ComponentBuilderFactory.transactionGrid().build();
        VBox.setVgrow(transactionGrid.getNode(), Priority.ALWAYS);
        container.getChildren().add(transactionGrid.getNode());
    }
    
    private void createAddButton() {
        addButton = new Button("+ Aggiungi Transazione");
        addButton.getStyleClass().add("add-button");
        
        HBox buttonContainer = new HBox(addButton);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20, 0, 0, 0));
        
        container.getChildren().add(buttonContainer);
    }
    
    @Override
    protected void setupEventHandlers() {
        addButton.setOnAction(e -> showAddTransactionDialog());
    }
    
    @Override
    protected void refreshContent() {
        if (currentView != null) {
            // Ottieni tutte le transazioni senza filtri
            var transactions = currentView.getFiltered();
            transactionGrid.updateTransactions(transactions);
        }
    }
    
    /**
     * Aggiorna le transazioni con filtri specifici.
     * 
     * @param filteredTransactions le transazioni filtrate da mostrare
     */
    public void updateTransactions(java.util.Collection<Transaction> filteredTransactions) {
        transactionGrid.updateTransactions(filteredTransactions);
    }
    
    private void showAddTransactionDialog() {
        if (currentView == null) {
            services.dialogService.showError("Nessuna vista selezionata");
            return;
        }
        
        // Create a holder for the overlay to be used in lambdas
        final StackPane[] overlayHolder = new StackPane[1];
        
        TransactionDialogComponent dialog = ComponentBuilderFactory.transactionDialog()
                .onSave(transaction -> {
                    try {
                        if (currentView instanceof User user) {
                            services.transactionService.addTransactionToUser(user, transaction);
                        } else {
                            throw new IllegalStateException("Current view is not a User");
                        }
                        refreshContent();
                        closeDialog(overlayHolder[0]);
                    } catch (Exception e) {
                        services.dialogService.showError("Errore durante il salvataggio: " + e.getMessage());
                    }
                })
                .onCancel(() -> closeDialog(overlayHolder[0]))
                .build();
        
        StackPane overlay = createDialogOverlay(dialog.getNode());
        overlayHolder[0] = overlay;
        onShowDialog.accept(overlay);
        animateDialogIn(overlay, dialog.getNode());
    }
    
    private StackPane createDialogOverlay(Node dialogContent) {
        StackPane overlay = new StackPane();
        overlay.getStyleClass().add("dialog-overlay");
        overlay.getChildren().add(dialogContent);
        
        overlay.setOnMouseClicked(e -> {
            if (e.getTarget() == overlay) {
                closeDialog(overlay);
            }
        });
        
        return overlay;
    }
    
    private void closeDialog(StackPane overlay) {
        // Rimuovi l'overlay dal container principale
        if (overlay.getParent() instanceof StackPane) {
            ((StackPane) overlay.getParent()).getChildren().remove(overlay);
        }
    }
    
    private void animateDialogIn(StackPane overlay, Node dialogContent) {
        dialogContent.setScaleX(0.9);
        dialogContent.setScaleY(0.9);
        
        ScaleTransition st = new ScaleTransition(Duration.millis(200), dialogContent);
        st.setToX(1.0);
        st.setToY(1.0);
        
        FadeTransition ft = new FadeTransition(Duration.millis(200), overlay);
        ft.setFromValue(0);
        ft.setToValue(1);
        
        ft.play();
        st.play();
    }
}
