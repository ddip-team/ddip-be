package ddip.me.ddipbe.application;

public enum UploadType {
    EVENT_THUMBNAIL("eventThumbnails"),
    EVENT_SUCCESS_IMAGE("eventSuccessImages"),
    ;

    private final String keyPrefix;

    UploadType(String eventSuccessImages) {
        this.keyPrefix = eventSuccessImages;
    }

    String toFileKey(String fileName) {
        return "data/" + this.keyPrefix + "/" + fileName;
    }
}
