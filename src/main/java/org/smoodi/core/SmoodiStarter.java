package org.smoodi.core;

import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.loader.BasePackageModuleLoader;
import org.smoodi.core.loader.ModuleLoader;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
public final class SmoodiStarter {

    public final ModuleLoader moduleLoader = new BasePackageModuleLoader();

    public static Class<?> mainClass = null;

    public static void startSmoodi(Class<?> mainClass) {
        final LocalDateTime started = LocalDateTime.now();

        try {

            SmoodiStarter.mainClass = mainClass;

            SmoodiFramework.getStarter().moduleLoader.loadModules(mainClass.getPackage().getName());

        } catch (Exception e) {
            SmoodiStarter.log.error(e.getMessage(), e);
        }

        final LocalDateTime now = LocalDateTime.now();
        SmoodiStarter.log.info(
                "Smoodi started on {} seconds. Started at : {}, Initialize finished at : {}",
                (double) (Duration.between(now, started).getNano() / 1_000_000_000),
                started, now
        );
    }
}
