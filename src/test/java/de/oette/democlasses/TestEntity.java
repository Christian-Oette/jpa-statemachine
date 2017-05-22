package de.oette.democlasses;

import de.oette.StatefulEntity;

public class TestEntity implements StatefulEntity<MyState> {

    public MyState state;

    public MyState getState() {
        return state;
    }

    public void updateState(MyState state) {
        this.state = state;
    }
}
