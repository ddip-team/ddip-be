package ddip.me.ddipbe.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class UrlUtilTest {

    @DisplayName("baseUrl과 path를 합쳐서 url을 만든다.")
    @ParameterizedTest
    @CsvSource(value = {
            "http://localhost:8080,/api",
            "http://localhost:8080/,/api",
            "http://localhost:8080,api",
            "http://localhost:8080/,api"})
    void joinUrl(String baseUrl, String path) {
        // when
        String url = UrlUtil.join(baseUrl, path);

        // then
        assertThat(url).isEqualTo("http://localhost:8080/api");
    }

    @DisplayName("마지막 파라미터의 trailing '/'를 제거하지 않는다.")
    @Test
    void joinLeavesLastPathTrailingSlash() {
        // given
        String baseUrl = "http://localhost:8080";
        String path = "/api/";

        // when
        String url = UrlUtil.join(baseUrl, path);

        // then
        assertThat(url).isEqualTo("http://localhost:8080/api/");
    }

    @DisplayName("첫 번째 파라미터의 leading '/'를 제거하지 않는다.")
    @Test
    void joinLeavesFirstPathLeadingSlash() {
        // given
        String base = "/data";
        String path = "key/path";

        // when
        String url = UrlUtil.join(base, path);

        // then
        assertThat(url).isEqualTo("/data/key/path");
    }
}