package ddip.me.ddipbe.application;

import ddip.me.ddipbe.application.exception.EventDateInvalidException;
import ddip.me.ddipbe.application.exception.EventNotFoundException;
import ddip.me.ddipbe.domain.Event;
import ddip.me.ddipbe.domain.Member;
import ddip.me.ddipbe.domain.repository.EventRepository;
import ddip.me.ddipbe.domain.repository.MemberRepository;
import ddip.me.ddipbe.presentation.dto.request.EventCreateReqDTO;
import ddip.me.ddipbe.presentation.dto.response.EventCommonResDTO;
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

    private final MemberRepository memberRepository; // TODO - MemberServiceLayer에서 호출로 추후 리팩터링

    @Transactional
    public Event createEvent(EventCreateReqDTO eventCreateDTO, Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new EventNotFoundException("ID가 존재하지 않습니다"));
        if (!eventEndTimeIsValidValue(eventCreateDTO.getStart(), eventCreateDTO.getEnd())) {
            throw new EventDateInvalidException();
        }
        return eventRepository.save(
                new Event(
                        UUID.randomUUID(),
                        eventCreateDTO.getTitle(),
                        eventCreateDTO.getPermitCount(),
                        eventCreateDTO.getContent(),
                        eventCreateDTO.getStart(),
                        eventCreateDTO.getEnd(),
                        findMember
                )
        );
    }

    public Event findEventByUuid(UUID uuid) {
        return eventRepository.findByUuid(uuid)
                .orElseThrow(() -> new EventNotFoundException("DB에서 UUID를 찾을 수 없습니다."));
    }

    public List<EventCommonResDTO> findOwnEvent(Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new EventNotFoundException("ID가 존재하지 않습니다"));
        List<Event> ownEvents = eventRepository.findByMember(findMember);
        return ownEvents.stream()
                .filter(event -> {
                    LocalDateTime now = LocalDateTime.now();
                    return event.getStart().isBefore(now) && event.getEnd().isAfter(now);
                })
                .map(EventCommonResDTO::new).toList();
    }

    private boolean eventEndTimeIsValidValue(LocalDateTime start, LocalDateTime end) {
        return end.isAfter(start) && end.isAfter(LocalDateTime.now());
    }
}
