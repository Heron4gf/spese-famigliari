package it.unicam.cs.mpgc.jbudget125639.gui.components;

import lombok.experimental.UtilityClass;

/**
 * Classe factory che fornisce metodi statici per creare costruttori di componenti.
 * Questa classe rappresenta il punto d’ingresso principale per utilizzare il pattern builder
 * nella creazione di componenti GUI in modo fluente e configurabile.
 */
@UtilityClass // costruttore privato + metodi statici
public final class ComponentBuilderFactory {

    /**
     * Crea un nuovo costruttore di BalanceBox.
     *
     * @return una nuova istanza di BalanceBoxBuilder
     */
    public BalanceBoxComponent.BalanceBoxComponentBuilder balanceBox() {
        return BalanceBoxComponent.builder();
    }

    /**
     * Crea un nuovo costruttore di HeaderBar.
     *
     * @return una nuova istanza di HeaderBarBuilder
     */
    public HeaderBarComponent.HeaderBarComponentBuilder headerBar() {
        return HeaderBarComponent.builder();
    }

    /**
     * Crea un nuovo costruttore di NavigationBar.
     *
     * @return una nuova istanza di NavigationBarBuilder
     */
    public NavigationBarComponent.NavigationBarComponentBuilder navigationBar() {
        return NavigationBarComponent.builder();
    }

    /**
     * Crea un nuovo costruttore di TransactionDialog.
     *
     * @return una nuova istanza di TransactionDialogBuilder
     */
    public TransactionDialogComponent.TransactionDialogComponentBuilder transactionDialog() {
        return TransactionDialogComponent.builder();
    }

    /**
     * Crea un nuovo costruttore di TransactionGrid.
     *
     * @return una nuova istanza di TransactionGridBuilder
     */
    public TransactionGridComponent.TransactionGridComponentBuilder transactionGrid() {
        return TransactionGridComponent.builder();
    }

    /**
     * Crea un nuovo costruttore di StatsView.
     *
     * @return una nuova istanza di StatsViewBuilder
     */
    public StatsViewComponent.StatsViewComponentBuilder statsView() {
        return StatsViewComponent.builder();
    }
}