package ddip.me.ddipbe.presentation.dto.response;


import ddip.me.ddipbe.domain.Event;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class EventDetailRes {
    private final String title;
    private final UUID uuid;
    private final Integer limitCount;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final Long memberId;

    public EventDetailRes(Event event) {
        this.title = event.getTitle();
        this.uuid = event.getUuid();
        this.limitCount = event.getLimitCount();
        this.startDateTime = event.getStartDateTime();
        this.endDateTime = event.getEndDateTime();
        this.memberId = event.getMember().getId();
    }
}

