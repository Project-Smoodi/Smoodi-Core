package org.smoodi.core.module.container;

import org.smoodi.core.module.ModuleType;
import org.smoodi.core.util.UtilCollection.SortedList;
import org.smoodi.core.util.ModuleUtils;

import java.util.*;

public class ModulesImpl implements Modules {

    private final Map<ModuleType<?>, List<Object>> map = new HashMap<>();

    @Override
    public <T> List<T> get(ModuleType<T> moduleType) {
        putIfAbsent(moduleType);

        //noinspection unchecked
        return (List<T>) Collections.unmodifiableList(map.get(moduleType));
    }

    @Override
    public List<Object> getAll() {
        return map.values()
                .stream()
                .flatMap(Collection::stream)
                .toList();
    }

    @Override
    public <T> void add(ModuleType<T> moduleType, T value) {
        putIfAbsent(moduleType);

        map.get(moduleType).add(value);
    }

    private void putIfAbsent(ModuleType<?> moduleType) {
        if (!map.containsKey(moduleType)) {
            map.put(moduleType, new SortedList<>(ModuleUtils.comparator()));
        }
    }

    @Override
    public int size() {
        return this.map.size();
    }
}
