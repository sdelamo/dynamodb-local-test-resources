package dynamodbtemp;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import io.micronaut.context.exceptions.ConfigurationException;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Requires(property = "dynamodb.endpoint")
@Singleton
public class DynamoDbClientBuilderListener implements BeanCreatedEventListener<DynamoDbClientBuilder> {
    private static final Logger LOG = LoggerFactory.getLogger(DynamoDbClientBuilderListener.class);

    private final URI endpoint;
    private final String accessKeyId;
    private final String secretAccessKey;

    public DynamoDbClientBuilderListener(@Value("${dynamodb.endpoint}") String endpoint,
                                         @Value("${aws.access-key-id}") String accessKeyId,
                                         @Value("${aws.secret-access-key}") String secretAccessKey) {

        try {
            this.endpoint = new URI(endpoint);
        } catch (URISyntaxException e) {
            throw new ConfigurationException("dynamodb.endpoint not a valid URI");
        }
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
    }

    @Override
    public DynamoDbClientBuilder onCreated(BeanCreatedEvent<DynamoDbClientBuilder> event) {
        return event.getBean().endpointOverride(endpoint)
                .credentialsProvider(() -> new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return accessKeyId;
                    }

                    @Override
                    public String secretAccessKey() {
                        return secretAccessKey;
                    }
                });
    }
}