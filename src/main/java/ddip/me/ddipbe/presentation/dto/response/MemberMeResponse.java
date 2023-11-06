package ddip.me.ddipbe.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberMeResponse {

    private long id;
    private String email;
}