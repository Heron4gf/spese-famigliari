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
import java.util.Date;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
@DatabaseTable(tableName = "Transactions")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
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

    @ForeignCollectionField(eager = true)
    private Collection<TransactionTag> transactionTags;

    public int maxTagPriority() {
        return getAssociatedTags().mapToInt(Tag::getPriority).max().orElse(0);
    }

    public Stream<Tag> getAssociatedTags() {
        if (transactionTags == null) {
            return Stream.empty();
        }
        return transactionTags.stream().map(TransactionTag::getTag);
    }
}