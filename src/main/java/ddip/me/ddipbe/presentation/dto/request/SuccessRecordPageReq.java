package ddip.me.ddipbe.presentation.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessRecordPageReq {
    private int pageIndex;
    private int pageSize;
    private String sortProperty;
}
