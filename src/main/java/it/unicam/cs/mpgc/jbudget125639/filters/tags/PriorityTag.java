package it.unicam.cs.mpgc.jbudget125639.filters.tags;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@DatabaseTable(tableName = "tags")
public class PriorityTag implements Tag {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, unique = true)
    @NonNull
    private String name;

    @DatabaseField(canBeNull = false)
    @NonNull
    private Integer priority;

    @Override
    public boolean hasMoreOrSamePriority(Transaction transaction) {
        return this.priority >= transaction.maxTagPriority();
    }

    @Override
    public boolean pass(Transaction transaction) {
        return transaction.getAssociatedTags()
                .anyMatch(tag -> tag.getName().equals(this.name));
    }
}