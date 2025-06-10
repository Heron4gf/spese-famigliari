package it.unicam.cs.mpgc.jbudget125639.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.Tag;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import lombok.*;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;

@Getter
@RequiredArgsConstructor
@DatabaseTable(tableName = "Transactions")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class Transaction {

    @DatabaseField(foreign = true, columnName = "user_id", foreignAutoRefresh = true)
    @Setter(AccessLevel.PACKAGE)
    private User user;

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    @NonNull
    private TransactionDirection direction;

    @DatabaseField(canBeNull = false, dataType = DataType.SERIALIZABLE)
    @NonNull
    private MoneyAmount amount;

    @DatabaseField(canBeNull = false)
    @NonNull
    private String description;

    @DatabaseField(canBeNull = false)
    private Date date = Date.from(Instant.now());

    @DatabaseField(canBeNull = true)
    @NonNull
    private Collection<Tag> associatedTags;

    public int maxTagPriority() {
        return this.associatedTags.stream()
                .mapToInt(Tag::getPriority)
                .max()
                .orElse(0);
    }

}