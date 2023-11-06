package ddip.me.ddipbe.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateEventReq {

    private String title;
    private Integer permitCount;
    private String content;
    private LocalDateTime start;
    private LocalDateTime end;
}
