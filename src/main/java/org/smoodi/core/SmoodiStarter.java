package org.smoodi.core;

import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.init.LoggerInitializer;
import org.smoodi.core.loader.BasePackageModuleLoader;
import org.smoodi.core.loader.ModuleCreationError;
import org.smoodi.core.loader.ModuleDeclareError;
import org.smoodi.core.loader.ModuleLoader;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
public final class SmoodiStarter {

    public final ModuleLoader moduleLoader = new BasePackageModuleLoader();

    public static Class<?> mainClass = null;

    public static void startSmoodi(Class<?> mainClass) {
        final LocalDateTime startedAt = LocalDateTime.now();
        SmoodiStarter.mainClass = mainClass;
        LoggerInitializer.configureLogback();

        try {

            SmoodiFramework.getStarter().moduleLoader.loadModules(mainClass.getPackage().getName());

        } catch (ModuleCreationError | ModuleDeclareError error) {
            log.error(error.getMessage(), error);
            return;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        final LocalDateTime finishedAt = LocalDateTime.now();
        log.info(
                "Smoodi started on {} seconds. Started at : {}, Initialize finished at : {}",
                (double) (Duration.between(startedAt, finishedAt).getNano() / 1_000_000_000),
                startedAt, finishedAt
        );
    }
}
