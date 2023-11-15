package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.Event;

import java.time.ZonedDateTime;
import java.util.UUID;

public record EventOwnRes(
        UUID uuid,
        String title,
        Integer limitCount,
        String thumbnailImageUrl,
        ZonedDateTime endDateTime,
        ZonedDateTime startDateTime
) {
    public EventOwnRes(Event event) {
        this(event.getUuid(),
                event.getTitle(),
                event.getLimitCount(),
                event.getThumbnailImageUrl(),
                event.getEndDateTime(),
                event.getStartDateTime()
        );
    }
}
