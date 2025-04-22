package org.smoodi.core.lifecycle;

public interface Lifecycle {

    State getState();

    enum State {
        NONE,
        SLEEPING,

        INITIALIZING,
        INITIALIZED,

        STARTING,
        STARTED,

        RUNNING,

        STOPPING,
        STOPPED,

        ERROR
    }
}
