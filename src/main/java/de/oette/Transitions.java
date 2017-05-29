package de.oette;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Transitions<T extends Enum> {

    private Set<Transition<T>> transitions = new HashSet<>();

    public void add(Transition<T> transition) {
        transitions.add(transition);
    }

    public boolean isTransitionAllowed(final T sourceState, T targetState) {
        Optional<Transition<T>> transitionOptional = transitions.stream()
                .filter(t -> t.hasSource(sourceState))
                .filter(t -> t.hasTarget(targetState))
                .findFirst();
        return transitionOptional.isPresent();
    }

    public T getTargetStateOnEvent(Event event, final T sourceState) {
        Optional<Transition<T>> transitionOptional = transitions.stream()
                .filter(t -> t.hasSource(sourceState))
                .filter(t -> t.hasEvent(event))
                .findFirst();
        if( transitionOptional.isPresent()) {
            return transitionOptional.get().getTargetState();
        } else {
            return null;
        }
    }


}
