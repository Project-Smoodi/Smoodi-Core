package org.smoodi.core;

import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.util.LazyInitUnmodifiableCollection;

import java.util.Set;

@Slf4j
class SmoodiInterrupter {

    private static final LazyInitUnmodifiableCollection<Set<SubprojectInterrupt>> subprojects =
            new LazyInitUnmodifiableCollection<>();

    synchronized static void interrupt() {
        SmoodiState.setState(SmoodiState.STOPPING);
        log.info("Stopping smoodi framework...");

        subprojects.initWith(
                SmoodiFramework.getInstance().getModuleContainer()
                        .getModulesByClass(SubprojectInterrupt.class)
        ).get().forEach(SubprojectInterrupt::interrupt);
    }
}
