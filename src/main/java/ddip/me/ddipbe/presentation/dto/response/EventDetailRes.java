package ddip.me.ddipbe.presentation.dto.response;


import ddip.me.ddipbe.domain.Event;

import java.time.ZonedDateTime;
import java.util.UUID;

public record EventDetailRes(
        String title,
        UUID uuid,
        Integer limitCount,
        String thumbnailImageUrl,
        ZonedDateTime startDateTime,
        ZonedDateTime endDateTime,
        MemberRes member
) {
    public EventDetailRes(Event event) {
        this(event.getTitle(),
                event.getUuid(),
                event.getApplicants().getLimitCount(),
                event.getThumbnailImageUrl(),
                event.getEventDuration().getStartDateTime(),
                event.getEventDuration().getEndDateTime(),
                new MemberRes(event.getMember())
        );
    }
}

