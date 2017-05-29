package de.oette;

public class DefaultStatemachineErrorHandler<T extends Enum> implements StatemachineErrorHandler<T> {

    public void onIllegalTransition(T from, T to) {
        throw new IllegalStateException(String.format("Illegal transition from %s to %s", from, to));
    }

    public void onIllegalEvent(Event event, T from) {
        throw new IllegalStateException(String.format("Illegal transition with event %s from source state %s", event, from));
    }
}
