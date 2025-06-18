package it.unicam.cs.mpgc.jbudget125639.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.PriorityTag;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@DatabaseTable(tableName = "transaction_tags")
public class TransactionTag {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "transaction_id")
    private Transaction transaction;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "tag_id")
    private PriorityTag tag;

    public TransactionTag(Transaction transaction, PriorityTag tag) {
        this.transaction = transaction;
        this.tag = tag;
    }
}