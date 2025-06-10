package it.unicam.cs.mpgc.jbudget125639.filters.tags;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum NamedTag implements Tag {
    UTENZE(new PriorityTag(11, "Utenze")),
    SPORT(new PriorityTag(10, "Sport")),
    MANUTENZIONE_AUTO(new PriorityTag(9, "Manutenzione Auto")),
    ALIMENTARI(new PriorityTag(8, "Alimentari")),
    ABBIGLIAMENTO(new PriorityTag(7, "Abbigliamento")),
    INTRATTENIMENTO(new PriorityTag(6, "Intrattenimento")),
    SALUTE(new PriorityTag(5, "Salute")),
    VIAGGI(new PriorityTag(4, "Viaggi")),
    EDUCAZIONE(new PriorityTag(3, "Educazione"));

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