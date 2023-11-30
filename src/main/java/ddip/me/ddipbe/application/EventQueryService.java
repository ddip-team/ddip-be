package ddip.me.ddipbe.application;

import ddip.me.ddipbe.application.dto.EventDto;
import ddip.me.ddipbe.application.dto.EventWithMemberDto;
import ddip.me.ddipbe.application.dto.SuccessRecordDto;
import ddip.me.ddipbe.domain.Event;
import ddip.me.ddipbe.domain.SuccessRecord;
import ddip.me.ddipbe.domain.SuccessResult;
import ddip.me.ddipbe.domain.exception.EventNotFoundException;
import ddip.me.ddipbe.domain.exception.NotEventOwnerException;
import ddip.me.ddipbe.domain.exception.SuccessRecordNotFoundException;
import ddip.me.ddipbe.domain.repository.EventRepository;
import ddip.me.ddipbe.domain.repository.SuccessRecordRepository;
import ddip.me.ddipbe.global.dto.CustomPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class EventQueryService {

    private final EventRepository eventRepository;
    private final SuccessRecordRepository successRecordRepository;

    public EventWithMemberDto findEventByUuid(UUID uuid) {
        Event event = eventRepository.findByUuid(uuid).orElseThrow(EventNotFoundException::new);
        return new EventWithMemberDto(event);
    }

    public Page<EventDto> findOwnEvents(long memberId, int page, int size, boolean filterOpen) {
        Page<Event> eventPage;
        if (filterOpen) {
            eventPage = eventRepository.findAllByMemberIdAndOpen(
                    memberId,
                    ZonedDateTime.now(),
                    CustomPageable.of(page, size, Sort.by("createdAt").descending())
            );
        } else {
            eventPage = eventRepository.findAllByMemberId(
                    memberId,
                    CustomPageable.of(page, size, Sort.by("createdAt").descending())
            );
        }
        return eventPage.map(EventDto::new);
    }

    public SuccessResult findSuccessEvent(UUID uuid, Long memberId, String token) {
        if (memberId == null) {
            if (token == null || !successRecordRepository.existsByEventUuidAndToken(uuid, token)) {
                throw new SuccessRecordNotFoundException();
            }
        }

        Event event = eventRepository.findByUuid(uuid).orElseThrow(EventNotFoundException::new);

        if (memberId != null && !event.isOwnedBy(memberId)) {
            throw new NotEventOwnerException();
        }

        return event.getSuccessResult();
    }

    public Page<SuccessRecordDto> findSuccessRecords(long memberId, UUID uuid, int page, int size) {
        Event event = eventRepository.findByUuid(uuid).orElseThrow(EventNotFoundException::new);

        if (!event.isOwnedBy(memberId)) {
            throw new NotEventOwnerException();
        }

        Page<SuccessRecord> successRecordPage = successRecordRepository.findAllByEventUuid(
                uuid,
                CustomPageable.of(page, size, Sort.by("createdAt").ascending())
        );

        return successRecordPage.map(SuccessRecordDto::new);
    }

    public Map<String, Object> findSuccessRecordFormInputValue(UUID uuid, String token) {
        SuccessRecord successRecord = successRecordRepository.findByEventUuidAndToken(uuid, token)
                .orElseThrow(SuccessRecordNotFoundException::new);
        return successRecord.getFormInputValue();
    }
}
