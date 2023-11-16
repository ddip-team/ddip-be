package ddip.me.ddipbe.presentation;

import ddip.me.ddipbe.application.EventService;
import ddip.me.ddipbe.domain.Event;
import ddip.me.ddipbe.domain.SuccessRecord;
import ddip.me.ddipbe.global.annotation.SessionMemberId;
import ddip.me.ddipbe.global.dto.ResponseEnvelope;
import ddip.me.ddipbe.presentation.dto.request.CreateEventReq;
import ddip.me.ddipbe.presentation.dto.request.PageReq;
import ddip.me.ddipbe.presentation.dto.request.RegisterFormInputValueReq;
import ddip.me.ddipbe.presentation.dto.response.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("events")
public class EventController {

    private final EventService eventService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEnvelope<EventUUIDRes> createEvent(
            @Valid @RequestBody CreateEventReq createEventReq,
            @SessionMemberId Long memberId
    ) {
        Event event = eventService.createEvent(
                createEventReq.title(),
                createEventReq.limitCount(),
                createEventReq.successContent(),
                createEventReq.successImageUrl(),
                createEventReq.thumbnailImageUrl(),
                createEventReq.startDateTime(),
                createEventReq.endDateTime(),
                createEventReq.successForm(),
                memberId);
        return ResponseEnvelope.of(new EventUUIDRes(event.getUuid()));
    }

    @GetMapping("{uuid}")
    public ResponseEnvelope<EventDetailRes> findEventByUuid(@PathVariable UUID uuid) {
        Event foundEvent = eventService.findEventByUuid(uuid);
        return ResponseEnvelope.of(new EventDetailRes(foundEvent));
    }

    @GetMapping("me")
    public ResponseEnvelope<PageRes<EventOwnRes>> findOwnEvents(
            @SessionMemberId Long memberId,
            @Valid PageReq pageReq,
            @RequestParam(required = false) String open
    ) {
        boolean checkOpen = open != null;
        Page<Event> ownEventPage = eventService.findOwnEvents(memberId, pageReq.page(), pageReq.size(), checkOpen);

        return ResponseEnvelope.of(new PageRes<>(ownEventPage.map(EventOwnRes::new)));
    }

    @GetMapping("{uuid}/success-records")
    public ResponseEnvelope<PageRes<SuccessRecordRes>> findSuccessRecords(@PathVariable UUID uuid, @Valid PageReq pageReq) {
        Page<SuccessRecord> successRecords = eventService.findSuccessRecords(
                uuid,
                pageReq.page(),
                pageReq.size());
        return ResponseEnvelope.of(new PageRes<>(successRecords.map(SuccessRecordRes::new)));
    }

    @DeleteMapping("{uuid}")
    public ResponseEnvelope<?> deleteEvent(@PathVariable UUID uuid, @SessionMemberId Long memberId) {
        eventService.deleteEvent(uuid, memberId);
        return ResponseEnvelope.of(null);
    }

    @PutMapping("{uuid}")
    public ResponseEnvelope<?> updateEvent(
            @PathVariable UUID uuid,
            @RequestBody CreateEventReq createEventReq,
            @SessionMemberId Long memberId
    ) {
        eventService.updateEvent(
                uuid,
                createEventReq.title(),
                createEventReq.limitCount(),
                createEventReq.successContent(),
                createEventReq.successImageUrl(),
                createEventReq.startDateTime(),
                createEventReq.endDateTime(),
                createEventReq.successForm(),
                memberId);
        return ResponseEnvelope.of(null);
    }

    @PostMapping("{uuid}/apply")
    public ResponseEnvelope<?> applyEvent(@PathVariable UUID uuid, @RequestParam String token) {
        eventService.applyEvent(uuid, token);
        return ResponseEnvelope.of(null);
    }

    @GetMapping("{uuid}/form")
    public ResponseEnvelope<FormInputValueRes> findSuccessRecordFormInputValue(
            @PathVariable UUID uuid,
            @RequestParam String token
    ) {
        SuccessRecord successRecord = eventService.findSuccessRecord(uuid, token);
        return ResponseEnvelope.of(new FormInputValueRes(successRecord));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("{uuid}/form")
    public ResponseEnvelope<?> registerSuccessRecordFormInputValue(
            @PathVariable UUID uuid,
            @Valid @RequestBody RegisterFormInputValueReq registerFormInputValueReq,
            @RequestParam String token
    ) {
        eventService.registerSuccessRecordSuccessInputInfo(
                uuid,
                registerFormInputValueReq.formInputValue(),
                token);
        return ResponseEnvelope.of(null);
    }

    @GetMapping("{uuid}/success")
    public ResponseEnvelope<EventSuccessRes> findSuccessEvent(@PathVariable UUID uuid, @RequestParam String token) {
        Event event = eventService.findSuccessEvent(uuid, token);
        return ResponseEnvelope.of(new EventSuccessRes(event));
    }
}