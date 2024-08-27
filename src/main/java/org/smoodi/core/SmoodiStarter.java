package org.smoodi.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smoodi.core.init.loader.ModuleLoader;

import java.time.Duration;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SmoodiStarter {

    public final ModuleLoader moduleLoader = ModuleLoader.getInstance();

    public final Logger logger = LoggerFactory.getLogger(SmoodiStarter.class);

    public static void startSmoodi(Class<?> mainClass) {
        final LocalDateTime started = LocalDateTime.now();

        instance.moduleLoader.loadModules(mainClass.getPackage().getName());

        final LocalDateTime now = LocalDateTime.now();
        instance.logger.info("Smoodi started on {} seconds. Started at : {}, Initialize finished at : {}",
                (double) (Duration.between(now, started).getNano() / 1_000_000_000),
                started, now);
    }

    // Singleton
    private static final SmoodiStarter instance = new SmoodiStarter();
}
