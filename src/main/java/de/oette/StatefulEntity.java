package de.oette;

public interface StatefulEntity<T extends Enum> {

    T getState();

    void updateState(T state);
}
