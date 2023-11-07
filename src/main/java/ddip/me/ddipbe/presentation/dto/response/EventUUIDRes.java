package ddip.me.ddipbe.presentation.dto.response;

import lombok.Getter;

import java.util.UUID;

@Getter
public class EventUUIDRes {

    private final UUID eventUuid;

    public EventUUIDRes(UUID uuid) {
        this.eventUuid = uuid;
    }
}
