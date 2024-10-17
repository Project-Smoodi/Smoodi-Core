package com.smoodi.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smoodi.core.SmoodiBootStrap;
import org.smoodi.core.SmoodiState;

public class StartSmoodiTest {

    @Test
    public void test() {
        SmoodiBootStrap.startSmoodi(StartSmoodiTest.class);

        while (SmoodiState.getState().equals(SmoodiState.SLEEPING) || SmoodiState.getState().equals(SmoodiState.INITIALIZING)) {
        }

        Assertions.assertEquals(
                SmoodiState.RUNNING,
                SmoodiState.getState()
        );
    }
}
