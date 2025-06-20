package it.unicam.cs.mpgc.jbudget125639.views;

import lombok.NonNull;

import java.util.Collection;

public interface ViewsHandler {
    Collection<View> getViews();
    View getView(@NonNull String name);
    void addView(@NonNull View view);
}
