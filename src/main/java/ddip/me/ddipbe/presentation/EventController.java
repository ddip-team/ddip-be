package ddip.me.ddipbe.presentation;

import ddip.me.ddipbe.application.EventService;
import ddip.me.ddipbe.domain.Event;
import ddip.me.ddipbe.domain.SuccessRecord;
import ddip.me.ddipbe.global.annotation.SessionMemberId;
import ddip.me.ddipbe.global.dto.ResponseEnvelope;
import ddip.me.ddipbe.presentation.dto.request.CreateEventReq;
import ddip.me.ddipbe.presentation.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
                createEventReq.getLimitCount(),
                createEventReq.getSuccessContent(),
                createEventReq.getStartDateTime(),
                createEventReq.getEndDateTime(),
                memberId,
                createEventReq.getJsonString());
        return new ResponseEnvelope<>(new EventUUIDRes(event.getUuid()));
    }

    @GetMapping("/{uuid}")
    public ResponseEnvelope<?> findEventByUuid(@PathVariable UUID uuid) {
        Event foundEvent = eventService.findEventByUuid(uuid);
        return new ResponseEnvelope<>(new EventDetailRes(foundEvent));
    }

    @GetMapping("/me")
    public ResponseEnvelope<?> findOwnEvent(@SessionMemberId Long memberId, @RequestParam(required = false) String open) {
        boolean checkOpen = open != null;
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

    @GetMapping("/{uuid}/form")
    public ResponseEnvelope<?> findEventSuccessJsonString(@PathVariable UUID uuid, @RequestParam String token){
        SuccessRecord successRecord = eventService.findEventSuccessJsonString(uuid, token);
        return new ResponseEnvelope<>(new SuccessRecordJsonStringRes(successRecord));
    }
}
