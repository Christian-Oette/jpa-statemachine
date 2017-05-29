package de.oette;

public interface StatemachineErrorHandler<T extends Enum> {

    void onIllegalTransition(T from, T to);

    void onIllegalEvent(Event event, T from);
}
