    package org.smoodi.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.smoodi.core.context.DefaultModuleContainer;
import org.smoodi.core.context.ModuleContainer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SmoodiFramework {

    @Getter
    private static final ModuleContainer moduleContainer = new DefaultModuleContainer();

    @Getter
    private static final SmoodiFramework instance = new SmoodiFramework();
}
