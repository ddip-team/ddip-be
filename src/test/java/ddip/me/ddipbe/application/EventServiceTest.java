package ddip.me.ddipbe.application;

import ddip.me.ddipbe.application.exception.NotFoundIdException;
import ddip.me.ddipbe.presentation.dto.request.EventCreateReqDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.*;


@SpringBootTest
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Test
    @DisplayName("이벤트 생성 시 유효하지 못한 회원 ID 입력 예외 테스트")
    void EventCreateByFormattingValueExcepted_InvalidMemberId(){
        //given
        Long memberId = 9999L;

        //when
        assertThatThrownBy(()->{
                eventService.createNovelEvent(initializingCreateDTO(),memberId);
            }
                ).isInstanceOf(NotFoundIdException.class);
    }


    private EventCreateReqDTO initializingCreateDTO(){
        return new EventCreateReqDTO("test",1,"test", now().minusDays(1), now().plusDays(1));
    }
}
