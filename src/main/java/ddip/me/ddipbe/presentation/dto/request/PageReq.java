package ddip.me.ddipbe.presentation.dto.request;

import jakarta.validation.constraints.Positive;

public record PageReq(@Positive int page, @Positive int size) {
}
