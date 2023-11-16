package ddip.me.ddipbe.application.model;

import ddip.me.ddipbe.global.util.UrlUtil;

public enum UploadType {
    EVENT_THUMBNAIL("eventThumbnails"),
    EVENT_SUCCESS_IMAGE("eventSuccessImages"),
    ;

    private final String keyPrefix;

    UploadType(String eventSuccessImages) {
        this.keyPrefix = eventSuccessImages;
    }

    public String toFileKey(String fileName) {
        return UrlUtil.join("data", this.keyPrefix, fileName);
    }
}
