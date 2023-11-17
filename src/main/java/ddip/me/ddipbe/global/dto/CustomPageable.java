package ddip.me.ddipbe.global.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CustomPageable {
    public static Pageable of(int page, int size, Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }
}
