package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.Event;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public class EventOwnRes {

    private final UUID uuid;
    private final String title;
    private final Integer limitCount;
    private final String thumbnailImageUrl;
    private final ZonedDateTime endDateTime;
    private final ZonedDateTime startDateTime;

    public EventOwnRes(Event event) {
        this.title = event.getTitle();
        this.uuid = event.getUuid();
        this.limitCount = event.getLimitCount();
        this.thumbnailImageUrl = event.getThumbnailImageUrl();
        this.startDateTime = event.getStartDateTime();
        this.endDateTime = event.getEndDateTime();
    }
}
