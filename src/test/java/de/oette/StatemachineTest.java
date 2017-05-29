package de.oette;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StatemachineTest {

	private TestEntity testee = new TestEntity();

	@Test
	public void testInititalState() {
		//given
		Statemachine<TestState> stateStatemachine = new Statemachine.Builder<TestState>()
				.withInitialState(TestState.NEW)
				.build();

		//when
		stateStatemachine.init(testee);

		//then
		assertThat(testee.getState()).isEqualTo(TestState.NEW);
	}


	@Test
	public void testTransition() {
		//given
		Statemachine<TestState> stateStatemachine = new Statemachine.Builder<TestState>()
				.withInitialState(TestState.NEW)
				.withTransition(TestState.NEW, TestState.PROCESSING)
				.withTransition(TestState.PROCESSING, TestState.COMPLETED)
				.build();
		stateStatemachine.init(testee);

		//when
		stateStatemachine.updateState(testee, TestState.PROCESSING);
		stateStatemachine.updateState(testee, TestState.COMPLETED);

		//then
		assertThat(testee.getState()).isEqualTo(TestState.COMPLETED);
	}


	@Test
	public void testEvent() {
		//given
		Statemachine<TestState> stateStatemachine = new Statemachine.Builder<TestState>()
				.withInitialState(TestState.NEW)
				.withTransitionOnEvent(TestEvent.START, TestState.NEW, TestState.PROCESSING)
				.withTransitionOnEvent(TestEvent.STOP, TestState.NEW, TestState.NEW)
				.withTransitionOnEvent(TestEvent.STOP, TestState.PROCESSING, TestState.NEW)
				.withTransitionOnEvent(TestEvent.ERROR, TestState.NEW, TestState.FAILED)
				.withTransitionOnEvent(TestEvent.ERROR, TestState.PROCESSING, TestState.FAILED)
				.build();
		stateStatemachine.init(testee);

		//when / then
		stateStatemachine.fireEvent(testee, TestEvent.START);
		assertThat(testee.getState()).isEqualTo(TestState.PROCESSING);

		stateStatemachine.fireEvent(testee, TestEvent.STOP);
		assertThat(testee.getState()).isEqualTo(TestState.NEW);

		stateStatemachine.fireEvent(testee, TestEvent.START);
		assertThat(testee.getState()).isEqualTo(TestState.PROCESSING);

		stateStatemachine.fireEvent(testee, TestEvent.ERROR);
		assertThat(testee.getState()).isEqualTo(TestState.FAILED);
	}


	private class TestEntity implements StatefulEntity<TestState>{

		private TestState state;

		@Override
		public TestState getState() {
			return state;
		}

		@Override
		public void updateState(TestState state) {
			this.state = state;
		}
	}


	private enum  TestState {
		NEW,
		PROCESSING,
		COMPLETED,
		FAILED
	}

	private enum TestEvent implements Event {
		START,
		STOP,
		ERROR;


		@Override
		public String getEventName() {
			return this.name();
		}
	}
}