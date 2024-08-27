package org.smoodi.core;

import org.smoodi.core.loader.ModuleConstructorTarget;

@ModuleConstructorTarget
public class StartTest {

    public static void main(String[] args) {
        SmoodiStarter.startSmoodi(StartTest.class);
    }
}
