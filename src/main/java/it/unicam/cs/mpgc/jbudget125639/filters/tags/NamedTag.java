package it.unicam.cs.mpgc.jbudget125639.filters.tags;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import lombok.AllArgsConstructor;

/**
 * Enum che rappresenta un insieme di tag predefiniti con priorità associate.
 * Ogni tag è delegato a un oggetto {@link Tag} specifico, implementando comportamenti
 * come la verifica della corrispondenza con una transazione e il confronto di priorità.
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

    private final Tag associatedTag;

    /**
     * Verifica se la transazione soddisfa il criterio del tag associato.
     *
     * @param transaction la transazione da valutare
     * @return {@code true} se la transazione ha il tag, altrimenti {@code false}
     */
    @Override
    public boolean pass(Transaction transaction) {
        return this.associatedTag.pass(transaction);
    }

    @Override
    public Integer getPriority() {
        return this.associatedTag.getPriority();
    }

    @Override
    public String getName() {
        return this.associatedTag.getName();
    }

    @Override
    public boolean hasMoreOrSamePriority(Transaction transaction) {
        return this.associatedTag.hasMoreOrSamePriority(transaction);
    }
}