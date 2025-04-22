package org.smoodi.core;

import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.annotation.StaticModule;
import org.smoodi.core.module.loader.ModuleLoaderComposite;
import org.smoodi.core.util.LazyInitUnmodifiableCollection;

import java.util.Set;

@Slf4j
@StaticModule
public final class SmoodiCoreProcessor implements Processor {

    private final LazyInitUnmodifiableCollection<Set<Processor>> processors = new LazyInitUnmodifiableCollection<>();

    @Override
    public void start() {
        loadModules();
        loadProcessors();

        processors.get().forEach(Processor::start);
    }

    @Override
    public void stop() {
        processors.get().forEach(Processor::stop);
    }

    private void loadModules() {
        new ModuleLoaderComposite().loadModules();
    }

    private void loadProcessors() {
        processors.initWith(Set.copyOf(SmoodiFramework.getInstance().getModuleContainer().getModulesByClass(
                Processor.class
        )));
    }
}
