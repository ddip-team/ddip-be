package ddip.me.ddipbe.domain;

import ddip.me.ddipbe.domain.exception.EventDateInvalidException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventDuration {

    @Column(nullable = false, name = "start_date_time")
    private ZonedDateTime startDateTime;

    @Column(nullable = false, name = "end_date_time")
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
        return startDateTime.isBefore(ZonedDateTime.now());
    }

    private static boolean isValid(ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        return endDateTime.isAfter(startDateTime) && endDateTime.isAfter(ZonedDateTime.now());
    }
}
