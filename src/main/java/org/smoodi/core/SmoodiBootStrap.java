package org.smoodi.core;

import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.init.LoggerInitializer;
import org.smoodi.core.module.loader.ModuleLoader;
import org.smoodi.core.module.loader.ModuleLoaderComposite;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.smoodi.core.SmoodiFramework.finishBootStrap;
import static org.smoodi.core.SmoodiFramework.getInstance;

@Slf4j
public final class SmoodiBootStrap {

    private ModuleLoader moduleLoader;

    private boolean initialized = false;

    private void init() {
        if (initialized) {
            return;
        }

        moduleLoader = new ModuleLoaderComposite();

        initialized = true;
    }

    public static void startSmoodi(Class<?> mainClass) {
        final LocalDateTime startedAt = LocalDateTime.now();
        LoggerInitializer.configureLogback();
        SmoodiFramework.initSmoodiFramework(mainClass);
        SmoodiFramework.getInstance().getStarter().init();

        try {

            getInstance().getStarter().moduleLoader.loadModules();

            new SubprojectBootStrapRunner().run();

        } catch (Throwable error) {
            log.error(error.getMessage(), error);
            return;
        }

        final LocalDateTime finishedAt = LocalDateTime.now();
        log.info(
                "Smoodi started on {} seconds. Started at : {}, Initialize finished at : {}",
                (Duration.between(startedAt, finishedAt).getNano() / 1_000_000_000.0),
                startedAt, finishedAt
        );

        finishBootStrap();
    }

    private static final class SubprojectBootStrapRunner {

        private final List<SubprojectBootStrap> bootStraps =
                SmoodiFramework.getInstance().getModuleContainer().getModulesByClass(
                        SubprojectBootStrap.class
                );

        public void run() {
            log.info("Smoodi subprojects bootstrap started.");
            bootStraps.forEach(SubprojectBootStrap::start);
        }
    }
}
