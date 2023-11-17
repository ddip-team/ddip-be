package ddip.me.ddipbe.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FileNameGeneratorTest {

    @DisplayName("확장자가 있는 파일명을 입력받으면 UUID와 확장자를 합쳐서 파일명을 생성한다.")
    @Test
    void generateFileNameWithExtension() {
        // given
        String originalFileName = "test.jpg";

        // when
        String generatedFileName = FileNameGenerator.generateFileName(originalFileName);

        // then
        assertThat(generatedFileName).matches("^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}\\.jpg$");
    }

    @DisplayName("확장자가 없는 파일명을 입력받으면 UUID만으로 파일명을 생성한다.")
    @Test
    void generateFileNameWithoutExtension() {
        // given
        String originalFileName = "test";

        // when
        String generatedFileName = FileNameGenerator.generateFileName(originalFileName);

        // then
        assertThat(generatedFileName).matches("^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$");
    }
}