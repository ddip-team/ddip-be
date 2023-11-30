package ddip.me.ddipbe.presentation;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import ddip.me.ddipbe.global.util.TestCustomClock;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventControllerTest extends IntegrationTest {

    @BeforeAll
    static void beforeAll() {
        now = ZonedDateTime.parse("2023-11-30T09:00:00Z");
    }

    @BeforeEach
    void setUp() {
        testCustomClock.setNow(now);
    }

    @AfterEach
    void tearDown() {
        truncateDatabase();
        TestCustomClock.reset();
    }

    @Nested
    class createEvent {
        @DisplayName("이벤트 생성을 성공한다.")
        @Test
        void createEvent_success() {
            // given
            signup();
            String sessionCookie = signinForSessionCookie();
            String body = """
                    {
                        "title": "title",
                        "limitCount": 10,
                        "successContent": "content",
                        "successImageUrl": "https://cdn.ddip.me/success-image.png",
                        "thumbnailImageUrl": "https://cdn.ddip.me/thumbnail-image.png",
                        "startDateTime": "2023-11-30T10:10:00Z",
                        "endDateTime": "2023-11-30T11:10:00Z",
                        "successForm": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events",
                    HttpMethod.POST,
                    createHttpEntity(body, sessionCookie),
                    String.class);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            System.out.println(res.getBody());
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(json.read("$.data.eventUuid", String.class)).isNotNull().matches(UUID_REGEX);
        }

        @DisplayName("이벤트 생성을 실패한다. (startDateTime에 초 단위가 포함된 경우)")
        @Test
        void createEvent_fail_startDateTimeHasSecond() {
            // given
            signup();
            String sessionCookie = signinForSessionCookie();
            String body = """
                    {
                        "title": "title",
                        "limitCount": 10,
                        "successContent": "content",
                        "successImageUrl": "https://cdn.ddip.me/success-image.png",
                        "thumbnailImageUrl": "https://cdn.ddip.me/thumbnail-image.png",
                        "startDateTime": "2023-11-30T10:10:10Z",
                        "endDateTime": "2023-11-30T11:10:10Z",
                        "successForm": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events",
                    HttpMethod.POST,
                    createHttpEntity(body, sessionCookie),
                    String.class);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            System.out.println(res.getBody());
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Disabled("TODO: 구현 예정")
        @DisplayName("이벤트 생성을 실패한다. (successContent와 successImageUrl이 모두 null인 경우)")
        @Test
        void createEvent_fail_bothSuccessContentAndSuccessImageUrlNull() {
            // given
            signup();
            String sessionCookie = signinForSessionCookie();
            String body = """
                    {
                        "title": "title",
                        "limitCount": 10,
                        "successContent": null,
                        "successImageUrl": null,
                        "thumbnailImageUrl": "https://cdn.ddip.me/thumbnail-image.png",
                        "startDateTime": "2023-11-30T10:10:00Z",
                        "endDateTime": "2023-11-30T11:10:00Z",
                        "successForm": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events",
                    HttpMethod.POST,
                    createHttpEntity(body, sessionCookie),
                    String.class);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            System.out.println(res.getBody());
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @DisplayName("이벤트 생성을 실패한다. (limitCount가 0인 경우)")
        @Test
        void createEvent_fail_limitCountZero() {
            // given
            signup();
            String sessionCookie = signinForSessionCookie();
            String body = """
                    {
                        "title": "title",
                        "limitCount": 0,
                        "successContent": "content",
                        "successImageUrl": "https://cdn.ddip.me/success-image.png",
                        "thumbnailImageUrl": "https://cdn.ddip.me/thumbnail-image.png",
                        "startDateTime": "2023-11-30T10:10:00Z",
                        "endDateTime": "2023-11-30T11:10:00Z",
                        "successForm": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events",
                    HttpMethod.POST,
                    createHttpEntity(body, sessionCookie),
                    String.class);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            System.out.println(res.getBody());
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @DisplayName("이벤트 생성을 실패한다. (limitCount가 음수인 경우)")
        @Test
        void createEvent_fail_limitCountNegative() {
            // given
            signup();
            String sessionCookie = signinForSessionCookie();
            String body = """
                    {
                        "title": "title",
                        "limitCount": -1,
                        "successContent": "content",
                        "successImageUrl": "https://cdn.ddip.me/success-image.png",
                        "thumbnailImageUrl": "https://cdn.ddip.me/thumbnail-image.png",
                        "startDateTime": "2023-11-30T10:10:00Z",
                        "endDateTime": "2023-11-30T11:10:00Z",
                        "successForm": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events",
                    HttpMethod.POST,
                    createHttpEntity(body, sessionCookie),
                    String.class);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            System.out.println(res.getBody());
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @DisplayName("이벤트 생성을 실패한다. (title이 빈 문자열인 경우)")
        @Test
        void createEvent_fail_titleEmptyString() {
            // given
            signup();
            String sessionCookie = signinForSessionCookie();
            String body = """
                    {
                        "title": "",
                        "limitCount": 10,
                        "successContent": "content",
                        "successImageUrl": "https://cdn.ddip.me/success-image.png",
                        "thumbnailImageUrl": "https://cdn.ddip.me/thumbnail-image.png",
                        "startDateTime": "2023-11-30T10:10:00Z",
                        "endDateTime": "2023-11-30T11:10:00Z",
                        "successForm": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events",
                    HttpMethod.POST,
                    createHttpEntity(body, sessionCookie),
                    String.class);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            System.out.println(res.getBody());
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @DisplayName("이벤트 생성을 실패한다. successImageUrl와 thumbnailImageUrl이 URL 형식이 아닌 경우")
        @Test
        void createEvent_fail_notUrl() {
            // given
            signup();
            String sessionCookie = signinForSessionCookie();
            String body = """
                    {
                        "title": "title",
                        "limitCount": 10,
                        "successContent": "content",
                        "successImageUrl": "test",
                        "thumbnailImageUrl": "test",
                        "startDateTime": "2023-11-30T10:10:00Z",
                        "endDateTime": "2023-11-30T11:10:00Z",
                        "successForm": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events",
                    HttpMethod.POST,
                    createHttpEntity(body, sessionCookie),
                    String.class);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            System.out.println(res.getBody());
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @DisplayName("이벤트 생성을 실패한다. (endDateTime이 startDateTime보다 빠른 경우)")
        @Test
        void createEvent_fail_endDateTimeBeforeStartDateTime() {
            // given
            signup();
            String sessionCookie = signinForSessionCookie();
            String body = """
                    {
                        "title": "title",
                        "limitCount": 10,
                        "successContent": "content",
                        "successImageUrl": "https://cdn.ddip.me/success-image.png",
                        "thumbnailImageUrl": "https://cdn.ddip.me/thumbnail-image.png",
                        "startDateTime": "2023-11-30T11:10:00Z",
                        "endDateTime": "2023-11-30T10:10:00Z",
                        "successForm": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events",
                    HttpMethod.POST,
                    createHttpEntity(body, sessionCookie),
                    String.class);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            System.out.println(res.getBody());
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @DisplayName("이벤트 생성을 실패한다. (endDateTime이 startDateTime과 같은 경우)")
        @Test
        void createEvent_fail_endDateTimeSameAsStartDateTime() {
            // given
            signup();
            String sessionCookie = signinForSessionCookie();
            String body = """
                    {
                        "title": "title",
                        "limitCount": 10,
                        "successContent": "content",
                        "successImageUrl": "https://cdn.ddip.me/success-image.png",
                        "thumbnailImageUrl": "https://cdn.ddip.me/thumbnail-image.png",
                        "startDateTime": "2023-11-30T10:10:00Z",
                        "endDateTime": "2023-11-30T10:10:00Z",
                        "successForm": {
                            "test": "test"
                        }
                    }
                    """;
            testCustomClock.setNow(ZonedDateTime.of(2023, 11, 30, 10, 10, 0, 0, ZoneId.of("UTC")));

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events",
                    HttpMethod.POST,
                    createHttpEntity(body, sessionCookie),
                    String.class);

            // then
            System.out.println(res.getBody());
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @DisplayName("이벤트 생성을 실패한다. (endDateTime이 현재 시간보다 빠른 경우)")
        @Test
        void createEvent_fail_endDateTimeBeforeNow() {
            // given
            signup();
            String sessionCookie = signinForSessionCookie();
            String body = """
                    {
                        "title": "title",
                        "limitCount": 10,
                        "successContent": "content",
                        "successImageUrl": "https://cdn.ddip.me/success-image.png",
                        "thumbnailImageUrl": "https://cdn.ddip.me/thumbnail-image.png",
                        "startDateTime": "2023-11-30T07:10:00Z",
                        "endDateTime": "2023-11-30T08:10:00Z",
                        "successForm": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events",
                    HttpMethod.POST,
                    createHttpEntity(body, sessionCookie),
                    String.class);

            // then
            System.out.println(res.getBody());
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Disabled("TODO: 구현 예정")
        @DisplayName("이벤트 생성을 실패한다. (startDateTime이 현재 시간보다 빠른 경우)")
        @Test
        void createEvent_fail_startDateTimeBeforeNow() {
            // given
            signup();
            String sessionCookie = signinForSessionCookie();
            String body = """
                    {
                        "title": "title",
                        "limitCount": 10,
                        "successContent": "content",
                        "successImageUrl": "https://cdn.ddip.me/success-image.png",
                        "thumbnailImageUrl": "https://cdn.ddip.me/thumbnail-image.png",
                        "startDateTime": "2023-11-29T10:10:00Z",
                        "endDateTime": "2023-11-30T11:10:00Z",
                        "successForm": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events",
                    HttpMethod.POST,
                    createHttpEntity(body, sessionCookie),
                    String.class);

            // then
            System.out.println(res.getBody());
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class findEventByUuid {
        @DisplayName("이벤트 조회를 성공한다.")
        @Test
        void findEventByUuid_success() {
            // given
            long memberId = signup();
            String sessionCookie = signinForSessionCookie();
            UUID eventUuid = addEvent(memberId, now.plusDays(1), now.plusDays(2), now);

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events/{uuid}",
                    HttpMethod.GET,
                    createHttpEntity(null, sessionCookie),
                    String.class,
                    eventUuid);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(json.read("$.data.uuid", String.class)).isEqualTo(eventUuid.toString());
            assertThat(json.read("$.data.title", String.class)).isEqualTo("title");
            assertThat(json.read("$.data.limitCount", Integer.class)).isEqualTo(10);
            assertThat(json.read("$.data.thumbnailImageUrl", String.class)).isEqualTo("https://cdn.ddip.me/thumbnail-image.png");
            assertThat(json.read("$.data.startDateTime", String.class)).isEqualTo(now.plusDays(1).format(formatter));
            assertThat(json.read("$.data.endDateTime", String.class)).isEqualTo(now.plusDays(2).format(formatter));
            assertThat(json.read("$.data.member.id", Long.class)).isEqualTo(memberId);
            assertThat(json.read("$.data.member.email", String.class)).isEqualTo(EMAIL);
        }

        @DisplayName("이벤트 조회를 실패한다. (존재하지 않는 이벤트인 경우)")
        @Test
        void findEventByUuid_fail_notFound() {
            // given
            signup();
            String sessionCookie = signinForSessionCookie();
            UUID eventUuid = UUID.randomUUID();

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events/{uuid}",
                    HttpMethod.GET,
                    createHttpEntity(null, sessionCookie),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class findOwnEvents {
        @DisplayName("내가 생성한 이벤트 목록 조회를 성공한다. (생성한 이벤트가 없는 경우, 빈 목록을 반환)")
        @Test
        void findOwnEvents_success_emptyList() {
            // given
            signup();
            String sessionCookie = signinForSessionCookie();

            // when
            String path = UriComponentsBuilder.fromPath("/events/me")
                    .queryParam("page", 1)
                    .queryParam("size", 10)
                    .build().toString();
            ResponseEntity<String> res = restTemplate.exchange(path,
                    HttpMethod.GET,
                    createHttpEntity(null, sessionCookie),
                    String.class);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(json.read("$.data.pageInfo.page", Integer.class)).isEqualTo(1);
            assertThat(json.read("$.data.pageInfo.size", Integer.class)).isEqualTo(10);
            assertThat(json.read("$.data.pageInfo.totalPage", Integer.class)).isEqualTo(0);
            assertThat(json.read("$.data.pageInfo.totalSize", Integer.class)).isEqualTo(0);
            assertThat(json.read("$.data.pageData.length()", Integer.class)).isEqualTo(0);
        }

        @DisplayName("내가 생성한 이벤트 목록 조회를 성공한다. (페이징 정상 처리 확인)")
        @Test
        void findOwnEvents_success_paging() {
            // given
            long memberId = signup();
            String sessionCookie = signinForSessionCookie();
            List<UUID> uuids = addEvents(memberId, 0, 8, 0);
            Collections.reverse(uuids);

            // when
            String url = UriComponentsBuilder.fromPath("/events/me")
                    .queryParam("page", 2)
                    .queryParam("size", 5)
                    .build().toString();
            ResponseEntity<String> res = restTemplate.exchange(url,
                    HttpMethod.GET,
                    createHttpEntity(null, sessionCookie),
                    String.class);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(json.read("$.data.pageData.length()", Integer.class)).isEqualTo(3);
            assertThat(json.read("$.data.pageData[*].uuid", List.class)).containsExactlyElementsOf(uuids.subList(5, 8).stream().map(UUID::toString).toList());
            assertThat(json.read("$.data.pageData[0].title", String.class)).isEqualTo("title");
            assertThat(json.read("$.data.pageData[0].limitCount", Integer.class)).isEqualTo(10);
            assertThat(json.read("$.data.pageData[0].thumbnailImageUrl", String.class)).isEqualTo("https://cdn.ddip.me/thumbnail-image.png");
            assertThat(json.read("$.data.pageData[0].startDateTime", String.class)).isEqualTo(now.minusDays(1).format(formatter));
            assertThat(json.read("$.data.pageData[0].endDateTime", String.class)).isEqualTo(now.plusDays(1).format(formatter));
        }

        @DisplayName("내가 생성한 이벤트 목록 조회를 성공한다. (open인 이벤트만 조회)")
        @Test
        void findOwnEvents_success_filterOpen() {
            // given
            long memberId = signup();
            String sessionCookie = signinForSessionCookie();
            List<UUID> uuids = addEvents(memberId, 5, 5, 5);
            Collections.reverse(uuids);

            // when
            String url = UriComponentsBuilder.fromPath("/events/me")
                    .queryParam("page", 1)
                    .queryParam("size", 10)
                    .queryParam("open", true)
                    .build().toString();
            ResponseEntity<String> res = restTemplate.exchange(url,
                    HttpMethod.GET,
                    createHttpEntity(null, sessionCookie),
                    String.class);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(json.read("$.data.pageData.length()", Integer.class)).isEqualTo(5);
            assertThat(json.read("$.data.pageData[*].uuid", List.class)).containsExactlyElementsOf(uuids.subList(5, 10).stream().map(UUID::toString).toList());
        }

        @DisplayName("내가 생성한 이벤트 목록 조회를 실패한다. (인증되지 않은 사용자인 경우)")
        @Test
        void findOwnEvents_fail_unauthorized() {
            // when
            String path = UriComponentsBuilder.fromPath("/events/me")
                    .queryParam("page", 1)
                    .queryParam("size", 10)
                    .build().toString();
            ResponseEntity<String> res = restTemplate.exchange(path,
                    HttpMethod.GET,
                    createHttpEntity(null, null),
                    String.class);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }

    @Nested
    class findSuccessRecords {
        @DisplayName("이벤트에 등록된 성공 기록 목록 조회를 성공한다. (성공 기록이 없는 경우, 빈 목록을 반환)")
        @Test
        void findSuccessRecords_success() {
            // given
            long memberId = signup();
            String sessionCookie = signinForSessionCookie();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);
            String path = UriComponentsBuilder.fromPath("/events/{uuid}/success-records")
                    .queryParam("page", 1)
                    .queryParam("size", 10)
                    .build().toString();

            // when
            ResponseEntity<String> res = restTemplate.exchange(path,
                    HttpMethod.GET,
                    createHttpEntity(null, sessionCookie),
                    String.class,
                    eventUuid);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(json.read("$.data.pageInfo.page", Integer.class)).isEqualTo(1);
            assertThat(json.read("$.data.pageInfo.size", Integer.class)).isEqualTo(10);
            assertThat(json.read("$.data.pageInfo.totalPage", Integer.class)).isEqualTo(0);
            assertThat(json.read("$.data.pageInfo.totalSize", Integer.class)).isEqualTo(0);
            assertThat(json.read("$.data.pageData.length()", Integer.class)).isEqualTo(0);
        }

        @DisplayName("이벤트에 등록된 성공 기록 목록 조회를 성공한다. (페이징 정상 처리 확인)")
        @Test
        void findSuccessRecords_success_paging() {
            // given
            long memberId = signup();
            String sessionCookie = signinForSessionCookie();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);
            List<String> tokens = addSuccessRecords(eventUuid, 8, false);

            // when
            String path = UriComponentsBuilder.fromPath("/events/{uuid}/success-records")
                    .queryParam("page", 2)
                    .queryParam("size", 5)
                    .build().toString();
            ResponseEntity<String> res = restTemplate.exchange(path,
                    HttpMethod.GET,
                    createHttpEntity(null, sessionCookie),
                    String.class,
                    eventUuid);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(json.read("$.data.pageData[*].token", List.class)).containsExactlyElementsOf(tokens.subList(5, 8));
            assertThat(json.read("$.data.pageData[0].createdAt", String.class)).isEqualTo(now.plusSeconds(5).format(formatter));
            assertThat(json.read("$.data.pageData[0].isFormInputValueRegistered", Boolean.class)).isEqualTo(false);
        }

        @DisplayName("이벤트에 등록된 성공 기록 목록 조회를 실패한다. (이벤트 소유자가 아닌 경우)")
        @Test
        void findSuccessRecords_fail_notEventOwner() {
            // given
            long memberId = signup();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);
            String path = UriComponentsBuilder.fromPath("/events/{uuid}/success-records")
                    .queryParam("page", 1)
                    .queryParam("size", 10)
                    .build().toString();
            signup("new@test.com", "test1234!@#$");
            String newSessionCookie = signinForSessionCookie("new@test.com", "test1234!@#$");

            // when
            ResponseEntity<String> res = restTemplate.exchange(path,
                    HttpMethod.GET,
                    createHttpEntity(null, newSessionCookie),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }
    }

    @Nested
    class deleteEvent {
        @DisplayName("이벤트 삭제를 성공한다.")
        @Test
        void deleteEvent_success() {
            // given
            long memberId = signup();
            String sessionCookie = signinForSessionCookie();
            UUID eventUuid = addEvent(memberId, now.plusDays(1), now.plusDays(2), now);

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events/{uuid}",
                    HttpMethod.DELETE,
                    createHttpEntity(null, sessionCookie),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @DisplayName("이벤트 삭제를 실패한다. (존재하지 않는 이벤트인 경우)")
        @Test
        void deleteEvent_fail_notFound() {
            // given
            signup();
            String sessionCookie = signinForSessionCookie();
            UUID eventUuid = UUID.randomUUID();

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events/{uuid}",
                    HttpMethod.DELETE,
                    createHttpEntity(null, sessionCookie),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @DisplayName("이벤트 삭제를 실패한다. (이벤트를 생성한 사용자가 아닌 경우)")
        @Test
        void deleteEvent_fail_notEventOwner() {
            // given
            long memberId = signup();
            UUID eventUuid = addEvent(memberId, now.plusDays(1), now.plusDays(2), now);
            signup("new@test.com", "test1234!@#$");
            String newSessionCookie = signinForSessionCookie("new@test.com", "test1234!@#$");

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events/{uuid}",
                    HttpMethod.DELETE,
                    createHttpEntity(null, newSessionCookie),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }

        @DisplayName("이벤트 삭제를 실패한다. (이벤트 참여자가 존재하는 경우)")
        @Test
        void deleteEvent_fail_existSuccessRecord() {
            // given
            long memberId = signup();
            String sessionCookie = signinForSessionCookie();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);
            addSuccessRecords(eventUuid, 1, false);

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events/{uuid}",
                    HttpMethod.DELETE,
                    createHttpEntity(null, sessionCookie),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class updateEvent {
        @DisplayName("이벤트 수정을 성공한다.")
        @Test
        void updateEvent_success() {
            // given
            long memberId = signup();
            String sessionCookie = signinForSessionCookie();
            UUID eventUuid = addEvent(memberId, now.plusDays(1), now.plusDays(2), now);
            String body = """
                    {
                        "title": "new title",
                        "limitCount": 20,
                        "successContent": "new content",
                        "successImageUrl": "https://cdn.ddip.me/new-success-image.png",
                        "thumbnailImageUrl": "https://cdn.ddip.me/new-thumbnail-image.png",
                        "startDateTime": "2023-11-30T11:10:00Z",
                        "endDateTime": "2023-11-30T12:10:00Z",
                        "successForm": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events/{uuid}",
                    HttpMethod.PUT,
                    createHttpEntity(body, sessionCookie),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @DisplayName("이벤트 수정을 실패한다. (존재하지 않는 이벤트인 경우)")
        @Test
        void updateEvent_fail_notFound() {
            // given
            signup();
            String sessionCookie = signinForSessionCookie();
            UUID eventUuid = UUID.randomUUID();
            String body = """
                    {
                        "title": "new title",
                        "limitCount": 20,
                        "successContent": "new content",
                        "successImageUrl": "https://cdn.ddip.me/new-success-image.png",
                        "thumbnailImageUrl": "https://cdn.ddip.me/new-thumbnail-image.png",
                        "startDateTime": "2023-11-30T11:10:00Z",
                        "endDateTime": "2023-11-30T12:10:00Z",
                        "successForm": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events/{uuid}",
                    HttpMethod.PUT,
                    createHttpEntity(body, sessionCookie),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @DisplayName("이벤트 수정을 실패한다. (이벤트를 생성한 사용자가 아닌 경우)")
        @Test
        void updateEvent_fail_notEventOwner() {
            // given
            long memberId = signup();
            UUID eventUuid = addEvent(memberId, now.plusDays(1), now.plusDays(2), now);
            signup("new@test.com", "test1234!@#$");
            String newSessionCookie = signinForSessionCookie("new@test.com", "test1234!@#$");
            String body = """
                    {
                        "title": "new title",
                        "limitCount": 20,
                        "successContent": "new content",
                        "successImageUrl": "https://cdn.ddip.me/new-success-image.png",
                        "thumbnailImageUrl": "https://cdn.ddip.me/new-thumbnail-image.png",
                        "startDateTime": "2023-11-30T11:10:00Z",
                        "endDateTime": "2023-11-30T12:10:00Z",
                        "successForm": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events/{uuid}",
                    HttpMethod.PUT,
                    createHttpEntity(body, newSessionCookie),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }

        @DisplayName("이벤트 수정을 실패한다. (이벤트 참여자가 존재하는 경우)")
        @Test
        void updateEvent_fail_existSuccessRecord() {
            // given
            long memberId = signup();
            String sessionCookie = signinForSessionCookie();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);
            addSuccessRecords(eventUuid, 1, false);
            String body = """
                    {
                        "title": "new title",
                        "limitCount": 20,
                        "successContent": "new content",
                        "successImageUrl": "https://cdn.ddip.me/new-success-image.png",
                        "thumbnailImageUrl": "https://cdn.ddip.me/new-thumbnail-image.png",
                        "startDateTime": "2023-11-30T11:10:00Z",
                        "endDateTime": "2023-11-30T12:10:00Z",
                        "successForm": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events/{uuid}",
                    HttpMethod.PUT,
                    createHttpEntity(body, sessionCookie),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @DisplayName("이벤트 수정을 실패한다. (이미 시작된 이벤트인 경우)")
        @Test
        void updateEvent_fail_alreadyStarted() {
            // given
            long memberId = signup();
            String sessionCookie = signinForSessionCookie();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);
            String body = """
                    {
                        "title": "new title",
                        "limitCount": 20,
                        "successContent": "new content",
                        "successImageUrl": "https://cdn.ddip.me/new-success-image.png",
                        "thumbnailImageUrl": "https://cdn.ddip.me/new-thumbnail-image.png",
                        "startDateTime": "2023-11-29T11:10:00Z",
                        "endDateTime": "2023-11-30T12:10:00Z",
                        "successForm": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            ResponseEntity<String> res = restTemplate.exchange("/events/{uuid}",
                    HttpMethod.PUT,
                    createHttpEntity(body, sessionCookie),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class findEventSuccessResult {
        @DisplayName("이벤트 성공 결과 조회를 성공한다. (token이 유효하고, 로그인하지 않은 경우)")
        @Test
        void findEventSuccessResult_success_validTokenAndNotLogin() {
            // given
            long memberId = signup();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);
            String token = addSuccessRecords(eventUuid, 1, true).get(0);

            // when
            String path = UriComponentsBuilder.fromPath("/events/{uuid}/success")
                    .queryParam("token", token)
                    .build().toString();
            ResponseEntity<String> res = restTemplate.exchange(path,
                    HttpMethod.GET,
                    createHttpEntity(null, null),
                    String.class,
                    eventUuid);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(json.read("$.data.successContent", String.class)).isEqualTo("content");
            assertThat(json.read("$.data.successImageUrl", String.class)).isEqualTo("https://cdn.ddip.me/success-image.png");
            assertThat(json.read("$.data", Map.class)).containsKey("successForm");
        }

        @DisplayName("이벤트 성공 결과 조회를 성공한다. (token이 유효하고, 로그인한 멤버가 이벤트 주최자가 아닌 경우)")
        @Test
        void findEventSuccessResult_success_validTokenAndNotEventOwner() {
            // given
            long memberId = signup();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);
            String token = addSuccessRecords(eventUuid, 1, true).get(0);
            signup("new@test.com", "test1234!@#$");
            String newSessionCookie = signinForSessionCookie("new@test.com", "test1234!@#$");

            // when
            String path = UriComponentsBuilder.fromPath("/events/{uuid}/success")
                    .queryParam("token", token)
                    .build().toString();
            ResponseEntity<String> res = restTemplate.exchange(path,
                    HttpMethod.GET,
                    createHttpEntity(null, newSessionCookie),
                    String.class,
                    eventUuid);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(json.read("$.data.successContent", String.class)).isEqualTo("content");
            assertThat(json.read("$.data.successImageUrl", String.class)).isEqualTo("https://cdn.ddip.me/success-image.png");
            assertThat(json.read("$.data", Map.class)).containsKey("successForm");
        }

        @DisplayName("이벤트 성공 결과 조회를 성공한다. (token이 없고, 로그인한 멤버가 이벤트 주최자인 경우)")
        @Test
        void findEventSuccessResult_success_notTokenAndEventOwner() {
            // given
            long memberId = signup();
            String sessionCookie = signinForSessionCookie();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);

            // when
            String path = UriComponentsBuilder.fromPath("/events/{uuid}/success")
                    .build().toString();
            ResponseEntity<String> res = restTemplate.exchange(path,
                    HttpMethod.GET,
                    createHttpEntity(null, sessionCookie),
                    String.class,
                    eventUuid);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(json.read("$.data.successContent", String.class)).isEqualTo("content");
            assertThat(json.read("$.data.successImageUrl", String.class)).isEqualTo("https://cdn.ddip.me/success-image.png");
            assertThat(json.read("$.data", Map.class)).containsKey("successForm");
        }

        @DisplayName("이벤트 성공 결과 조회를 실패한다. (token도 없고, 로그인도 하지 않은 경우)")
        @Test
        void findEventSuccessResult_fail_notTokenAndNotLogin() {
            // given
            long memberId = signup();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);

            // when
            String path = UriComponentsBuilder.fromPath("/events/{uuid}/success")
                    .build().toString();
            ResponseEntity<String> res = restTemplate.exchange(path,
                    HttpMethod.GET,
                    createHttpEntity(null, null),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }

        @DisplayName("이벤트 성공 결과 조회를 실패한다. (이벤트에 참여하지 않은 경우)")
        @Test
        void findEventSuccessResult_fail_notSuccessRecord() {
            // given
            long memberId = signup();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);
            String randomToken = UUID.randomUUID().toString();

            // when
            String path = UriComponentsBuilder.fromPath("/events/{uuid}/success")
                    .queryParam("token", randomToken)
                    .build().toString();
            ResponseEntity<String> res = restTemplate.exchange(path,
                    HttpMethod.GET,
                    createHttpEntity(null, null),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class findSuccessRecordFormInputValue {
        @DisplayName("유저가 입력한 폼 값 조회를 성공한다.")
        @Test
        void findSuccessRecordFormInputValue_success() {
            // given
            long memberId = signup();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);
            String token = addSuccessRecords(eventUuid, 1, true).get(0);

            // when
            String path = UriComponentsBuilder.fromPath("/events/{uuid}/form")
                    .queryParam("token", token)
                    .build().toString();
            ResponseEntity<String> res = restTemplate.exchange(path,
                    HttpMethod.GET,
                    createHttpEntity(null, null),
                    String.class,
                    eventUuid);
            DocumentContext json = JsonPath.parse(res.getBody());

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(json.read("$.data.formInputValue", JSONObject.class)).isNotNull();
        }

        @DisplayName("유저가 입력한 폼 값 조회를 실패한다. (이벤트에 참여하지 않은 경우)")
        @Test
        void findSuccessRecordFormInputValue_fail_notSuccessRecord() {
            // given
            long memberId = signup();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);
            addSuccessRecords(eventUuid, 1, true).get(0);
            String randomToken = UUID.randomUUID().toString();

            // when
            String path = UriComponentsBuilder.fromPath("/events/{uuid}/form")
                    .queryParam("token", randomToken)
                    .build().toString();
            ResponseEntity<String> res = restTemplate.exchange(path,
                    HttpMethod.GET,
                    createHttpEntity(null, null),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @DisplayName("유저가 입력한 폼 값 조회를 실패한다. (이벤트에 참여했지만, 폼을 입력하지 않은 경우)")
        @Test
        void findSuccessRecordFormInputValue_fail_notRegisteredFormInputValue() {
            // given
            long memberId = signup();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);
            String token = addSuccessRecords(eventUuid, 1, false).get(0);

            // when
            String path = UriComponentsBuilder.fromPath("/events/{uuid}/form")
                    .queryParam("token", token)
                    .build().toString();
            ResponseEntity<String> res = restTemplate.exchange(path,
                    HttpMethod.GET,
                    createHttpEntity(null, null),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class registerSuccessRecordFormInputValue {
        @DisplayName("유저가 입력한 폼 값 등록을 성공한다.")
        @Test
        void registerSuccessRecordFormInputValue_success() {
            // given
            long memberId = signup();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);
            String token = addSuccessRecords(eventUuid, 1, false).get(0);
            String body = """
                    {
                        "formInputValue": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            String path = UriComponentsBuilder.fromPath("/events/{uuid}/form")
                    .queryParam("token", token)
                    .build().toString();
            ResponseEntity<String> res = restTemplate.exchange(path,
                    HttpMethod.POST,
                    createHttpEntity(body, null),
                    String.class,
                    eventUuid);

            // then
            System.out.println(res.getBody());
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @DisplayName("유저가 입력한 폼 값 등록을 실패한다. (이벤트에 참여하지 않은 경우)")
        @Test
        void registerSuccessRecordFormInputValue_fail_notSuccessRecord() {
            // given
            long memberId = signup();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);
            addSuccessRecords(eventUuid, 1, false);
            String randomToken = UUID.randomUUID().toString();
            String body = """
                    {
                        "formInputValue": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            String path = UriComponentsBuilder.fromPath("/events/{uuid}/form")
                    .queryParam("token", randomToken)
                    .build().toString();
            ResponseEntity<String> res = restTemplate.exchange(path,
                    HttpMethod.POST,
                    createHttpEntity(body, null),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @DisplayName("유저가 입력한 폼 값 등록을 실패한다. (이미 폼을 입력한 경우)")
        @Test
        void registerSuccessRecordFormInputValue_fail_alreadyRegisteredFormInputValue() {
            // given
            long memberId = signup();
            UUID eventUuid = addEvent(memberId, now.minusDays(1), now.plusDays(1), now);
            String token = addSuccessRecords(eventUuid, 1, true).get(0);
            String body = """
                    {
                        "formInputValue": {
                            "test": "test"
                        }
                    }
                    """;

            // when
            String path = UriComponentsBuilder.fromPath("/events/{uuid}/form")
                    .queryParam("token", token)
                    .build().toString();
            ResponseEntity<String> res = restTemplate.exchange(path,
                    HttpMethod.POST,
                    createHttpEntity(body, null),
                    String.class,
                    eventUuid);

            // then
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }
}