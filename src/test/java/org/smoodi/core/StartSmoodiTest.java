package org.smoodi.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smoodi.core.lifecycle.Lifecycle;

public class StartSmoodiTest {

    @Test
    public void test() {
        Assertions.assertDoesNotThrow(() -> SmoodiFramework.startSmoodi(StartSmoodiTest.class));

        Assertions.assertEquals(
                Lifecycle.State.RUNNING,
                SmoodiFramework.getInstance().getState()
        );
    }
}
