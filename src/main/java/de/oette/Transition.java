package de.oette;

public class Transition<T extends Enum> {

    private final String eventName;
    private final T sourceState;
    private final T targetState;

    public Transition(Event event, T sourceState, T targetState) {
        this.eventName = event != null ? event.getEventName() : null;
        this.sourceState = sourceState;
        this.targetState = targetState;
    }

    public Transition(T sourceState, T targetState) {
        this(null, sourceState, targetState);
    }

    public boolean hasEvent(Event event) {
        return this.eventName != null && this.eventName.equals(event.getEventName());
    }

    public boolean hasSource(Enum sourceState) {
        return this.sourceState.equals(sourceState);
    }

    public boolean hasTarget(Enum targetState) {
        return this.targetState.equals(targetState);
    }

    public T getTargetState() {
        return targetState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transition<?> that = (Transition<?>) o;

        if (eventName != null ? !eventName.equals(that.eventName) : that.eventName != null) return false;
        if (sourceState != null ? !sourceState.equals(that.sourceState) : that.sourceState != null) return false;
        return targetState != null ? targetState.equals(that.targetState) : that.targetState == null;

    }

    @Override
    public int hashCode() {
        int result = eventName != null ? eventName.hashCode() : 0;
        result = 31 * result + (sourceState != null ? sourceState.hashCode() : 0);
        result = 31 * result + (targetState != null ? targetState.hashCode() : 0);
        return result;
    }
}
