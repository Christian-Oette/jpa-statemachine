package de.oette;

public interface StatefulEntity<T extends State> {

    T getState();

    void updateState(T state);
}
