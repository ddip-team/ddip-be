package ddip.me.ddipbe.presentation;

import ddip.me.ddipbe.application.EventCommandService;
import ddip.me.ddipbe.application.EventQueryService;
import ddip.me.ddipbe.application.dto.EventDto;
import ddip.me.ddipbe.application.dto.EventWithMemberDto;
import ddip.me.ddipbe.application.dto.SuccessRecordDto;
import ddip.me.ddipbe.domain.SuccessResult;
import ddip.me.ddipbe.global.annotation.SessionMemberId;
import ddip.me.ddipbe.global.dto.ResponseEnvelope;
import ddip.me.ddipbe.presentation.dto.request.CreateEventReq;
import ddip.me.ddipbe.presentation.dto.request.PageReq;
import ddip.me.ddipbe.presentation.dto.request.RegisterFormInputValueReq;
import ddip.me.ddipbe.presentation.dto.response.EventDetailRes;
import ddip.me.ddipbe.presentation.dto.response.EventUuidRes;
import ddip.me.ddipbe.presentation.dto.response.FormInputValueRes;
import ddip.me.ddipbe.presentation.dto.response.PageRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("events")
public class EventController {

    private final EventQueryService eventQueryService;
    private final EventCommandService eventCommandService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEnvelope<EventUuidRes> createEvent(
            @Valid @RequestBody CreateEventReq createEventReq,
            @SessionMemberId Long memberId
    ) {
        UUID eventUuid = eventCommandService.createEvent(
                createEventReq.title(),
                createEventReq.limitCount(),
                createEventReq.successContent(),
                createEventReq.successImageUrl(),
                createEventReq.thumbnailImageUrl(),
                createEventReq.startDateTime(),
                createEventReq.endDateTime(),
                createEventReq.successForm(),
                memberId);

        return ResponseEnvelope.of(new EventUuidRes(eventUuid));
    }

    @GetMapping("{uuid}")
    public ResponseEnvelope<EventDetailRes> findEventByUuid(@PathVariable UUID uuid) {
        EventWithMemberDto eventByUuid = eventQueryService.findEventByUuid(uuid);
        return ResponseEnvelope.of(new EventDetailRes(eventByUuid));
    }

    @GetMapping("me")
    public ResponseEnvelope<PageRes<EventDto>> findOwnEvents(
            @SessionMemberId Long memberId,
            @Valid PageReq pageReq,
            @RequestParam(required = false) String open
    ) {
        boolean checkOpen = open != null;
        Page<EventDto> eventPage = eventQueryService.findOwnEvents(memberId, pageReq.page(), pageReq.size(), checkOpen);

        return ResponseEnvelope.of(new PageRes<>(eventPage));
    }

    @GetMapping("{uuid}/success-records")
    public ResponseEnvelope<PageRes<SuccessRecordDto>> findSuccessRecords(
            @SessionMemberId Long memberId,
            @PathVariable UUID uuid,
            @Valid PageReq pageReq
    ) {
        Page<SuccessRecordDto> successRecordPage = eventQueryService.findSuccessRecords(
                memberId,
                uuid,
                pageReq.page(),
                pageReq.size());
        return ResponseEnvelope.of(new PageRes<>(successRecordPage));
    }

    @DeleteMapping("{uuid}")
    public ResponseEnvelope<?> deleteEvent(@PathVariable UUID uuid, @SessionMemberId Long memberId) {
        eventCommandService.deleteEvent(uuid, memberId);
        return ResponseEnvelope.of(null);
    }

    @PutMapping("{uuid}")
    public ResponseEnvelope<?> updateEvent(
            @PathVariable UUID uuid,
            @RequestBody CreateEventReq createEventReq,
            @SessionMemberId Long memberId
    ) {
        eventCommandService.updateEvent(
                uuid,
                createEventReq.title(),
                createEventReq.limitCount(),
                createEventReq.successContent(),
                createEventReq.successImageUrl(),
                createEventReq.thumbnailImageUrl(),
                createEventReq.startDateTime(),
                createEventReq.endDateTime(),
                createEventReq.successForm(),
                memberId);
        return ResponseEnvelope.of(null);
    }

    @PostMapping("{uuid}/apply")
    public ResponseEnvelope<?> applyEvent(@PathVariable UUID uuid, @RequestParam String token) {
        eventCommandService.applyEvent(uuid, token);
        return ResponseEnvelope.of(null);
    }

    @GetMapping("{uuid}/form")
    public ResponseEnvelope<FormInputValueRes> findSuccessRecordFormInputValue(
            @PathVariable UUID uuid,
            @RequestParam String token
    ) {
        Map<String, Object> formInputValue = eventQueryService.findSuccessRecordFormInputValue(uuid, token);
        return ResponseEnvelope.of(new FormInputValueRes(formInputValue));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("{uuid}/form")
    public ResponseEnvelope<?> registerSuccessRecordFormInputValue(
            @PathVariable UUID uuid,
            @Valid @RequestBody RegisterFormInputValueReq registerFormInputValueReq,
            @RequestParam String token
    ) {
        eventCommandService.registerSuccessRecordSuccessInputInfo(
                uuid,
                registerFormInputValueReq.formInputValue(),
                token);
        return ResponseEnvelope.of(null);
    }

    @GetMapping("{uuid}/success")
    public ResponseEnvelope<SuccessResult> findSuccessEvent(
            @SessionMemberId(required = false) Long memberId,
            @PathVariable UUID uuid,
            @RequestParam(required = false) String token
    ) {
        SuccessResult successResult = eventQueryService.findSuccessEvent(uuid, memberId, token);
        return ResponseEnvelope.of(successResult);
    }
}