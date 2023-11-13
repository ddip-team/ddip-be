package ddip.me.ddipbe.application.model;

import lombok.Getter;

import java.util.List;

@Getter
public class Page<T> {

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

    public Page(PageInfo pageInfo, List<T> pageData) {
        this.pageInfo = pageInfo;
        this.pageData = pageData;
    }
}
