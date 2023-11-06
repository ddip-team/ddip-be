package ddip.me.ddipbe.application;

import ddip.me.ddipbe.application.exception.NotFoundUuidException;
import ddip.me.ddipbe.domain.Event;
import ddip.me.ddipbe.domain.repository.EventRepository;
import ddip.me.ddipbe.presentation.dto.request.EventCreateReqDTO;
import ddip.me.ddipbe.presentation.dto.response.EventCommonResDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
public class EventService {

    private final EventRepository eventRepository;

    @Transactional
    public EventCommonResDTO createNovelEvent(EventCreateReqDTO eventCreateDTO,Long memberId) {
        //TODO memberID find
        Event novelEvent = eventRepository.save(
                new Event(UUID.randomUUID(),
                        eventCreateDTO.getTitle(),
                        eventCreateDTO.getPermitCount(),
                        eventCreateDTO.getContent(),
                        eventCreateDTO.getStart(),
                        eventCreateDTO.getEnd())
        );

        return new EventCommonResDTO(novelEvent);
    }

    public EventCommonResDTO findEventByUuid(UUID uuid){
        Event findEvent = eventRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundUuidException("DB에서 UUID를 찾을 수 없습니다."));
        return new EventCommonResDTO(findEvent);
    }
}
