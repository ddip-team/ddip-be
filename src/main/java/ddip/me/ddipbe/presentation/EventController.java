package ddip.me.ddipbe.presentation;

import ddip.me.ddipbe.application.EventService;
import ddip.me.ddipbe.domain.Event;
import ddip.me.ddipbe.global.annotation.SessionMemberId;
import ddip.me.ddipbe.global.dto.ResponseEnvelope;
import ddip.me.ddipbe.presentation.dto.request.EventCreateReqDTO;
import ddip.me.ddipbe.presentation.dto.response.EventCommonResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("events")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEnvelope<?> createEvent(@RequestBody EventCreateReqDTO eventCreateReqDTO, @SessionMemberId Long memberId) {
        Event createEvent = eventService.createEvent(eventCreateReqDTO.getTitle(),
                eventCreateReqDTO.getPermitCount(),
                eventCreateReqDTO.getContent(),
                eventCreateReqDTO.getStart(),
                eventCreateReqDTO.getEnd(),
                memberId);
        return new ResponseEnvelope<>(null, new EventCommonResDTO(createEvent), null);
    }

    @GetMapping("/{uuid}")
    public ResponseEnvelope<?> findEventByUuid(@PathVariable UUID uuid) {
        Event findEvent = eventService.findEventByUuid(uuid);
        return new ResponseEnvelope<>(null, new EventCommonResDTO(findEvent), null);
    }

    @GetMapping("/me")
    public ResponseEnvelope<?> findOwnEvent(@SessionMemberId Long memberId) {
        List<Event> ownEvent = eventService.findOwnEvent(memberId);
        return new ResponseEnvelope<>(null, ownEvent, null);
    }
}
