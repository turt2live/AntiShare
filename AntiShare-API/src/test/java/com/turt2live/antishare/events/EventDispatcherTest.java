package com.turt2live.antishare.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class EventDispatcherTest {

    public class TestEvent1 implements Event {
    }

    public class TestEvent2 implements Event {
    }

    public class InheritedEvent extends TestEvent1 {
    }

    public class Listener {
        public int GENERAL_FIRES = 0;
        public int TEST1_FIRES = 0;
        public int TEST2_FIRES = 0;
        public int INHERITED_FIRES = 0;

        void reset() {
            GENERAL_FIRES = 0;
            TEST1_FIRES = 0;
            TEST2_FIRES = 0;
            INHERITED_FIRES = 0;
        }

        @EventListener
        public void onEvent(Event event) {
            GENERAL_FIRES++;
        }

        @EventListener
        public void onEvent(TestEvent1 event) {
            TEST1_FIRES++;
        }

        @EventListener
        public void onEvent(TestEvent2 event) {
            TEST2_FIRES++;
        }

        @EventListener
        public void onEvent(InheritedEvent event) {
            INHERITED_FIRES++;
        }
    }

    @Test
    public void testRegisterNull() {
        // Should do nothing, just return
        EventDispatcher.register(null);
    }

    @Test
    public void testDeregisterNull() {
        // Should do nothing, just return
        EventDispatcher.deregister(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullEvent() {
        EventDispatcher.dispatch(null);
    }

    @Test
    public void testDispatch() throws Exception {
        Listener listener = new Listener();
        EventDispatcher.register(listener);

        Event testEvent = new TestEvent1();
        EventDispatcher.dispatch(testEvent);

        assertEquals(1, listener.GENERAL_FIRES);
        assertEquals(1, listener.TEST1_FIRES);
        assertEquals(0, listener.TEST2_FIRES);
        assertEquals(0, listener.INHERITED_FIRES);
        listener.reset();

        testEvent = new TestEvent2();
        EventDispatcher.dispatch(testEvent);

        assertEquals(1, listener.GENERAL_FIRES);
        assertEquals(0, listener.TEST1_FIRES);
        assertEquals(1, listener.TEST2_FIRES);
        assertEquals(0, listener.INHERITED_FIRES);
        listener.reset();

        testEvent = new InheritedEvent();
        EventDispatcher.dispatch(testEvent);

        assertEquals(1, listener.GENERAL_FIRES);
        assertEquals(1, listener.TEST1_FIRES);
        assertEquals(0, listener.TEST2_FIRES);
        assertEquals(1, listener.INHERITED_FIRES);
        listener.reset();

        EventDispatcher.deregister(listener);
        EventDispatcher.dispatch(testEvent);

        assertEquals(0, listener.GENERAL_FIRES);
        assertEquals(0, listener.TEST1_FIRES);
        assertEquals(0, listener.TEST2_FIRES);
        assertEquals(0, listener.INHERITED_FIRES);
    }
}
