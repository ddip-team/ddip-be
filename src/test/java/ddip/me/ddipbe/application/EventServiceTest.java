package ddip.me.ddipbe.application;

import ddip.me.ddipbe.application.exception.EventDateInvalidException;
import ddip.me.ddipbe.application.exception.EventNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

// TODO: 테스트 코드 리팩토링
@Transactional
@SpringBootTest
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @DisplayName("이벤트 생성 시 유요하지 않은 회원 ID 입력시 예외가 발생한다.")
    @Test
    void eventCreateByFormattingValueExcepted_InvalidMemberId() {
        // given
        long memberId = 9999L;
        LocalDateTime now = LocalDateTime.now();

        // when, then
        assertThatThrownBy(() -> eventService.createEvent(
                "test",
                1,
                "test",
                now.minusDays(1),
                now.plusDays(1),
                memberId)
        ).isInstanceOf(EventNotFoundException.class);
    }

    @DisplayName("이벤트 조회 시 유효하지 않은 UUID 입력시 예외가 발생한다.")
    @Test
    void findEventByInvalidUuid() {
        // given
        UUID targetUuid = UUID.randomUUID();

        // when, then
        assertThatThrownBy(() -> {
                    eventService.findEventByUuid(targetUuid);
                }
        ).isInstanceOf(EventNotFoundException.class);
    }

    @DisplayName("이벤트 생성 시 유효하지 않은 날짜 입력시 예외가 발생한다.")
    @Test
    void eventCreateByFormattingValueExcepted_InvalidDateTime() {
        // given
        Long memberId = 1L;
        LocalDateTime now = LocalDateTime.now();

        // when, then
        assertThatThrownBy(() -> eventService.createEvent(
                "test",
                1,
                "test",
                now.minusDays(2),
                now.minusDays(1),
                memberId)
        ).isInstanceOf(EventDateInvalidException.class);
    }
}
