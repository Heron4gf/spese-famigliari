package it.unicam.cs.mpgc.jbudget125639.filters.tags;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * Enum che rappresenta un insieme di tag predefiniti, ciascuno associato a un oggetto {@link PriorityTag}.
 * I tag forniscono metodi per verificare la corrispondenza con una transazione e confrontare priorità.
 */
@AllArgsConstructor
public enum NamedTag implements Tag {
    UTENZE(new PriorityTag("Utenze", 11)),
    SPORT(new PriorityTag("Sport", 10)),
    MANUTENZIONE_AUTO(new PriorityTag("Manutenzione Auto", 9)),
    ALIMENTARI(new PriorityTag("Alimentari", 8)),
    ABBIGLIAMENTO(new PriorityTag("Abbigliamento", 7)),
    INTRATTENIMENTO(new PriorityTag("Intrattenimento", 6)),
    SALUTE(new PriorityTag("Salute", 5)),
    VIAGGI(new PriorityTag("Viaggi", 4)),
    EDUCAZIONE(new PriorityTag("Educazione", 3));

    private final PriorityTag associatedTag;

    /**
     * Verifica se la transazione fornita soddisfa i criteri del tag associato.
     *
     * @param transaction la transazione da esaminare
     * @return {@code true} se la transazione è compatibile con il tag, altrimenti {@code false}
     */
    @Override
    public boolean pass(Transaction transaction) {
        return this.associatedTag.pass(transaction);
    }

    /**
     * Restituisce il {@link PriorityTag} associato a questo tag.
     *
     * @return il tag con priorità associato
     */
    public @NonNull PriorityTag asPriorityTag() {
        return associatedTag;
    }

    /**
     * Ricava un'istanza di {@code NamedTag} a partire dal nome visualizzato.
     * Il confronto è case-insensitive.
     *
     * @param displayName il nome visualizzato del tag
     * @return l'istanza di {@code NamedTag} corrispondente
     * @throws IllegalArgumentException se il nome non corrisponde ad alcun tag
     */
    public static NamedTag fromName(String displayName) {
        for (NamedTag tag : NamedTag.values()) {
            if (tag.getName().equalsIgnoreCase(displayName)) {
                return tag;
            }
        }
        throw new IllegalArgumentException("Nessun tag trovato con il nome: " + displayName);
    }

    /**
     * Restituisce un identificatore numerico unico per il tag.
     *
     * @return ID del tag
     */
    @Override
    public int getId() {
        return associatedTag.getId();
    }

    /**
     * Restituisce la priorità del tag, dove valori più alti indicano maggiore importanza.
     *
     * @return la priorità associata al tag
     */
    @Override
    public @NonNull Integer getPriority() {
        return this.associatedTag.getPriority();
    }

    /**
     * Restituisce il nome descrittivo del tag.
     *
     * @return il nome del tag
     */
    @Override
    public @NonNull String getName() {
        return this.associatedTag.getName();
    }

    /**
     * Determina se una transazione ha una priorità maggiore o uguale rispetto a questo tag.
     *
     * @param transaction la transazione da confrontare
     * @return {@code true} se la transazione ha priorità maggiore o uguale, altrimenti {@code false}
     */
    @Override
    public boolean hasMoreOrSamePriority(Transaction transaction) {
        return this.associatedTag.hasMoreOrSamePriority(transaction);
    }
}