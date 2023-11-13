package ddip.me.ddipbe.application;

import ddip.me.ddipbe.application.model.PresignedUrl;
import ddip.me.ddipbe.application.model.UploadType;
import ddip.me.ddipbe.global.util.FileNameGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;

@Service
public class PresignedService {

    private static final String METADATA_KEY_ORIGINAL_NAME = "original-name";

    private final S3Presigner s3Presigner;
    private final String bucketName;
    private final String cdnUrl;

    public PresignedService(
            S3Presigner s3Presigner,
            @Value("${spring.cloud.aws.s3.bucket}") String bucketName,
            @Value("${spring.cloud.aws.cdn.url}") String cdnUrl
    ) {
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
        this.cdnUrl = cdnUrl;
    }

    public PresignedUrl getPresignedUrl(String fileName, UploadType type) {
        final String newFileName = FileNameGenerator.generateFileName(fileName);
        final String filePath = type.toFileKey(newFileName);

        var putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .metadata(Map.of(METADATA_KEY_ORIGINAL_NAME, fileName))
                .build();

        var preSignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(1))
                .putObjectRequest(putObjectRequest)
                .build();

        String presignedUrl = s3Presigner.presignPutObject(preSignRequest).url().toString();
        String fileUrl = cdnUrl + filePath;

        return new PresignedUrl(presignedUrl, fileUrl);
    }
}
