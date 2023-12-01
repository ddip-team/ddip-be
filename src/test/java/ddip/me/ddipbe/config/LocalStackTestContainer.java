package ddip.me.ddipbe.config;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Component
public class LocalStackTestContainer {

    private static final DockerImageName IMAGE_NAME = DockerImageName.parse("localstack/localstack");

    public static final String BUCKET_NAME = "ddip-bucket";

    public static final LocalStackContainer LOCAL_STACK_CONTAINER = new LocalStackContainer(IMAGE_NAME)
            .withEnv("DEFAULT_REGION", "ap-northeast-2")
            .withServices(S3);

    static {
        LOCAL_STACK_CONTAINER.start();
    }

    @PreDestroy
    public void stop() {
        LOCAL_STACK_CONTAINER.stop();
    }
}
