package it.unicam.cs.mpgc.jbudget125639.filters;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Filtro che accetta tutte le transazioni senza alcun criterio di selezione.
 * Utile come segnaposto o comportamento predefinito in assenza di filtri specifici.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
public class EmptyFilter implements IFilter {

    private String name;

    /**
     * Accetta qualsiasi transazione senza condizioni.
     *
     * @param transaction la transazione da valutare
     * @return sempre {@code true}
     */
    @Override
    public boolean pass(Transaction transaction) {
        return true;
    }
}
