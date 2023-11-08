package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.Event;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Getter
public class EventOwnRes {

    private final UUID uuid;
    private final String title;
    private final Integer limitCount;
    private final String successContent;
    private final LocalDateTime endDateTime;
    private final LocalDateTime startDateTime;
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
