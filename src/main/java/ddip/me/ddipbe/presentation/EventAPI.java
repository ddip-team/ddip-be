package ddip.me.ddipbe.presentation;

import ddip.me.ddipbe.application.EventService;
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
public class EventAPI {

    private final EventService eventService;

    @PostMapping
    public ResponseEnvelope<?> createNovelEvent(@RequestBody EventCreateReqDTO eventCreateReqDTO, @SessionMemberId Long memberId) {
        EventCommonResDTO novelEvent = eventService.createNovelEvent(eventCreateReqDTO, memberId);
        return new ResponseEnvelope<>(null, novelEvent, null);
    }

    @GetMapping("/{uuid}")
    public ResponseEnvelope<?> findEventByUuid(@PathVariable UUID uuid) {
        EventCommonResDTO findEvent = eventService.findEventByUuid(uuid);
        return new ResponseEnvelope<>(null, findEvent, null);
    }

    @GetMapping("/me")
    public ResponseEnvelope<?> findOwnEvent(@SessionMemberId Long memberId) {
        List<EventCommonResDTO> ownEvents = eventService.findOwnEvent(memberId);
        return new ResponseEnvelope<>(null, ownEvents, null);
    }
}
