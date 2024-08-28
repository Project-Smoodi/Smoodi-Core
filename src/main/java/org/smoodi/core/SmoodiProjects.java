package org.smoodi.core;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SmoodiProjects {

    SMOODI_CORE("org.smoodi.core"),
    SMOODI_SECURITY("org.smoodi.security"),
    SMOODI_SERVER("org.smoodi.server"),
    SMOODI_CLIENT("org.smoodi.client"),
    SMOODI_DATA("org.smoodi.data");

    public final String basePackage;
}
