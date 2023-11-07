package ddip.me.ddipbe.application;

import ddip.me.ddipbe.application.exception.*;
import ddip.me.ddipbe.domain.Event;
import ddip.me.ddipbe.domain.Member;
import ddip.me.ddipbe.domain.Permit;
import ddip.me.ddipbe.domain.repository.EventRepository;
import ddip.me.ddipbe.domain.repository.MemberRepository;
import ddip.me.ddipbe.domain.repository.PermitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
public class EventService {

    private final EventRepository eventRepository;
    private final PermitRepository permitRepository;

    private final MemberRepository memberRepository; // TODO - MemberServiceLayer에서 호출로 추후 리팩터링

    @Transactional
    public UUID createEvent(String title, Integer permitCount, String content, LocalDateTime start, LocalDateTime end, Long memberId) {
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
                findMember
        );
        event = eventRepository.save(event);

        return event.getUuid();
    }

    public Event findEventByUuid(UUID uuid) {
        return eventRepository.findByUuid(uuid)
                .orElseThrow(() -> new EventNotFoundException("DB에서 UUID를 찾을 수 없습니다."));
    }

    public List<Event> findOwnEvent(Long memberId, boolean filterOpen) {
        Member foundMember = memberRepository.findById(memberId).orElseThrow(() -> new EventNotFoundException("ID가 존재하지 않습니다"));
        if (filterOpen) {
            LocalDateTime now = LocalDateTime.now();
            return eventRepository.findAllByStartBeforeAndEndAfter(now, now);
        }
        return eventRepository.findAllByMember(foundMember);
    }

    private boolean eventEndTimeIsValidValue(LocalDateTime start, LocalDateTime end) {
        return end.isAfter(start) && end.isAfter(LocalDateTime.now());
    }

    @Transactional
    public void applyEvent(UUID uuid, String token) {
        Event event = eventRepository.findByUuidForUpdate(uuid)
                .orElseThrow(() -> new EventNotFoundException("DB에서 UUID를 찾을 수 없습니다."));

        LocalDateTime now = LocalDateTime.now();
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

        event.addPermit(new Permit(token, event));
    }

    public Event findSuccessEvent(UUID uuid, String token) {
        Permit permit = permitRepository.findByEventUuidAndToken(uuid, token).orElseThrow(PermitNotFoundException::new);
        return permit.getEvent();
    }
}
