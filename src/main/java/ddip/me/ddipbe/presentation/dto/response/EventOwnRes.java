package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.Event;
import lombok.Getter;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Getter
public class EventOwnRes {
    private String title;
    private Integer permitCount;
    private String contents;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean isOpen;

    public EventOwnRes(Event event) {
        this.title = event.getTitle();
        this.permitCount = event.getPermitCount();
        this.contents = event.getContent();
        this.start = event.getStart();
        this.end = event.getEnd();
        this.isOpen = event.getStart().isBefore(now()) && event.getEnd().isAfter(now());
    }
}
