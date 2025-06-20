package it.unicam.cs.mpgc.jbudget125639.gui.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Configuration constants for UI components.
 * Centralizes all UI-related numeric constants and styles.
 */
@RequiredArgsConstructor
@Getter
public enum UIConfig {
    
    // Window configuration
    WINDOW_WIDTH(800.0),
    WINDOW_HEIGHT(600.0),
    
    // Table configuration
    TABLE_PREFERRED_HEIGHT(250.0),

    // ListView configuration
    VIEWS_LIST_WIDTH(200.0),
    
    // Input field configuration
    AMOUNT_FIELD_WIDTH(80.0),
    CURRENCY_CHOICE_WIDTH(80.0),
    DIRECTION_CHOICE_WIDTH(100.0);

    private final double value;
    
    /**
     * Property names for table columns.
     */
    @RequiredArgsConstructor
    @Getter
    public enum Property {
        DIRECTION("direction"),
        MONEY_AMOUNT("amount"),
        DESCRIPTION("description"),
        DATE("date");
        
        private final String name;
    }
}
