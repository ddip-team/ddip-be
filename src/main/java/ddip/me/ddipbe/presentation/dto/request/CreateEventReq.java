package ddip.me.ddipbe.presentation.dto.request;

import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.Map;

@Getter
public class CreateEventReq {

    private final String title;
    private final Integer limitCount;
    private final String successContent;
    private final ZonedDateTime startDateTime;
    private final ZonedDateTime endDateTime;
    private Map<String, String> jsonString;
    private String imgUrl;

    public CreateEventReq(String title, Integer limitCount, String successContent, ZonedDateTime startDateTime, ZonedDateTime endDateTime,Map<String, String> jsonString, String imgUrl) {
        this.title = title;
        this.limitCount = limitCount;
        this.successContent = successContent;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.jsonString = jsonString;
        this.imgUrl = imgUrl;
    }
}
