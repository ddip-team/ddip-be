package ddip.me.ddipbe.presentation.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageRes<T> {

    @Getter
    public static class PageInfo {
        private final int page;
        private final int size;
        private final int totalPage;
        private final long totalSize;

        public PageInfo(int page, int size, int totalPage, long totalSize) {
            this.page = page;
            this.size = size;
            this.totalPage = totalPage;
            this.totalSize = totalSize;
        }
    }

    private final PageInfo pageInfo;
    private final List<T> pageData;

    public PageRes(Page<T> page) {
        this.pageInfo = new PageInfo(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
        this.pageData = page.getContent();
    }
}
