package de.oette.democlasses;

import de.oette.State;

public enum MyState implements State {

    NEW,
    PROCESSING,
    COMPLETED,
    FAILED
}
