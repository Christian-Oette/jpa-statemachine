package de.oette;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statemachine<T extends State> {

    private final Map<Event, T> events;
    private T initialState;
    private TransitionListener<T> transitionListener;
    private StatemachineErrorHandler<T> errorHandler;
    private Map<T, List<T>> allowedTransitions;

    public Statemachine(T initialState,
                        TransitionListener<T> transitionListener,
                        StatemachineErrorHandler<T> exceptionHandler,
                        Map<T, List<T>> allowedTransitions,
                        Map<Event, T> events) {
        this.initialState = initialState;
        this.transitionListener = transitionListener;
        this.errorHandler = exceptionHandler;
        this.allowedTransitions = allowedTransitions;
        this.events = events;
    }

    public void updateState(StatefulEntity<T> entity, T newState) {
        T currentState = entity.getState();
        assertTransitionAllowed(currentState, newState);
        entity.updateState(newState);

        if (transitionListener!=null) {
            transitionListener.onTransition(entity, currentState, newState);
        }
    }

    public boolean isTransitionAllowed(T currentState, T newState) {
        if (!allowedTransitions.containsKey(currentState)) {
            return false;
        }
        List<T> transitions = allowedTransitions.get(currentState);
        return transitions!=null && transitions.contains(newState);
    }

    public void init(StatefulEntity<T> entity) {
        entity.updateState(initialState);
        if (transitionListener!=null) {
            transitionListener.onInitialState(entity, initialState);
        }
    }

    private void assertTransitionAllowed(T currentState, T newState) {
        boolean isTransitionError = !isTransitionAllowed(currentState, newState);
        if (isTransitionError && errorHandler !=null) {
            errorHandler.onIllegalTransition(currentState, newState);
        } else if (isTransitionError){
            throw new IllegalStateException(String.format("Transition from %s to %s is not allowed", currentState, newState));
        }
    }

    public void fireEvent(StatefulEntity<T> entity, Event event) {
        T nextState = this.events.get(event);
        this.updateState(entity, nextState);
    }

    public static class Builder<T extends State> {

        private T initialState;
        private StatemachineErrorHandler<T> exceptionHandler;
        private TransitionListener<T> transitionListener;
        private Map<T, List<T>> allowedTransitions = new HashMap<T, List<T>>();
        private Map<Event, T> events = new HashMap<Event, T>();

        public Builder<T> withInitialState(T state) {
            this.initialState = state;
            return this;
        }

        public Builder<T> withAllowedTransition(T from, T to) {
            List<T> transitions = this.allowedTransitions.get(from);
            if (transitions==null) {
                transitions = new ArrayList<T>();
                this.allowedTransitions.put(from, transitions);
            }
            transitions.add(to);
            return this;
        }

        public Builder<T> withExceptionHandler(StatemachineErrorHandler<T> exceptionHandler) {
            this.exceptionHandler = exceptionHandler;
            return this;
        }

        public Builder<T> withTransitionListener(TransitionListener<T> transitionListener) {
            this.transitionListener = transitionListener;
            return this;
        }

        public Statemachine<T> build() {
            return new Statemachine<T>(initialState, transitionListener, exceptionHandler, allowedTransitions, events);
        }

        public Builder<T> withEvent(Event event, T state) {
            this.events.put(event, state);
            return this;
        }
    }
}
