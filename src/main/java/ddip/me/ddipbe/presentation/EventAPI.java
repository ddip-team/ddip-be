package ddip.me.ddipbe.presentation;

import ddip.me.ddipbe.application.EventService;
import ddip.me.ddipbe.global.annotation.SessionMemberId;
import ddip.me.ddipbe.global.dto.ResponseEnvelope;
import ddip.me.ddipbe.presentation.dto.request.EventCreateReqDTO;
import ddip.me.ddipbe.presentation.dto.response.EventCommonResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventAPI {

    private final EventService eventService;
    @PostMapping
    public ResponseEnvelope<?> createNovelEvent(@RequestBody EventCreateReqDTO eventCreateReqDTO,@SessionMemberId Long memberId){
        EventCommonResDTO novelEvent = eventService.createNovelEvent(eventCreateReqDTO, memberId);
        return new ResponseEnvelope<>("success",novelEvent,null);
    }
}
