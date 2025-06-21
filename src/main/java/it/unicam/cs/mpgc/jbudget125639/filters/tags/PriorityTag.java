package it.unicam.cs.mpgc.jbudget125639.filters.tags;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import lombok.*;

/**
 * Rappresenta un tag con priorità associata, utilizzato per classificare le transazioni.
 * Ogni {@code PriorityTag} ha un nome univoco e un valore di priorità numerico.
 * I tag possono essere confrontati con transazioni per verificarne la presenza
 * e determinare l'importanza relativa rispetto ad altri tag.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@DatabaseTable(tableName = "tags")
public class PriorityTag implements Tag {

    /**
     * Identificatore univoco generato automaticamente per il tag.
     */
    @DatabaseField(generatedId = true)
    @Getter
    private int id;

    /**
     * Nome descrittivo e univoco del tag.
     */
    @DatabaseField(canBeNull = false, unique = true)
    @NonNull
    private String name;

    /**
     * Priorità numerica del tag; valori più alti indicano maggiore rilevanza.
     */
    @DatabaseField(canBeNull = false)
    @NonNull
    private Integer priority;

    /**
     * Verifica se la priorità del tag è maggiore o uguale rispetto alla massima priorità
     * dei tag associati alla transazione specificata.
     *
     * @param transaction la transazione con cui confrontare la priorità
     * @return {@code true} se la priorità di questo tag è maggiore o uguale, altrimenti {@code false}
     */
    @Override
    public boolean hasMoreOrSamePriority(Transaction transaction) {
        return this.priority >= transaction.maxTagPriority();
    }

    /**
     * Verifica se la transazione è etichettata con questo tag, confrontando il nome.
     *
     * @param transaction la transazione da analizzare
     * @return {@code true} se tra i tag associati alla transazione è presente questo tag, altrimenti {@code false}
     */
    @Override
    public boolean pass(Transaction transaction) {
        return transaction.getAssociatedTags()
                .anyMatch(tag -> tag.getName().equals(this.name));
    }
}