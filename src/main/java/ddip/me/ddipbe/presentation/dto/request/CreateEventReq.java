package ddip.me.ddipbe.presentation.dto.request;

import ddip.me.ddipbe.global.annotation.MinutePreciseUTC;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.URL;

import java.time.ZonedDateTime;
import java.util.Map;

public record CreateEventReq(
        @NotBlank String title,
        @Positive Integer limitCount,
        String successContent,
        @URL String successImageUrl,
        @URL String thumbnailImageUrl,
        @NotNull @MinutePreciseUTC ZonedDateTime startDateTime,
        @NotNull @MinutePreciseUTC ZonedDateTime endDateTime,
        Map<String, Object> successForm
) {
}
