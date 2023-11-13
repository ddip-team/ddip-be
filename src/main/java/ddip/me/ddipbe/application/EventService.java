package ddip.me.ddipbe.application;

import ddip.me.ddipbe.application.exception.*;
import ddip.me.ddipbe.domain.Event;
import ddip.me.ddipbe.domain.Member;
import ddip.me.ddipbe.domain.SuccessRecord;
import ddip.me.ddipbe.domain.repository.EventRepository;
import ddip.me.ddipbe.domain.repository.MemberRepository;
import ddip.me.ddipbe.domain.repository.PermitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final PermitRepository permitRepository;

    private final MemberRepository memberRepository; // TODO - MemberServiceLayer에서 호출로 추후 리팩터링

    @Transactional
    public Event createEvent(String title, Integer permitCount, String content, ZonedDateTime start, ZonedDateTime end, Long memberId, Map<String,String> jsonString) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new EventNotFoundException("ID가 존재하지 않습니다"));

        if (!eventEndTimeIsValidValue(start, end)) {
            throw new EventDateInvalidException();
        }

        Event event = new Event(
                UUID.randomUUID(),
                title,
                permitCount,
                content,
                start,
                end,
                findMember,
                jsonString
        );
        event = eventRepository.save(event);

        return event;
    }

    public Event findEventByUuid(UUID uuid) {
        return eventRepository.findByUuid(uuid)
                .orElseThrow(() -> new EventNotFoundException("DB에서 UUID를 찾을 수 없습니다."));
    }

    public List<Event> findOwnEvent(Long memberId, boolean filterOpen) {
        Member foundMember = memberRepository.findById(memberId).orElseThrow(() -> new EventNotFoundException("ID가 존재하지 않습니다"));
        if (filterOpen) {
            ZonedDateTime now = ZonedDateTime.now();
            return eventRepository.findAllByStartDateTimeBeforeAndEndDateTimeAfter(now, now);
        }
        return eventRepository.findAllByMember(foundMember);
    }

    private boolean eventEndTimeIsValidValue(ZonedDateTime start, ZonedDateTime end) {
        return end.isAfter(start) && end.isAfter(ZonedDateTime.now());
    }

    @Transactional
    public void applyEvent(UUID uuid, String token) {
        Event event = eventRepository.findByUuidForUpdate(uuid)
                .orElseThrow(() -> new EventNotFoundException("DB에서 UUID를 찾을 수 없습니다."));

        ZonedDateTime now = ZonedDateTime.now();
        if (!event.isOpen(now)) {
            throw new EventNotOpenException();
        }

        if (permitRepository.existsByEventUuidAndToken(uuid, token)) {
            throw new EventAlreadyAppliedException();
        }

        boolean success = event.decreaseRemainCount();
        if (!success) {
            throw new EventCapacityFullException();
        }

        event.addPermit(new SuccessRecord(token, event,ZonedDateTime.now()));
    }

    public Event findSuccessEvent(UUID uuid, String token) {
        SuccessRecord successRecord = permitRepository.findByEventUuidAndToken(uuid, token).orElseThrow(PermitNotFoundException::new);
        return successRecord.getEvent();
    }

    public SuccessRecord findEventSuccessJsonString(UUID uuid, String token){
        return permitRepository.findByEventUuidAndToken(uuid, token).orElseThrow(PermitNotFoundException::new);
    }

    @Transactional
    public SuccessRecord createSuccessRecordJsonString(UUID uuid, Map<String,String> jsonString, String token){
        SuccessRecord successRecord = permitRepository.findByEventUuidAndToken(uuid, token).orElseThrow(PermitNotFoundException::new);
        if (Optional.ofNullable(successRecord.getJsonString()).isEmpty()){
            successRecord.createJsonString(jsonString);
        }
        return successRecord;
    }

    public List<SuccessRecord> findSuccessRecords(UUID uuid, int pageIndex, int pageSize){
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("timeStamp").ascending());
        return permitRepository.findByEventUuidOrderByTimeStampAsc(uuid, pageable);
    }
}
