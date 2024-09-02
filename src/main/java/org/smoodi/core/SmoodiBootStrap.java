package org.smoodi.core;

import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.init.LoggerInitializer;
import org.smoodi.core.module.loader.ModuleLoaderComposite;
import org.smoodi.core.module.loader.PackageBasedModuleLoader;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.smoodi.core.SmoodiFramework.finishBootStrap;
import static org.smoodi.core.SmoodiFramework.getStarter;

@Slf4j
public final class SmoodiBootStrap {

    private final PackageBasedModuleLoader moduleLoader =
            new ModuleLoaderComposite();

    public static void startSmoodi(Class<?> mainClass) {
        final LocalDateTime startedAt = LocalDateTime.now();
        LoggerInitializer.configureLogback();
        SmoodiFramework.initSmoodiFramework(mainClass);

        try {

            getStarter().moduleLoader.loadModules(mainClass.getPackageName());

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
}
