package org.smoodi.core.lifecycle;

public interface Lifecycle {

    State getState();

    enum State {
        SLEEPING,

        INITIALIZING,
        INITIALIZED,

        STARTING,
        RUNNING,

        STOPPING,
        STOPPED,

        DESTROYING,
        DESTROYED,

        ERROR
    }
}
