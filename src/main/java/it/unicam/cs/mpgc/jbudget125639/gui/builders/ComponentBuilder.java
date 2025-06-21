package it.unicam.cs.mpgc.jbudget125639.gui.builders;

/**
 * Base interface for component builders that follow the Builder pattern.
 * Provides a fluent API for creating GUI components with optional configuration.
 * 
 * @param <T> the type of component being built
 * @param <B> the type of builder (for method chaining)
 */
public interface ComponentBuilder<T extends NodeBuilder, B extends ComponentBuilder<T, B>> {
    
    /**
     * Builds and returns the configured component.
     * 
     * @return the built component
     */
    T build();
    
    /**
     * Returns the builder instance for method chaining.
     * 
     * @return this builder instance
     */
    @SuppressWarnings("unchecked")
    default B self() {
        return (B) this;
    }
}
