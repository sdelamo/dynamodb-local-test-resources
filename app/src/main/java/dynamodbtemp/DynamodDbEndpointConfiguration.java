package dynamodbtemp;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;

@Requires(env = Environment.DEVELOPMENT)
@ConfigurationProperties("dynamodb-override")
public class DynamodDbEndpointConfiguration {
    private String endpoint;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
