package ddip.me.ddipbe.domain;

import ddip.me.ddipbe.domain.exception.EventDateInvalidException;
import ddip.me.ddipbe.global.util.CustomClock;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventDuration {

    private ZonedDateTime startDateTime;

    private ZonedDateTime endDateTime;

    public EventDuration(ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        if (!EventDuration.isValid(startDateTime, endDateTime)) {
            throw new EventDateInvalidException();
        }
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public boolean isOpen(ZonedDateTime now) {
        return startDateTime.isBefore(now) && endDateTime.isAfter(now);
    }

    public boolean started() {
        return startDateTime.isBefore(CustomClock.now());
    }

    private static boolean isValid(ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        return endDateTime.isAfter(startDateTime) && endDateTime.isAfter(CustomClock.now());
    }
}
