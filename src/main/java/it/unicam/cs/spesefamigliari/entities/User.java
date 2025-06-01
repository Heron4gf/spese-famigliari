package it.unicam.cs.spesefamigliari.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import it.unicam.cs.spesefamigliari.filters.IFilter;
import it.unicam.cs.spesefamigliari.views.AbstractView;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
@Data
@RequiredArgsConstructor
@DatabaseTable(tableName = "Users")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class User extends AbstractView {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false, unique = true)
    @NonNull
    private String name;

    @ForeignCollectionField
    private Collection<Transaction> transactions = new LinkedList<>();

    @Override
    public double total(IFilter... filters) {
        return transactions.stream()
                .filter(transaction -> Arrays.stream(filters).allMatch(filter -> filter.pass(transaction)))
                .mapToDouble(transaction -> transaction.getAmount().toDouble())
                .sum();
    }

    public void addTransaction(@NonNull Transaction transaction) {
        transactions.add(transaction);
        transaction.setUser(this); // Maintain bidirectional relationship
    }

    @Override
    public Collection<Transaction> getFiltered(@NonNull IFilter... filters) {
        return transactions.stream()
                .filter(transaction -> Arrays.stream(filters).allMatch(filter -> filter.pass(transaction)))
                .collect(Collectors.toList());
    }
}