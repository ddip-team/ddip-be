package ddip.me.ddipbe.presentation;

import ddip.me.ddipbe.application.EventService;
import ddip.me.ddipbe.domain.Event;
import ddip.me.ddipbe.global.annotation.SessionMemberId;
import ddip.me.ddipbe.global.dto.ResponseEnvelope;
import ddip.me.ddipbe.presentation.dto.request.CreateEventReq;
import ddip.me.ddipbe.presentation.dto.response.EventRes;
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
    public ResponseEnvelope<?> createEvent(@RequestBody CreateEventReq createEventReq, @SessionMemberId Long memberId) {
        Event createEvent = eventService.createEvent(
                createEventReq.getTitle(),
                createEventReq.getPermitCount(),
                createEventReq.getContent(),
                createEventReq.getStart(),
                createEventReq.getEnd(),
                memberId);
        return new ResponseEnvelope<>(null, new EventRes(createEvent), null);
    }

    @GetMapping("/{uuid}")
    public ResponseEnvelope<?> findEventByUuid(@PathVariable UUID uuid) {
        Event findEvent = eventService.findEventByUuid(uuid);
        return new ResponseEnvelope<>(null, new EventRes(findEvent), null);
    }

    @GetMapping("/me")
    public ResponseEnvelope<?> findOwnEvent(@SessionMemberId Long memberId) {
        List<Event> ownEvent = eventService.findOwnEvent(memberId);
        return new ResponseEnvelope<>(null, ownEvent, null);
    }
}
