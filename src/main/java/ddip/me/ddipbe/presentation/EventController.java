package ddip.me.ddipbe.presentation;

import ddip.me.ddipbe.application.EventService;
import ddip.me.ddipbe.application.model.Page;
import ddip.me.ddipbe.domain.Event;
import ddip.me.ddipbe.domain.SuccessRecord;
import ddip.me.ddipbe.global.annotation.SessionMemberId;
import ddip.me.ddipbe.global.dto.ResponseEnvelope;
import ddip.me.ddipbe.presentation.dto.request.CreateEventReq;
import ddip.me.ddipbe.presentation.dto.request.PageReq;
import ddip.me.ddipbe.presentation.dto.request.SuccessRecordPageReq;
import ddip.me.ddipbe.presentation.dto.request.UpdateSuccessInputInfoReq;
import ddip.me.ddipbe.presentation.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

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
                createEventReq.getSuccessImageUrl(),
                createEventReq.getStartDateTime(),
                createEventReq.getEndDateTime(),
                memberId);
        return new ResponseEnvelope<>(new EventUUIDRes(event.getUuid()));
    }

    @GetMapping("/{uuid}")
    public ResponseEnvelope<EventDetailRes> findEventByUuid(@PathVariable UUID uuid) {
        Event foundEvent = eventService.findEventByUuid(uuid);
        return new ResponseEnvelope<>(new EventDetailRes(foundEvent));
    }

    @GetMapping("/me")
    public ResponseEnvelope<Page<EventOwnRes>> findOwnEvent(
            @SessionMemberId Long memberId,
            PageReq pageReq,
            @RequestParam(required = false) String open
    ) {
        boolean checkOpen = open != null;
        Page<Event> ownEvents = eventService.findOwnEvents(memberId, pageReq.getPage(), pageReq.getSize(), checkOpen);

        return new ResponseEnvelope<>(
                new Page<>(
                        ownEvents.getPageInfo(),
                        ownEvents.getPageData().stream().map(EventOwnRes::new).toList()
                )
        );
    }

    @PostMapping("/{uuid}/apply")
    public ResponseEnvelope<?> applyEvent(@PathVariable UUID uuid, @RequestParam String token) {
        eventService.applyEvent(uuid, token);
        return new ResponseEnvelope<>(null);
    }

    @GetMapping("/{uuid}/success")
    public ResponseEnvelope<EventSuccessRes> findSuccessEvent(@PathVariable UUID uuid, @RequestParam String token) {
        Event event = eventService.findSuccessEvent(uuid, token);
        return new ResponseEnvelope<>(new EventSuccessRes(event));
    }

    @GetMapping("/{uuid}/form")
    public ResponseEnvelope<SuccessRecordSuccessInputInfoRes> findSuccessRecordSuccessInputInfo(@PathVariable UUID uuid, @RequestParam String token) {
        SuccessRecord successRecord = eventService.findEventSuccessJsonString(uuid, token);
        return new ResponseEnvelope<>(new SuccessRecordSuccessInputInfoRes(successRecord));
    }

    @PostMapping("/{uuid}/form")
    public ResponseEnvelope<SuccessRecordSuccessInputInfoRes> updateSuccessRecordSuccessInputInfo(@PathVariable UUID uuid, @RequestBody UpdateSuccessInputInfoReq updateSuccessInputInfoReq, @RequestParam String token) {
        SuccessRecord successRecord = eventService.updateSuccessRecordSuccessInputInfo(uuid, updateSuccessInputInfoReq.getSuccessInputInfo(), token);
        return new ResponseEnvelope<>(new SuccessRecordSuccessInputInfoRes(successRecord));
    }

    @GetMapping("/{uuid}/success-records")
    public ResponseEnvelope<?> findSuccessRecords(@PathVariable UUID uuid, SuccessRecordPageReq successRecordPageReq) {
        List<SuccessRecord> successRecords = eventService.findSuccessRecords(uuid,
                successRecordPageReq.getPageIndex(),
                successRecordPageReq.getPageSize(),
                successRecordPageReq.getSortProperty());
        List<SuccessRecordPageRes> pageSuccessRecords = successRecords.stream().map(SuccessRecordPageRes::new).toList();
        return new ResponseEnvelope<>(pageSuccessRecords);
    }
}