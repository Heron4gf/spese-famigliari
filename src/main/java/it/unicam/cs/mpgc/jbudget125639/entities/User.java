package it.unicam.cs.mpgc.jbudget125639.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;
import it.unicam.cs.mpgc.jbudget125639.views.AbstractView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Il nome non può essere vuoto.")
    @Size(min = 3, max = 50, message = "Il nome deve contenere tra 3 e 50 caratteri.")
    private String name;


    @ForeignCollectionField
    private Collection<Transaction> transactions = new LinkedList<>();

    @Override
    public double total(IFilter... filters) {
        return transactions.stream()
                .filter(transaction -> Arrays.stream(filters).allMatch(filter -> filter.pass(transaction)))
                .mapToDouble(transaction -> transaction.getAmount().getValue())
                .sum();
    }

    public void addTransaction(@NonNull Transaction transaction) {
        transactions.add(transaction);
        transaction.setUser(this);
    }

    @Override
    public Collection<Transaction> getFiltered(@NonNull IFilter... filters) {
        return transactions.stream()
                .filter(transaction -> Arrays.stream(filters).allMatch(filter -> filter.pass(transaction)))
                .collect(Collectors.toList());
    }

    @Override
    public void removeTransaction(@NonNull Transaction transaction) {
        transactions.remove(transaction);
    }
}