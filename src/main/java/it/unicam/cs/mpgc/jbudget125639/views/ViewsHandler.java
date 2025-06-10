package it.unicam.cs.mpgc.jbudget125639.views;

import java.util.Collection;

public interface ViewsHandler {
    Collection<View> getViews();
    View getView(String name);
}
