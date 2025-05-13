package org.smoodi.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smoodi.core.lifecycle.Lifecycle;

public class StartSmoodiTest {

    @Test
    public void test() {
        TestBase.initWith(StartSmoodiTest.class);

        Assertions.assertDoesNotThrow(() -> SmoodiFramework.startSmoodi(StartSmoodiTest.class));

        Assertions.assertEquals(
                Lifecycle.State.RUNNING,
                SmoodiFramework.getInstance().getState()
        );

        Assertions.assertDoesNotThrow(SmoodiFramework::kill);

        Assertions.assertEquals(
                Lifecycle.State.STOPPED,
                SmoodiFramework.getInstance().getState()
        );
    }
}
