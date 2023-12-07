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
import ddip.me.ddipbe.global.util.CustomClock;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "events")
public class EventQueryService {

    private final EventRepository eventRepository;
    private final SuccessRecordRepository successRecordRepository;

    @Cacheable(key = "#uuid")
    public EventWithMemberDto findEventByUuid(UUID uuid) {
        Event event = eventRepository.findByUuid(uuid).orElseThrow(EventNotFoundException::new);
        return new EventWithMemberDto(event);
    }

    @Cacheable(key = "#memberId + #page + #size + #filterOpen.toString()")
    public Page<EventDto> findOwnEvents(long memberId, int page, int size, boolean filterOpen) {
        Page<Event> eventPage;
        if (filterOpen) {
            eventPage = eventRepository.findAllByMemberIdAndOpen(
                    memberId,
                    CustomClock.now(),
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

    @Cacheable(key = "#uuid + #memberId + #token")
    public SuccessResult findEventSuccessResult(UUID uuid, Long memberId, String token) {
        Event event = eventRepository.findByUuid(uuid).orElseThrow(EventNotFoundException::new);

        if (token != null) {
            if (!successRecordRepository.existsByEventIdAndToken(event.getId(), token)) {
                throw new SuccessRecordNotFoundException();
            }
            return event.getSuccessResult();
        }

        if (memberId == null || !event.isOwnedBy(memberId)) {
            throw new NotEventOwnerException();
        }

        return event.getSuccessResult();
    }

    @Cacheable(key = "#memberId + #uuid + #page + #size")
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

    @Cacheable(key = "#uuid + #token")
    public Map<String, Object> findSuccessRecordFormInputValue(UUID uuid, String token) {
        SuccessRecord successRecord = successRecordRepository.findByEventUuidAndToken(uuid, token)
                .orElseThrow(SuccessRecordNotFoundException::new);

        if (successRecord.getFormInputValue() == null) {
            throw new SuccessRecordNotFoundException();
        }

        return successRecord.getFormInputValue();
    }
}

