package ddip.me.ddipbe.presentation.dto.request;

import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.Map;

@Getter
public class CreateEventReq {

    private final String title;
    private final Integer limitCount;
    private final String successContent;
    private final String successImageUrl;
    private final String thumbnailImageUrl;
    private final ZonedDateTime startDateTime;
    private final ZonedDateTime endDateTime;
    private final Map<String, Object> successForm;

    public CreateEventReq(
            String title,
            Integer limitCount,
            String successContent,
            String successImageUrl,
            ZonedDateTime startDateTime,
            ZonedDateTime endDateTime,
            Map<String, Object> successForm
    ) {
        this.title = title;
        this.limitCount = limitCount;
        this.successContent = successContent;
        this.successImageUrl = successImageUrl;
        this.thumbnailImageUrl = successImageUrl;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.successForm = successForm;
    }
}
