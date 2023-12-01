package ddip.me.ddipbe.application.dto;

import ddip.me.ddipbe.domain.Event;

import java.time.ZonedDateTime;
import java.util.UUID;

public record EventDto(
        UUID uuid,
        String title,
        Integer limitCount,
        String thumbnailImageUrl,
        ZonedDateTime startDateTime,
        ZonedDateTime endDateTime
) {
    public EventDto(Event event) {
        this(
                event.getUuid(),
                event.getTitle(),
                event.getApplicants().getLimitCount(),
                event.getThumbnailImageUrl(),
                event.getEventDuration().getStartDateTime(),
                event.getEventDuration().getEndDateTime()
        );
    }
}
