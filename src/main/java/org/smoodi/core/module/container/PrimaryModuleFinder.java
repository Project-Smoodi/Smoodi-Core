package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.core.module.ModuleType;
import org.smoodi.core.util.ModuleUtils;

import java.util.Collections;
import java.util.List;

class PrimaryModuleFinder implements ModuleFinder {

    @EmptyableArray
    @NotNull
    @Override
    public <T> List<T> find(@NotNull Modules modules, @NotNull ModuleType<T> moduleType) {
        List<T> list = modules.get(
                ModuleUtils.findPrimaryModuleType(moduleType)
        );

        if (list.isEmpty()) {
            return Collections.emptyList();
        } else if (list.size() > 1) {
            throw new IllegalStateException("More than one primary module found.");
        }

        return Collections.unmodifiableList(list);
    }
}
