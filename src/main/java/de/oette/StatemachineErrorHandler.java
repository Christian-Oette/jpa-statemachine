package de.oette;

public interface StatemachineErrorHandler<T extends State> {

    void onIllegalTransition(T from, T to);
}
