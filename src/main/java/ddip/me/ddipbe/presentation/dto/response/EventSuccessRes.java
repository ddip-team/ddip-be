package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.Event;

import java.util.Map;

public record EventSuccessRes(String successContent, String successImageUrl, Map<String, Object> successForm) {
    public EventSuccessRes(Event event) {
        this(event.getSuccessResult().getSuccessContent(),
                event.getSuccessResult().getSuccessImageUrl(),
                event.getSuccessResult().getSuccessForm());
    }
}
