package ddip.me.ddipbe.presentation.dto.request;

import lombok.Getter;

@Getter
public class PageReq {

    private final int page;
    private final int size;

    public PageReq(int page, int size) {
        this.page = page;
        this.size = size;
    }
}
