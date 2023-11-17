package ddip.me.ddipbe.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonConverterTest {

    private final JsonConverter jsonConverter = new JsonConverter();

    @DisplayName("Map 객체를 JSON 문자열로 변환한다.")
    @Test
    void convertToDatabaseColumn() {
        // given
        Map<String, Object> map = new HashMap<>();
        map.put("name", "John");
        map.put("age", 30);

        // when
        String jsonString = jsonConverter.convertToDatabaseColumn(map);

        // then
        assertThat(jsonString).isEqualTo("{\"name\":\"John\",\"age\":30}");
    }

    @DisplayName("JSON 문자열을 Map 객체로 변환한다.")
    @Test
    void convertToEntityAttribute() {
        // given
        String jsonString = "{\"name\":\"John\",\"age\":30}";

        // when
        Map<String, Object> map = jsonConverter.convertToEntityAttribute(jsonString);

        // then
        assertThat(map).containsEntry("name", "John").containsEntry("age", 30);
    }

    @DisplayName("null 값을 입력받으면 null을 반환한다.")
    @Test
    void convertToEntityAttribute_withNull() {
        // when
        Map<String, Object> map = jsonConverter.convertToEntityAttribute(null);

        // then
        assertThat(map).isNull();
    }
}