package ddip.me.ddipbe.presentation.dto.response;


import ddip.me.ddipbe.application.dto.EventWithMemberDto;
import ddip.me.ddipbe.application.dto.MemberDto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record EventDetailRes(
        String title,
        UUID uuid,
        Integer limitCount,
        String thumbnailImageUrl,
        ZonedDateTime startDateTime,
        ZonedDateTime endDateTime,
        MemberDto member
) {
    public EventDetailRes(EventWithMemberDto eventWithMemberDto) {
        this(eventWithMemberDto.event().title(),
                eventWithMemberDto.event().uuid(),
                eventWithMemberDto.event().limitCount(),
                eventWithMemberDto.event().thumbnailImageUrl(),
                eventWithMemberDto.event().startDateTime(),
                eventWithMemberDto.event().endDateTime(),
                eventWithMemberDto.member()
        );
    }
}

