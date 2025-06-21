package it.unicam.cs.mpgc.jbudget125639.gui.constants;

public enum UILabel {
    ERROR_TITLE("Errore"),
    ERROR_HEADER("Si è verificato un errore"),
    CONFIRMATION_TITLE("Conferma"),
    NEW_USER_TITLE("Nuovo Utente"),
    NEW_USER_HEADER("Aggiungi un nuovo utente"),
    NEW_USER_PROMPT("Nome Utente:");

    private final String text;

    UILabel(String text) {
        this.text = text;
    }

    public String get() {
        return text;
    }
}