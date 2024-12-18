package com.smoodi.module;

import com.smoodi.module.order.OrderedModuleDefine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smoodi.core.SmoodiFramework;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@Tag("fork")
public class OrderedModuleDefineTests {

    private static final Logger log = LoggerFactory.getLogger(OrderedModuleDefineTests.class);

    @Test
    public void 순서가_명시된_모듈들의_정렬_확인() {
        SmoodiFramework.startSmoodi(OrderedModuleDefine.class);

        var modules = SmoodiFramework.getInstance().getModuleContainer()
                .getModulesByClass(OrderedModuleDefine.Ordered.class);

        Assertions.assertEquals(modules.size(), 4);
        Assertions.assertIterableEquals(
                modules.stream().map(Object::getClass).toList(),
                List.of(
                        OrderedModuleDefine.Order1.class,
                        OrderedModuleDefine.Order2.class,
                        OrderedModuleDefine.Order3.class,
                        OrderedModuleDefine.Order4.class
                ));
    }
}
