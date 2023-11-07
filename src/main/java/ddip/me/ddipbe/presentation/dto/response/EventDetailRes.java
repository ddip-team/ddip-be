package ddip.me.ddipbe.presentation.dto.response;


import ddip.me.ddipbe.domain.Event;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class EventDetailRes {
    private final String title;
    private final UUID uuid;
    private final Integer permitCount;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Long memberId;

    public EventDetailRes(Event event) {
        this.title = event.getTitle();
        this.uuid = event.getUuid();
        this.permitCount = event.getPermitCount();
        this.start = event.getStart();
        this.end = event.getEnd();
        this.memberId = event.getMember().getId();
    }
}

