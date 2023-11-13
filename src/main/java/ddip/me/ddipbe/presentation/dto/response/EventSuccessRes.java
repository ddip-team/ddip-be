package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.Event;

import java.util.Map;

public class EventSuccessRes {
    private String successContent;
    private String successImgUrl;
    private Map<String, String> jsonString;

    public EventSuccessRes(Event event) {
        this.successContent = event.getSuccessContent();
        this.successImgUrl = event.getSuccessImgUrl();
        this.jsonString = event.getJsonString();
    }
}
