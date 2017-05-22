package de.oette;

public interface TransitionListener<T extends State> {

    void onTransition(StatefulEntity<T> entity, T from, T to);

    void onInitialState(StatefulEntity<T> entity, T initialState);
}
