package ddip.me.ddipbe.presentation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConcurrentEventControllerTest extends IntegrationTest {

    static int THREAD_COUNT = 100;

    @AfterEach
    void tearDown() {
        truncateDatabase();
    }

    @Test
    void applyEvent_concurrent() throws InterruptedException {
        // given
        long memberId = signup();
        UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // when
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    String path = UriComponentsBuilder.fromPath("/events/{uuid}/apply")
                            .queryParam("token", UUID.randomUUID().toString())
                            .build().toString();
                    ResponseEntity<String> res = restTemplate.exchange(path,
                            HttpMethod.POST,
                            createHttpEntity(null, null),
                            String.class,
                            eventUuid);
                    System.out.println(res.getBody());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        assertEventRemainCount(eventUuid, 0);
        assertSuccessRecordCount(eventUuid, 10);
    }

    private void assertEventRemainCount(UUID eventUuid, int count) {
        Integer remainCount = jdbcTemplate.queryForObject("SELECT remain_count FROM event WHERE uuid = UUID_TO_BIN(?)", Integer.class, eventUuid.toString());
        assertThat(remainCount).isEqualTo(count);
    }

    private void assertSuccessRecordCount(UUID eventUuid, int count) {
        Integer successRecordCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM success_record WHERE event_id = (SELECT id FROM event WHERE uuid = UUID_TO_BIN(?))", Integer.class, eventUuid.toString());
        assertThat(successRecordCount).isEqualTo(count);
    }
}
