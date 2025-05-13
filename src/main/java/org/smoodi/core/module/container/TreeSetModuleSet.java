package org.smoodi.core.module.container;

import org.smoodi.core.module.ModuleType;
import org.smoodi.core.util.ModuleUtils;

import java.util.*;

public class TreeSetModuleSet implements ModuleSet {

    private final Map<ModuleType<?>, SequencedSet<Object>> map = new HashMap<>();

    @Override
    public <T> SequencedSet<T> get(ModuleType<T> moduleType) {
        putIfAbsent(moduleType);

        //noinspection unchecked
        return (SequencedSet<T>) Collections.unmodifiableSequencedSet(map.get(moduleType));
    }

    @Override
    public <T> void add(ModuleType<T> moduleType, T value) {
        putIfAbsent(moduleType);

        map.get(moduleType).add(value);
    }

    private void putIfAbsent(ModuleType<?> moduleType) {
        if (!map.containsKey(moduleType)) {
            map.put(moduleType, new TreeSet<>(ModuleUtils.comparator()));
        }
    }

    @Override
    public int size() {
        return this.map.size();
    }
}
