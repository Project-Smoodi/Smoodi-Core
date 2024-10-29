package com.smoodi.module.order;

import org.smoodi.core.annotation.Module;

public class OrderedModuleDefine {

    public interface Ordered {
    }

    @Module(order = 0)
    public static class Order1 implements Ordered {
    }

    @Module(order = 1)
    public static class Order2 implements Ordered {
    }

    @Module(order = 2)
    public static class Order3 implements Ordered {
    }

    @Module(order = 3)
    public static class Order4 implements Ordered {
    }
}
