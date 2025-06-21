package it.unicam.cs.mpgc.jbudget125639.modules;

import it.unicam.cs.mpgc.jbudget125639.gui.builders.BalanceBoxBuilder;
import it.unicam.cs.mpgc.jbudget125639.gui.builders.ComponentBuilderFactory;
import it.unicam.cs.mpgc.jbudget125639.gui.builders.HeaderBarBuilder;
import it.unicam.cs.mpgc.jbudget125639.gui.builders.NavigationBarBuilder;
import it.unicam.cs.mpgc.jbudget125639.gui.builders.StatsViewBuilder;
import it.unicam.cs.mpgc.jbudget125639.gui.builders.TransactionDialogBuilder;
import it.unicam.cs.mpgc.jbudget125639.gui.builders.TransactionGridBuilder;
import it.unicam.cs.mpgc.jbudget125639.gui.screens.ScreenManager;
import it.unicam.cs.mpgc.jbudget125639.gui.services.ServiceFactory;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.RequiresModulesManagerModule;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;

import java.util.function.Consumer;

/**
 * Module that provides component building capabilities using the Builder pattern.
 * This module encapsulates the creation logic for GUI components and provides
 * convenient methods for creating pre-configured components.
 */
public class ComponentBuilderModule extends RequiresModulesManagerModule {
    
    private ServiceFactory.ServiceBundle services;
    
    public ComponentBuilderModule(ModulesManager modulesManager) {
        super(modulesManager);
    }
    
    @Override
    public String name() {
        return "ComponentBuilder";
    }
    
    @Override
    public void internalLoad() throws Exception {
        // Module is ready to use after loading
    }
    
    @Override
    public void internalUnload() throws Exception {
        // Nothing to clean up
    }
    
    /**
     * Sets the service bundle for components that require services.
     * 
     * @param services the service bundle
     */
    public void setServices(ServiceFactory.ServiceBundle services) {
        this.services = services;
    }
    
    /**
     * Creates a BalanceBox with default configuration.
     * 
     * @return a new BalanceBox instance
     */
    public BalanceBoxBuilder.BalanceBoxComponent createBalanceBox() {
        return ComponentBuilderFactory.balanceBox().build();
    }
    
    /**
     * Creates a BalanceBox with initial balance.
     * 
     * @param initialBalance the initial balance to display
     * @return a new BalanceBox instance
     */
    public BalanceBoxBuilder.BalanceBoxComponent createBalanceBox(MoneyAmount initialBalance) {
        return ComponentBuilderFactory.balanceBox()
                .withInitialBalance(initialBalance)
                .build();
    }
    
    /**
     * Creates a HeaderBar with required dependencies.
     * 
     * @param onUserChanged callback for user changes
     * @param onFiltersChanged callback for filter changes
     * @return a new HeaderBar instance
     */
    public HeaderBarBuilder.HeaderBarComponent createHeaderBar(Consumer<String> onUserChanged, Runnable onFiltersChanged) {
        if (modulesManager == null) {
            throw new IllegalStateException("ModulesManager not set");
        }
        
        return ComponentBuilderFactory.headerBar()
                .withUserChangeHandler(onUserChanged)
                .withFiltersChangeHandler(onFiltersChanged)
                .withModulesManager(modulesManager)
                .build();
    }
    
    /**
     * Creates a NavigationBar with required dependencies.
     * 
     * @param screenManager the screen manager
     * @return a new NavigationBar instance
     */
    public NavigationBarBuilder.NavigationBarComponent createNavigationBar(ScreenManager screenManager) {
        return ComponentBuilderFactory.navigationBar()
                .withScreenManager(screenManager)
                .build();
    }
    
    /**
     * Creates a TransactionGrid with default configuration.
     * 
     * @return a new TransactionGrid instance
     */
    public TransactionGridBuilder.TransactionGridComponent createTransactionGrid() {
        return ComponentBuilderFactory.transactionGrid().build();
    }
    
    /**
     * Creates a StatsView with default configuration.
     * 
     * @return a new StatsView instance
     */
    public StatsViewBuilder.StatsViewComponent createStatsView() {
        return ComponentBuilderFactory.statsView().build();
    }
    
    /**
     * Creates a TransactionDialog with save and cancel handlers.
     * 
     * @param onSave callback for saving transactions
     * @param onCancel callback for cancelling the dialog
     * @return a new TransactionDialog instance
     */
    public TransactionDialogBuilder.TransactionDialogComponent createTransactionDialog(
            Consumer<TransactionDialogBuilder.TransactionDialogComponent.TransactionData> onSave,
            Runnable onCancel) {
        
        return ComponentBuilderFactory.transactionDialog()
                .withSaveHandler(onSave)
                .withCancelHandler(onCancel)
                .build();
    }
    
    /**
     * Provides access to the component builder factory for advanced usage.
     * 
     * @return the ComponentBuilderFactory class
     */
    public ComponentBuilderFactory getBuilderFactory() {
        return ComponentBuilderFactory.class.cast(null); // Static access only
    }
}
