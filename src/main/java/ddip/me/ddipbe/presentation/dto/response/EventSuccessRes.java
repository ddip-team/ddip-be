package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.Event;
import lombok.Getter;

import java.util.Map;

@Getter
public class EventSuccessRes {
    private final String successContent;
    private final String successImageUrl;
    private final Map<String, Object> successForm;

    public EventSuccessRes(Event event) {
        this.successContent = event.getSuccessContent();
        this.successImageUrl = event.getSuccessImageUrl();
        this.successForm = event.getSuccessForm();
    }
}
