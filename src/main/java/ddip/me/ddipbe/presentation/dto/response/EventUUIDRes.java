package ddip.me.ddipbe.presentation.dto.response;

import lombok.Getter;

import java.util.UUID;

@Getter
public class EventUUIDRes {

    private UUID eventUuid;

    public EventUUIDRes(UUID eventUuid) {
        this.eventUuid = eventUuid;
    }
}
