package it.unicam.cs.mpgc.jbudget125639.views;

import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
public class Global extends AbstractView implements ViewsHandler {

    @NonNull private Collection<User> users = new HashSet<>();

    @Override
    public double total(IFilter... filters) {
        return users.stream()
                .mapToDouble(user -> user.total(filters))
                .sum();
    }

    @Override
    public String getName() {
        return "global";
    }


    @Override
    public Collection<Transaction> getFiltered(@NonNull IFilter... filters) {
        return users.stream()
                .map(user -> user.getFiltered(filters))
                .flatMap(Collection::stream)
                .toList();
    }

    @Override
    public Collection<View> getViews() {
        return Stream.concat(users.stream(), Stream.of(this))
                .collect(Collectors.toSet());
    }

    @Override
    public View getView(String name) {
        return getViews().stream()
                .filter(view -> view.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

}
