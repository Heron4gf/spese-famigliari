package it.unicam.cs.mpgc.jbudget125639.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.PriorityTag;
import lombok.*;

/*
 * Classe che rappresenta la relazione molti a molti tra transazione e priority Tag
 */
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@DatabaseTable(tableName = "transaction_tags")
@EqualsAndHashCode
public class TransactionTag {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "transaction_id")
    @NonNull
    @EqualsAndHashCode.Exclude
    private Transaction transaction;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "tag_id")
    @NonNull
    @EqualsAndHashCode.Exclude
    private PriorityTag tag;

}