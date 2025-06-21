package it.unicam.cs.mpgc.jbudget125639.gui.builders;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.experimental.UtilityClass;

/**
 * Classe di utilità che fornisce funzionalità comuni per i costruttori di interfacce grafiche.
 * Contiene metodi riutilizzabili per creare componenti stilizzati e gestire pattern ricorrenti.
 */
@UtilityClass // genera un costruttore privato e rende i metodi statici
public final class BuilderUtils {

    /**
     * Crea un contenitore VBox stilizzato con lo spazio e l'allineamento specificati.
     *
     * @param spacing     lo spazio tra i nodi figli
     * @param alignment   l'allineamento dei nodi figli
     * @param styleClass  classe CSS opzionale da applicare
     * @return VBox configurato
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
     * Crea un'etichetta stilizzata con il testo e le classi CSS specificate.
     *
     * @param text        il testo dell'etichetta
     * @param styleClass  classi CSS opzionali da applicare
     * @return Label configurata
     */
    public static Label createStyledLabel(String text, String... styleClass) {
        Label label = new Label(text);
        if (styleClass.length > 0) {
            label.getStyleClass().addAll(styleClass);
        }
        return label;
    }

}
