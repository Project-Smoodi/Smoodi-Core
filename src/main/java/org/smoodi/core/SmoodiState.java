package org.smoodi.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SmoodiState {

    public static final String SLEEPING = "Sleeping";
    public static final String INITIALIZING = "Initializing";
    public static final String RUNNING = "Running";
    public static final String STOPPING = "Stopping";
    public static final String STOPPED = "Stopped";
    public static final String ERRORED = "Errored";

    @Getter
    @Setter
    private static String state = SLEEPING;
}
