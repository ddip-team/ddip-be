package ddip.me.ddipbe.application;

import ddip.me.ddipbe.domain.Event;
import ddip.me.ddipbe.domain.Member;
import ddip.me.ddipbe.domain.SuccessRecord;
import ddip.me.ddipbe.domain.exception.*;
import ddip.me.ddipbe.domain.repository.EventRepository;
import ddip.me.ddipbe.domain.repository.SuccessRecordRepository;
import ddip.me.ddipbe.global.util.CustomClock;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class EventCommandService {

    private final EventRepository eventRepository;
    private final SuccessRecordRepository successRecordRepository;

    public UUID createEvent(String title,
                            Integer permitCount,
                            String successContent,
                            String successImageUrl,
                            String thumbnailImageUrl,
                            ZonedDateTime start,
                            ZonedDateTime end,
                            Map<String, Object> successForm,
                            long memberId) {
        Event event = new Event(
                title,
                permitCount,
                successContent,
                successImageUrl,
                thumbnailImageUrl,
                start,
                end,
                successForm,
                new Member(memberId)
        );
        event = eventRepository.save(event);

        return event.getUuid();
    }

    public void deleteEvent(UUID uuid, Long memberId) {
        Event event = eventRepository.findByUuid(uuid).orElseThrow(EventNotFoundException::new);

        if (!event.isOwnedBy(memberId)) {
            throw new NotEventOwnerException();
        }

        if (!event.isDeletable()) {
            throw new EventNotDeletableException();
        }

        eventRepository.delete(event);
    }

    public void updateEvent(
            UUID uuid,
            String title,
            Integer limitCount,
            String successContent,
            String successImageUrl,
            String thumbnailImageUrl,
            ZonedDateTime startDateTime,
            ZonedDateTime endDateTime,
            Map<String, Object> successForm,
            long memberId
    ) {
        Event event = eventRepository.findByUuid(uuid).orElseThrow(EventNotFoundException::new);

        if (!event.isOwnedBy(memberId)) {
            throw new NotEventOwnerException();
        }

        if (!event.isEditable()) {
            throw new EventNotEditableException();
        }

        event.update(
                title,
                limitCount,
                successContent,
                successImageUrl,
                thumbnailImageUrl,
                startDateTime,
                endDateTime,
                successForm
        );
    }

    public void applyEvent(UUID uuid, String token) {
        Event event = eventRepository.findByUuidForUpdate(uuid).orElseThrow(EventNotFoundException::new);

        if (!event.getEventDuration().isOpen(CustomClock.now())) {
            throw new EventNotOpenException();
        }

        if (successRecordRepository.existsByEventIdAndToken(event.getId(), token)){
            throw new EventAlreadyAppliedException();
        }

        event.apply(new SuccessRecord(token, event));
    }

    public void registerSuccessRecordSuccessInputInfo(UUID uuid, Map<String, Object> formInputValue, String token) {
        SuccessRecord successRecord = successRecordRepository.findByEventUuidAndToken(uuid, token).orElseThrow(SuccessRecordNotFoundException::new);

        successRecord.registerFormInputValue(formInputValue);
    }
}
