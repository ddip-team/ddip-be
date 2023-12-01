package ddip.me.ddipbe.global.util;

import java.time.ZonedDateTime;

public class CustomClock {

    private static final CustomClock DEFAULT = new CustomClock();
    private static CustomClock instance = DEFAULT;

    public static void reset() {
        instance = DEFAULT;
    }

    public static ZonedDateTime now() {
        return instance.timeNow();
    }

    protected void setInstance(CustomClock customClock) {
        CustomClock.instance = customClock;
    }

    protected ZonedDateTime timeNow() {
        return ZonedDateTime.now();
    }
}
