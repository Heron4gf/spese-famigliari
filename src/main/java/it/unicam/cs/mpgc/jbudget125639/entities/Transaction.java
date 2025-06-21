package it.unicam.cs.mpgc.jbudget125639.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.PriorityTag;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.Tag;
import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
@DatabaseTable(tableName = "Transactions")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@ToString
@EqualsAndHashCode
public class Transaction {

    /**
     * Crea una nuova transazione con tutti i dettagli specificati.
     *
     * @param direction la direzione della transazione (entrata o uscita).
     * @param amount l'importo monetario della transazione.
     * @param description la descrizione testuale della transazione.
     * @param tags la collezione di tag da associare alla transazione.
     */
    public Transaction(@NonNull TransactionDirection direction, @NonNull MoneyAmount amount, @NonNull String description, @NonNull Collection<PriorityTag> tags) {
        this(direction, amount, description);

        this.transactionTags = tags.stream()
                .map(tag -> new TransactionTag(this, tag))
                .collect(Collectors.toList());
    }

    /**
     * Crea una nuova transazione con tutti i dettagli specificati.
     *
     * @param direction la direzione della transazione (entrata o uscita).
     * @param moneyAmount l'importo monetario della transazione.
     * @param description la descrizione testuale della transazione.
     * @param date la data della transazione (anche futura)
     * @param selectedTags la collezione di tag da associare alla transazione.
     */
    public Transaction(String description, MoneyAmount moneyAmount, TransactionDirection direction, Date date, List<NamedTag> selectedTags) {
        this(direction, moneyAmount, description);
        this.date = date;

        this.transactionTags = selectedTags.stream()
                .map(tag -> new TransactionTag(this, tag.asPriorityTag()))
                .collect(Collectors.toList());
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
    @NotBlank(message = "La descrizione non può essere vuota.")
    @Size(min = 3, max = 100, message = "La descrizione deve contenere tra 3 e 100 caratteri.")
    private String description;


    @DatabaseField(canBeNull = false)
    @NonNull
    private Date date = Date.from(Instant.now());

    @ForeignCollectionField(eager = true)
    @NonNull
    @EqualsAndHashCode.Exclude
    private Collection<TransactionTag> transactionTags = Collections.emptyList();

    /**
     * Calcola la priorità massima tra tutti i tag associati a questa transazione.
     *
     * @return il valore di priorità più alto, o 0 se non ci sono tag.
     */
    public int maxTagPriority() {
        return getAssociatedTags().mapToInt(Tag::getPriority).max().orElse(0);
    }

    /**
     * Restituisce uno stream di tutti i tag associati a questa transazione.
     *
     * @return uno stream contenente gli oggetti Tag associati.
     */
    public @NonNull Stream<Tag> getAssociatedTags() {
        return transactionTags.stream().map(TransactionTag::getTag);
    }
}