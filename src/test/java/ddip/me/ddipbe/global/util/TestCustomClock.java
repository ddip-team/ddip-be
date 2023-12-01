package ddip.me.ddipbe.global.util;

import java.time.ZonedDateTime;

public class TestCustomClock extends CustomClock {
    private ZonedDateTime now;

    public TestCustomClock() {
        setInstance(this);
    }

    public void setNow(ZonedDateTime now) {
        this.now = now;
    }

    @Override
    protected ZonedDateTime timeNow() {
        return now != null ? now : super.timeNow();
    }
}
