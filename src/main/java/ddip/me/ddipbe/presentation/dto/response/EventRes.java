package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.Event;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class EventRes {

    private final UUID uuid;
    private final String title;
    private final Integer permitCount;
    private final String content;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Long memberId;

    public EventRes(Event event) {
        this.uuid = event.getUuid();
        this.title = event.getTitle();
        this.permitCount = event.getPermitCount();
        this.content = event.getContent();
        this.start = event.getStart();
        this.end = event.getEnd();
        this.memberId = event.getMember().getId();
    }
}
