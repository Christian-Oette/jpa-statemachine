package de.oette;

import de.oette.democlasses.MyEvent;
import de.oette.democlasses.MyState;
import de.oette.democlasses.TestEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class StatemachineTest implements StatemachineErrorHandler<MyState>, TransitionListener<MyState> {

    private static final Logger LOGGER = LogManager.getLogger(StatemachineTest.class);

    @Test
    public void test() {
        Statemachine<MyState> stateStatemachine = new Statemachine.Builder<MyState>()
                .withInitialState(MyState.NEW)
                .withAllowedTransition(MyState.NEW, MyState.PROCESSING)
                .withAllowedTransition(MyState.PROCESSING, MyState.COMPLETED)
                .withTransitionListener(this)
                .withEvent(MyEvent.BAD_ERROR, MyState.FAILED)
                .withExceptionHandler(this)
                .build();

        TestEntity testEntity = new TestEntity();
        stateStatemachine.init(testEntity);
        stateStatemachine.updateState(testEntity, MyState.PROCESSING);
        stateStatemachine.updateState(testEntity, MyState.COMPLETED);
        stateStatemachine.fireEvent(testEntity, MyEvent.BAD_ERROR);
    }

    public void onIllegalTransition(MyState from, MyState to) {
        LOGGER.error("Illegal transition from {} to {}", from, to);
    }


    public void onTransition(StatefulEntity<MyState> entity, MyState from, MyState to) {
        LOGGER.info("Transition from {} to {}", from, to);
    }

    public void onInitialState(StatefulEntity<MyState> entity, MyState initialState) {
        LOGGER.info("Initial state set to {}", initialState);
    }
}
