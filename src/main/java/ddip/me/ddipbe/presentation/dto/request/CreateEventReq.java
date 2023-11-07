package ddip.me.ddipbe.presentation.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateEventReq {

    private final String title;
    private final Integer permitCount;
    private final String content;
    private final LocalDateTime start;
    private final LocalDateTime end;

    public CreateEventReq(String title, Integer permitCount, String content, LocalDateTime start, LocalDateTime end) {
        this.title = title;
        this.permitCount = permitCount;
        this.content = content;
        this.start = start;
        this.end = end;
    }
}
