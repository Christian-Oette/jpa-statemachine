package de.oette;

public class Statemachine<T extends Enum> {

    private final T initialState;
    private final TransitionListener<T> transitionListener;
    private final StatemachineErrorHandler<T> errorHandler;
    private final Transitions<T> allowedTransitions;

    public Statemachine(T initialState,
                        TransitionListener<T> transitionListener,
                        StatemachineErrorHandler<T> exceptionHandler,
                        Transitions<T> allowedTransitions) {
        this.initialState = initialState;
        this.transitionListener = transitionListener;
        this.errorHandler = exceptionHandler;
        this.allowedTransitions = allowedTransitions;
    }

    public void updateState(StatefulEntity<T> entity, T newState) {
        T currentState = entity.getState();
        assertTransitionAllowed(currentState, newState);
        entity.updateState(newState);

        if (transitionListener!=null) {
            transitionListener.onTransition(entity, currentState, newState);
        }
    }

    public void init(StatefulEntity<T> entity) {
        entity.updateState(initialState);
        transitionListener.onInitialState(entity, initialState);
    }

    private void assertTransitionAllowed(T currentState, T newState) {
        boolean transitionAllowed = allowedTransitions.isTransitionAllowed(currentState, newState);
        if (!transitionAllowed) {
            errorHandler.onIllegalTransition(currentState, newState);
        }
    }

    public void fireEvent(StatefulEntity<T> entity, Event event) {
        T targetStateOnEvent = allowedTransitions.getTargetStateOnEvent(event, entity.getState());
        if (targetStateOnEvent == null) {
            errorHandler.onIllegalEvent(event, entity.getState());
        } else {
            entity.updateState(targetStateOnEvent);
        }
    }

    public static class Builder<T extends Enum> {

        private T initialState;
        private StatemachineErrorHandler<T> exceptionHandler = new DefaultStatemachineErrorHandler<>();
        private TransitionListener<T> transitionListener = new DefaultTransitionListener<>();
        private Transitions<T> transitions = new Transitions<>();

        public Builder<T> withInitialState(T state) {
            this.initialState = state;
            return this;
        }

        public Builder<T> withAllowedTransition(T from, T to) {
            transitions.add(new Transition<>(from, to));
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

        public Builder<T> withEvent(Event event, T source, T target) {
            this.transitions.add(new Transition<>(event, source, target));
            return this;
        }

        public Statemachine<T> build() {
            return new Statemachine<T>(initialState, transitionListener, exceptionHandler, transitions);
        }
    }
}
