package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.Event;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

import static java.time.ZonedDateTime.now;

@Getter
public class EventOwnRes {

    private final UUID uuid;
    private final String title;
    private final Integer limitCount;
    private final String successContent;
    private final ZonedDateTime endDateTime;
    private final ZonedDateTime startDateTime;
    private final boolean isOpen;

    public EventOwnRes(Event event) {
        this.title = event.getTitle();
        this.uuid = event.getUuid();
        this.limitCount = event.getLimitCount();
        this.successContent = event.getSuccessContent();
        this.startDateTime = event.getStartDateTime();
        this.endDateTime = event.getEndDateTime();
        this.isOpen = event.getStartDateTime().isBefore(now()) && event.getEndDateTime().isAfter(now());
    }
}
