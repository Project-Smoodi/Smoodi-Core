package org.smoodi.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StartSmoodiTest {

    @Test
    public void test() {
        SmoodiFramework.startSmoodi(StartSmoodiTest.class);

        //noinspection StatementWithEmptyBody
        while (SmoodiState.getState().equals(SmoodiState.SLEEPING) || SmoodiState.getState().equals(SmoodiState.INITIALIZING)) {
        }

        Assertions.assertEquals(
                SmoodiState.RUNNING,
                SmoodiState.getState()
        );
    }
}
