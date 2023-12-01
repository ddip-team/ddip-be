package ddip.me.ddipbe.presentation;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import ddip.me.ddipbe.config.LocalStackTestContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PresignedControllerTest extends IntegrationTest {

    static byte[] image = createSmallRedPng();

    // 10x10 크기의 빨간색 png 이미지 생성
    private static byte[] createSmallRedPng() {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                image.setRGB(x, y, 0xFFFF0000);
            }
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    @DisplayName("이벤트 썸네일용 Presigned URL을 가져온다.")
    @Test
    void getPresignedUrlForThumbnail() {
        // given
        String fileName = "test.png";
        String type = "EVENT_THUMBNAIL";
        URI uri = UriComponentsBuilder.fromPath("/presigned-url")
                .queryParam("fileNames", fileName)
                .queryParam("type", type)
                .build().toUri();

        // when
        ResponseEntity<String> res = restTemplate.exchange(uri,
                HttpMethod.GET,
                null,
                String.class);
        DocumentContext json = JsonPath.parse(res.getBody());

        // then
        assertThat(res.getStatusCode().value()).isEqualTo(200);
        assertThat(json.read("$.data", List.class)).isNotNull().hasSize(1);
        assertPresignedUrl(json.read("$.data[0].presignedUrl", String.class), fileName);
    }

    @DisplayName("이벤트 성공 이미지용 Presigned URL을 가져온다.")
    @Test
    void getPresignedUrlForSuccessImage() {
        // given
        String fileName = "test.png";
        String type = "EVENT_SUCCESS_IMAGE";
        URI uri = UriComponentsBuilder.fromPath("/presigned-url")
                .queryParam("fileNames", fileName)
                .queryParam("type", type)
                .build().toUri();

        // when
        ResponseEntity<String> res = restTemplate.exchange(uri,
                HttpMethod.GET,
                null,
                String.class);
        DocumentContext json = JsonPath.parse(res.getBody());

        // then
        assertThat(res.getStatusCode().value()).isEqualTo(200);
        assertThat(json.read("$.data", List.class)).isNotNull().hasSize(1);
        assertPresignedUrl(json.read("$.data[0].presignedUrl", String.class), fileName);
    }

    @DisplayName("Presigned URL을 한번에 여러개 가져온다.")
    @Test
    void getMultiplePresignedUrls() {
        // given
        String fileName1 = "test1.png";
        String fileName2 = "test2.png";
        String type = "EVENT_THUMBNAIL";
        URI uri = UriComponentsBuilder.fromPath("/presigned-url")
                .queryParam("fileNames", fileName1, fileName2)
                .queryParam("type", type)
                .build().toUri();

        // when
        ResponseEntity<String> res = restTemplate.exchange(uri,
                HttpMethod.GET,
                null,
                String.class);
        DocumentContext json = JsonPath.parse(res.getBody());

        // then
        assertThat(res.getStatusCode().value()).isEqualTo(200);
        assertThat(json.read("$.data", List.class)).isNotNull().hasSize(2);
        assertPresignedUrl(json.read("$.data[0].presignedUrl", String.class), fileName1);
        assertPresignedUrl(json.read("$.data[1].presignedUrl", String.class), fileName2);
    }

    private void assertPresignedUrl(String url, String fileName) {
        assertThat(url).contains(LocalStackTestContainer.BUCKET_NAME);
        assertThat(url).contains("x-amz-meta-original-name");

        // given
        String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.set("x-amz-meta-original-name", fileName);
        HttpEntity<?> httpEntity = new HttpEntity<>(image, headers);

        // when
        ResponseEntity<String> res = restTemplate.exchange(decodedUrl,
                HttpMethod.PUT,
                httpEntity,
                String.class);

        // then
        assertThat(res.getStatusCode().value())
                .withFailMessage("Failed Response: " + res.getBody())
                .isEqualTo(200);
    }
}