package it.unicam.cs.mpgc.jbudget125639.filters.tags;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PriorityTag implements Tag {
    private Integer priority;
    private String name;

    @Override
    public boolean hasMoreOrSamePriority(Transaction transaction) {
        return this.priority >= transaction.maxTagPriority();
    }

    @Override
    public boolean pass(Transaction transaction) {
        return transaction.getAssociatedTags().contains(this);
    }
}
