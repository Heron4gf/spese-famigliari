package it.unicam.cs.spesefamigliari.views;

import java.util.Collection;

public interface ViewsHandler {
    Collection<View> getViews();
    View getView(String name);
}
