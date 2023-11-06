package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.Event;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EventRes {
    private String title;
    private Integer permitCount;
    private String content;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long memberId;

    public EventRes(Event event) {
        this.title = event.getTitle();
        this.permitCount = event.getPermitCount();
        this.content = event.getContent();
        this.start = event.getStart();
        this.end = event.getEnd();
        this.memberId = event.getMember().getId();
    }
}
