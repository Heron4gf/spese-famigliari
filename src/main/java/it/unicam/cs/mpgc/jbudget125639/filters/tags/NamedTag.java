package it.unicam.cs.mpgc.jbudget125639.filters.tags;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import lombok.AllArgsConstructor;

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