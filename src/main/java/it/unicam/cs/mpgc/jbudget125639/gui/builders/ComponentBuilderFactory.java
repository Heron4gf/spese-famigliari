package it.unicam.cs.mpgc.jbudget125639.gui.builders;

/**
 * Factory class that provides static methods to create component builders.
 * This class serves as the main entry point for using the builder pattern
 * to create GUI components in a fluent and configurable way.
 */
public final class ComponentBuilderFactory {
    
    private ComponentBuilderFactory() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Creates a new BalanceBox builder.
     * 
     * @return a new BalanceBoxBuilder instance
     */
    public static BalanceBoxBuilder balanceBox() {
        return new BalanceBoxBuilder();
    }
    
    /**
     * Creates a new HeaderBar builder.
     * 
     * @return a new HeaderBarBuilder instance
     */
    public static HeaderBarBuilder headerBar() {
        return new HeaderBarBuilder();
    }
    
    /**
     * Creates a new NavigationBar builder.
     * 
     * @return a new NavigationBarBuilder instance
     */
    public static NavigationBarBuilder navigationBar() {
        return new NavigationBarBuilder();
    }
    
    /**
     * Creates a new TransactionDialog builder.
     * 
     * @return a new TransactionDialogBuilder instance
     */
    public static TransactionDialogBuilder transactionDialog() {
        return new TransactionDialogBuilder();
    }
    
    /**
     * Creates a new TransactionGrid builder.
     * 
     * @return a new TransactionGridBuilder instance
     */
    public static TransactionGridBuilder transactionGrid() {
        return new TransactionGridBuilder();
    }
    
    /**
     * Creates a new StatsView builder.
     * 
     * @return a new StatsViewBuilder instance
     */
    public static StatsViewBuilder statsView() {
        return new StatsViewBuilder();
    }
}
