package ddip.me.ddipbe.application.dto;

import ddip.me.ddipbe.domain.Event;

public record EventWithMemberDto(
        EventDto event,
        MemberDto member
) {
    public EventWithMemberDto(Event event) {
        this(
                new EventDto(event),
                new MemberDto(event.getMember())
        );
    }
}
