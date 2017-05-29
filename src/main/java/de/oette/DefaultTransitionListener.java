package de.oette;

public class DefaultTransitionListener<T extends Enum> implements TransitionListener<T> {

    public void onTransition(StatefulEntity<T> entity, T from, T to) {
        //Do nothing
    }

    public void onInitialState(StatefulEntity<T> entity, T initialState) {
        //Do nothing
    }
}
