package it.unicam.cs.mpgc.jbudget125639.gui.builders;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.function.Function;

/**
 * Utility class providing common functionality for GUI builders.
 * Contains reusable methods for creating styled components and handling common patterns.
 */
public final class BuilderUtils {

    private BuilderUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Creates a styled VBox container with the specified spacing and alignment.
     *
     * @param spacing   the spacing between children
     * @param alignment the alignment of children
     * @param styleClass optional CSS style class to apply
     * @return configured VBox
     */
    public static VBox createStyledVBox(double spacing, Pos alignment, String... styleClass) {
        VBox vbox = new VBox(spacing);
        vbox.setAlignment(alignment);
        if (styleClass.length > 0) {
            vbox.getStyleClass().addAll(styleClass);
        }
        return vbox;
    }

    /**
     * Creates a styled HBox container with the specified spacing and alignment.
     *
     * @param spacing   the spacing between children
     * @param alignment the alignment of children
     * @param styleClass optional CSS style class to apply
     * @return configured HBox
     */
    public static HBox createStyledHBox(double spacing, Pos alignment, String... styleClass) {
        HBox hbox = new HBox(spacing);
        hbox.setAlignment(alignment);
        if (styleClass.length > 0) {
            hbox.getStyleClass().addAll(styleClass);
        }
        return hbox;
    }

    /**
     * Creates a styled label with the specified text and CSS classes.
     *
     * @param text       the label text
     * @param styleClass optional CSS style classes to apply
     * @return configured Label
     */
    public static Label createStyledLabel(String text, String... styleClass) {
        Label label = new Label(text);
        if (styleClass.length > 0) {
            label.getStyleClass().addAll(styleClass);
        }
        return label;
    }

    /**
     * Validates that a required dependency is not null.
     *
     * @param dependency the dependency to validate
     * @param name       the name of the dependency for error messages
     * @param <T>        the type of the dependency
     * @return the validated dependency
     * @throws IllegalStateException if the dependency is null
     */
    public static <T> T requireNonNull(T dependency, String name) {
        if (dependency == null) {
            throw new IllegalStateException(name + " is required");
        }
        return dependency;
    }

    /**
     * Safely applies a function to an input, returning null if the function throws an IllegalArgumentException.
     * This is useful for enum parsing and other operations that may fail.
     *
     * @param function the function to apply
     * @param input    the input value
     * @param <T>      the input type
     * @param <R>      the result type
     * @return the result of the function or null if an IllegalArgumentException is thrown
     */
    public static <T, R> R tryCatch(Function<T, R> function, T input) {
        try {
            return function.apply(input);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    /**
     * Creates a section title label with consistent styling.
     *
     * @param title the title text
     * @return styled title label
     */
    public static Label createSectionTitle(String title) {
        return createStyledLabel(title, "section-title");
    }

    /**
     * Creates a dialog title label with consistent styling.
     *
     * @param title the title text
     * @return styled dialog title label
     */
    public static Label createDialogTitle(String title) {
        return createStyledLabel(title, "dialog-title");
    }
}
