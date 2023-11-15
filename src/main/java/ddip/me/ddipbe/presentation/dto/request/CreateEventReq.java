package ddip.me.ddipbe.presentation.dto.request;

import ddip.me.ddipbe.global.annotation.MinutePreciseUTC;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.ZonedDateTime;
import java.util.Map;

public record CreateEventReq(
        @NotBlank String title,
        @Positive Integer limitCount,
        String successContent,
        String successImageUrl,
        String thumbnailImageUrl,
        @MinutePreciseUTC ZonedDateTime startDateTime,
        @MinutePreciseUTC ZonedDateTime endDateTime,
        Map<String, Object> successForm
) {
}
