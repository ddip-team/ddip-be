package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.Event;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class EventRes {

    private final UUID uuid;
    private final String title;
    private final Integer limitCount;
    private final String successContent;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final Long memberId;

    public EventRes(Event event) {
        this.uuid = event.getUuid();
        this.title = event.getTitle();
        this.limitCount = event.getLimitCount();
        this.successContent = event.getSuccessContent();
        this.startDateTime = event.getStartDateTime();
        this.endDateTime = event.getEndDateTime();
        this.memberId = event.getMember().getId();
    }
}
