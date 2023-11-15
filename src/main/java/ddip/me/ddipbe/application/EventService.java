package ddip.me.ddipbe.application;

import ddip.me.ddipbe.application.exception.*;
import ddip.me.ddipbe.domain.Event;
import ddip.me.ddipbe.domain.Member;
import ddip.me.ddipbe.domain.SuccessRecord;
import ddip.me.ddipbe.domain.repository.EventRepository;
import ddip.me.ddipbe.domain.repository.MemberRepository;
import ddip.me.ddipbe.domain.repository.SuccessRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final SuccessRecordRepository successRecordRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Event createEvent(String title,
                             Integer permitCount,
                             String successContent,
                             String successImageUrl,
                             ZonedDateTime start,
                             ZonedDateTime end,
                             Map<String, Object> successForm,
                             Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(EventNotFoundException::new);

        if (!eventDateTimeIsValid(start, end)) {
            throw new EventDateInvalidException();
        }

        Event event = new Event(
                UUID.randomUUID(),
                title,
                permitCount,
                successContent,
                successImageUrl,
                successImageUrl,
                start,
                end,
                successForm,
                findMember
        );
        event = eventRepository.save(event);

        return event;
    }

    private boolean eventDateTimeIsValid(ZonedDateTime start, ZonedDateTime end) {
        return end.isAfter(start) && end.isAfter(ZonedDateTime.now());
    }

    public Event findEventByUuid(UUID uuid) {
        return eventRepository.findByUuid(uuid).orElseThrow(EventNotFoundException::new);
    }

    public Page<Event> findOwnEvents(Long memberId, int page, int size, boolean filterOpen) {
        Member foundMember = memberRepository.findById(memberId).orElseThrow(EventNotFoundException::new);

        if (filterOpen) {
            ZonedDateTime now = ZonedDateTime.now();
            return eventRepository.findAllByMemberAndStartDateTimeBeforeAndEndDateTimeAfter(
                    foundMember,
                    now,
                    now,
                    PageRequest.of(page - 1, size, Sort.by("createdAt").descending())
            );
        } else {
            return eventRepository.findAllByMember(
                    foundMember,
                    PageRequest.of(page - 1, size, Sort.by("createdAt").descending())
            );
        }
    }

    public Page<SuccessRecord> findSuccessRecords(UUID uuid, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("timestamp").ascending());
        return successRecordRepository.findAllByEventUuid(uuid, pageable);
    }

    @Transactional
    public void updateEvent(
            UUID uuid,
            String title,
            Integer limitCount,
            String successContent,
            String successImageUrl,
            ZonedDateTime startDateTime,
            ZonedDateTime endDateTime,
            Map<String, Object> successForm,
            Long memberId
    ) {
        Event event = eventRepository.findByUuid(uuid).orElseThrow(EventNotFoundException::new);

        if (!event.getMember().getId().equals(memberId)) {
            throw new NotEventOwnerException();
        }

        if (event.hasSuccessRecord() || event.ended()) {
            throw new EventNotEditableException();
        }

        if (!eventDateTimeIsValid(startDateTime, endDateTime)) {
            throw new EventDateInvalidException();
        }

        event.update(
                title,
                limitCount,
                successContent,
                successImageUrl,
                startDateTime,
                endDateTime,
                successForm
        );
    }

    @Transactional
    public void applyEvent(UUID uuid, String token) {
        Event event = eventRepository.findByUuidForUpdate(uuid).orElseThrow(EventNotFoundException::new);

        if (!event.isOpen(ZonedDateTime.now())) {
            throw new EventNotOpenException();
        }

        if (successRecordRepository.existsByEventUuidAndToken(uuid, token)) {
            throw new EventAlreadyAppliedException();
        }

        boolean success = event.decreaseRemainCount();
        if (!success) {
            throw new EventCapacityFullException();
        }

        event.addSuccessRecord(new SuccessRecord(token, event, ZonedDateTime.now()));
    }

    public Event findSuccessEvent(UUID uuid, String token) {
        SuccessRecord successRecord = successRecordRepository.findByEventUuidAndToken(uuid, token)
                .orElseThrow(SuccessRecordNotFoundException::new);
        return successRecord.getEvent();
    }

    public SuccessRecord findSuccessRecord(UUID uuid, String token) {
        return successRecordRepository.findByEventUuidAndToken(uuid, token)
                .orElseThrow(SuccessRecordNotFoundException::new);
    }

    @Transactional
    public void registerSuccessRecordSuccessInputInfo(UUID uuid, Map<String, Object> formInputValue, String token) {
        SuccessRecord successRecord = successRecordRepository.findByEventUuidAndToken(uuid, token)
                .orElseThrow(SuccessRecordNotFoundException::new);
        if (!successRecord.registerFormInputValue(formInputValue)) {
            throw new SuccessFormAlreadyRegisteredException();
        }
    }
}
