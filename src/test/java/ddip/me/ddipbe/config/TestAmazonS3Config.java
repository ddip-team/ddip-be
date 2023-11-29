package ddip.me.ddipbe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Configuration
public class TestAmazonS3Config {

    @Bean
    @DependsOn("localStackTestContainer")
    public S3Client s3Client() {
        S3Client client = S3Client.builder()
                .endpointOverride(LocalStackTestContainer.LOCAL_STACK_CONTAINER.getEndpointOverride(S3))
                .region(Region.of(LocalStackTestContainer.LOCAL_STACK_CONTAINER.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        LocalStackTestContainer.LOCAL_STACK_CONTAINER.getAccessKey(),
                                        LocalStackTestContainer.LOCAL_STACK_CONTAINER.getSecretKey()
                                )
                        )
                )
                .build();
        ListBucketsResponse listBucketsResponse = client.listBuckets();
        // 버켓이 없으면 생성
        boolean bucketAlreadyExists = listBucketsResponse.buckets().stream().anyMatch(bucket -> bucket.name().equals(LocalStackTestContainer.BUCKET_NAME));
        if (!bucketAlreadyExists) {
            client.createBucket(builder -> builder.bucket(LocalStackTestContainer.BUCKET_NAME).build());
        }
        return client;
    }

    @Bean
    @DependsOn("localStackTestContainer")
    S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .endpointOverride(LocalStackTestContainer.LOCAL_STACK_CONTAINER.getEndpointOverride(S3))
                .region(Region.of(LocalStackTestContainer.LOCAL_STACK_CONTAINER.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        LocalStackTestContainer.LOCAL_STACK_CONTAINER.getAccessKey(),
                                        LocalStackTestContainer.LOCAL_STACK_CONTAINER.getSecretKey()
                                )
                        )
                )
                .build();
    }
}
