package ddip.me.ddipbe.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class EventUUIDRes {

    private UUID eventUuid;
}
