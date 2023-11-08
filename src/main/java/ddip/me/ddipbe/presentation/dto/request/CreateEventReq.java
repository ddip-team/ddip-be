package ddip.me.ddipbe.presentation.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateEventReq {

    private final String title;
    private final Integer limitCount;
    private final String successContent;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public CreateEventReq(String title, Integer limitCount, String successContent, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.title = title;
        this.limitCount = limitCount;
        this.successContent = successContent;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}
