package it.unicam.cs.mpgc.jbudget125639.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.Tag;
import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import lombok.*;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
@DatabaseTable(tableName = "Transactions")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@ToString
@EqualsAndHashCode
public class Transaction {

    public Transaction(@NonNull TransactionDirection direction, @NonNull MoneyAmount amount, @NonNull String description, @NonNull Collection<TransactionTag> transactionTags) {
        this(direction, amount, description);
        this.transactionTags = transactionTags;
    }

    @ToString.Exclude
    @DatabaseField(foreign = true, columnName = "user_id", foreignAutoRefresh = true)
    @Setter(AccessLevel.PACKAGE)
    private User user;

    @ToString.Exclude
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
    @NonNull
    private Date date = Date.from(Instant.now());

    @ForeignCollectionField(eager = true)
    @NonNull
    private Collection<TransactionTag> transactionTags = Collections.emptyList();

    public int maxTagPriority() {
        return getAssociatedTags().mapToInt(Tag::getPriority).max().orElse(0);
    }

    public @NonNull Stream<Tag> getAssociatedTags() {
        return transactionTags.stream().map(TransactionTag::getTag);
    }
}