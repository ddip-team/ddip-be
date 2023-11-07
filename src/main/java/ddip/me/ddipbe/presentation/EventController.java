package ddip.me.ddipbe.presentation;

import ddip.me.ddipbe.application.EventService;
import ddip.me.ddipbe.domain.Event;
import ddip.me.ddipbe.global.annotation.SessionMemberId;
import ddip.me.ddipbe.global.dto.ResponseEnvelope;
import ddip.me.ddipbe.presentation.dto.request.CreateEventReq;
import ddip.me.ddipbe.presentation.dto.response.EventDetailRes;
import ddip.me.ddipbe.presentation.dto.response.EventOwnRes;
import ddip.me.ddipbe.presentation.dto.response.EventRes;
import ddip.me.ddipbe.presentation.dto.response.EventUUIDRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("events")
@Log4j2
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEnvelope<EventUUIDRes> createEvent(@RequestBody CreateEventReq createEventReq, @SessionMemberId Long memberId) {
        Event event = eventService.createEvent(
                createEventReq.getTitle(),
                createEventReq.getPermitCount(),
                createEventReq.getContent(),
                createEventReq.getStart(),
                createEventReq.getEnd(),
                memberId);
        return new ResponseEnvelope<>(new EventUUIDRes(event.getUuid()));
    }

    @GetMapping("/{uuid}")
    public ResponseEnvelope<?> findEventByUuid(@PathVariable UUID uuid) {
        Event foundEvent = eventService.findEventByUuid(uuid);
        return new ResponseEnvelope<>(new EventDetailRes(foundEvent));
    }

    @GetMapping("/me")
    public ResponseEnvelope<?> findOwnEvent(@SessionMemberId Long memberId, @RequestParam(required = false) String open) {
        boolean checkOpen = false;
        if (open != null) {
            checkOpen = true;
        }
        List<Event> ownEvents = eventService.findOwnEvent(memberId, checkOpen);
        List<EventOwnRes> ownEvnetList = ownEvents.stream().map(EventOwnRes::new).toList();
        return new ResponseEnvelope<>(ownEvnetList);
    }

    @PostMapping("/{uuid}/apply")
    public ResponseEnvelope<?> applyEvent(@PathVariable UUID uuid, @RequestParam String token) {
        eventService.applyEvent(uuid, token);
        return new ResponseEnvelope<>(null);
    }

    @GetMapping("/{uuid}/success")
    public ResponseEnvelope<EventRes> findSuccessEvent(@PathVariable UUID uuid, @RequestParam String token) {
        Event event = eventService.findSuccessEvent(uuid, token);
        return new ResponseEnvelope<>(new EventRes(event));
    }
}
