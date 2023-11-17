package ddip.me.ddipbe.domain;

import ddip.me.ddipbe.global.util.JsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SuccessResult {

    private String successContent;

    private String successImageUrl;

    @Column(columnDefinition = "json")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> successForm;

    public SuccessResult(String successContent, String successImageUrl, Map<String, Object> successForm) {
        this.successContent = successContent;
        this.successImageUrl = successImageUrl;
        this.successForm = successForm;
    }
}
