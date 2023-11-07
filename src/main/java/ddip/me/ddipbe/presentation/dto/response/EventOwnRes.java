package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.Event;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Getter
public class EventOwnRes {
    private final String title;
    private final UUID uuid;
    private final Integer permitCount;
    private final String contents;
    private final LocalDateTime end;
    private final LocalDateTime start;
    private final boolean isOpen;

    public EventOwnRes(Event event) {
        this.title = event.getTitle();
        this.uuid = event.getUuid();
        this.permitCount = event.getPermitCount();
        this.contents = event.getContent();
        this.start = event.getStart();
        this.end = event.getEnd();
        this.isOpen = event.getStart().isBefore(now()) && event.getEnd().isAfter(now());
    }
}
