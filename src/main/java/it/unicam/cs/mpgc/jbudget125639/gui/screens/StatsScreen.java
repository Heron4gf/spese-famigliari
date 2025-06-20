package it.unicam.cs.mpgc.jbudget125639.gui.screens;

import it.unicam.cs.mpgc.jbudget125639.gui.components.StatsView;
import it.unicam.cs.mpgc.jbudget125639.gui.services.ServiceFactory;
import javafx.scene.Node;

/**
 * Schermata delle statistiche che mostra grafici e analisi delle transazioni dell'utente.
 */
public class StatsScreen extends AbstractScreen {
    
    public static final String SCREEN_ID = "stats";
    
    private StatsView statsView;
    
    /**
     * Costruttore della schermata Stats.
     * 
     * @param services i servizi dell'applicazione
     */
    public StatsScreen(ServiceFactory.ServiceBundle services) {
        super(services, SCREEN_ID);
    }
    
    @Override
    public Node getNode() {
        return statsView.getNode();
    }
    
    @Override
    protected void createContent() {
        statsView = new StatsView();
    }
    
    @Override
    protected void refreshContent() {
        if (currentUser != null) {
            statsView.updateData(currentUser.getTransactions());
        }
    }
    
    @Override
    public void onActivate() {
        super.onActivate();
        // Aggiorna sempre i dati quando la schermata diventa attiva
        // per assicurarsi che le statistiche siano aggiornate
        refreshContent();
    }
    
    /**
     * Aggiorna le statistiche con transazioni filtrate.
     * 
     * @param filteredTransactions le transazioni filtrate da analizzare
     */
    public void updateStats(java.util.Collection<it.unicam.cs.mpgc.jbudget125639.entities.Transaction> filteredTransactions) {
        statsView.updateData(filteredTransactions);
    }
}
