package it.unicam.cs.mpgc.jbudget125639.gui.constants;

public enum UILabel {

    // Window
    WINDOW_TITLE("JBudget"),

    // Messages
    ERROR_TITLE("Errore"),
    ERROR_HEADER("Si è verificato un errore"),
    CONFIRMATION_TITLE("Conferma"),
    DELETE_TRANSACTION_MESSAGE("Sei sicuro di voler eliminare la transazione?"),
    DELETE_VIEW_MESSAGE_FORMAT("Sei sicuro di voler eliminare la vista '%s'?"),
    NEW_USER_TITLE("Nuovo Utente"),
    NEW_USER_HEADER("Aggiungi un nuovo utente"),
    NEW_USER_PROMPT("Nome Utente:"),

    VIEW_DETAILS_FORMAT("Dettagli per: %s"),
    BALANCE_FORMAT("Saldo: %.2f EUR"),

    // Prompts
    DESCRIPTION_PROMPT("Descrizione"),
    AMOUNT_PROMPT("Importo");

    private final String text;

    UILabel(String text) {
        this.text = text;
    }

    public String get() {
        return text;
    }
}