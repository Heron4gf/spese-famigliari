package it.unicam.cs.mpgc.jbudget125639.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.PriorityTag;
import lombok.*;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@DatabaseTable(tableName = "transaction_tags")
@ToString
@EqualsAndHashCode
public class TransactionTag {

    @DatabaseField(generatedId = true)
    @ToString.Exclude
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "transaction_id")
    @NonNull
    private Transaction transaction;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "tag_id")
    @NonNull
    private PriorityTag tag;

}