package org.smoodi.core.loader;

import lombok.RequiredArgsConstructor;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.SmoodiProjects;

import java.util.Set;

@RequiredArgsConstructor
public class SmoodiProjectModuleLoader implements StaticModuleLoader {

    private final BasePackageModuleLoader packageLoader;

    @Override
    public void loadModules() {
        final Set<SmoodiProjects> projects = SmoodiFramework.getAddedSmoodiProjects();

        projects.forEach(it ->
                packageLoader.loadModules(it.basePackage)
        );
    }
}
