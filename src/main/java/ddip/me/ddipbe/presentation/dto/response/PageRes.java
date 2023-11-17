package ddip.me.ddipbe.presentation.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageRes<T>(PageInfo pageInfo, List<T> pageData) {

    public PageRes(Page<T> page) {
        this(new PageInfo(
                        page.getNumber() + 1,
                        page.getSize(),
                        page.getTotalPages(),
                        page.getTotalElements()
                ),
                page.getContent()
        );
    }

    public record PageInfo(int page, int size, int totalPage, long totalSize) {
    }
}
