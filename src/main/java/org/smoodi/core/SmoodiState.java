package org.smoodi.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SmoodiState {

    public static final int SLEEPING = 0;
    public static final int INITIALIZING = 1;
    public static final int RUNNING = 2;
    public static final int STOPPING = 3;
    public static final int STOPPED = 4;
    public static final int ERRORED = 5;

    @Getter
    @Setter
    private static int state = SLEEPING;
}
