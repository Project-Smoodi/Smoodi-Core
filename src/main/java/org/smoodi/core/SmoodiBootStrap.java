package org.smoodi.core;

import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.init.LoggerInitializer;
import org.smoodi.core.loader.BasePackageModuleLoader;
import org.smoodi.core.loader.DefaultStaticModuleLoader;
import org.smoodi.core.loader.PackageBasedModuleLoader;
import org.smoodi.core.loader.StaticModuleLoader;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.smoodi.core.SmoodiFramework.*;

@Slf4j
public final class SmoodiBootStrap {

    private final PackageBasedModuleLoader moduleLoader = new BasePackageModuleLoader();

    private final StaticModuleLoader staticModuleLoader = new DefaultStaticModuleLoader();

    public static void startSmoodi(Class<?> mainClass) {
        final LocalDateTime startedAt = LocalDateTime.now();
        LoggerInitializer.configureLogback();
        startBootStrap(mainClass);

        try {

            loadModules();

        } catch (Throwable error) {
            log.error(error.getMessage(), error);
            return;
        }

        final LocalDateTime finishedAt = LocalDateTime.now();
        log.info(
                "Smoodi started on {} seconds. Started at : {}, Initialize finished at : {}",
                (double) (Duration.between(startedAt, finishedAt).getNano() / 1_000_000_000),
                startedAt, finishedAt
        );

        finishBootStrap();
    }

    private static void loadModules() {
        getStarter().staticModuleLoader.loadModules();
        getStarter().moduleLoader.loadModules(
                SmoodiFramework.class.getPackage().getName());
        getStarter().moduleLoader.loadModules(getMainClass().getPackage().getName());
    }
}
