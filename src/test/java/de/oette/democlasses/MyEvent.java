package de.oette.democlasses;

import de.oette.Event;

public enum MyEvent implements Event {
    BAD_ERROR;


    public String getEventName() {
        return this.name();
    }
}
