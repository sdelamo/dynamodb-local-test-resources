package example

import io.micronaut.context.annotation.Value
import io.micronaut.testresources.core.TestResourcesResolver

class DynamoDbLocalTestResolver implements TestResourcesResolver, AutoCloseable {

    private boolean running = false
    Process proc

    @Override
    List<String> getResolvableProperties(Map<String, Collection<String>> propertyEntries, Map<String, Object> testResourcesConfig) {
        [
                'dynamodb.endpoint',
                'aws.access-key-id',
                'aws.secret-access-key'
        ]
    }

    @Override
    Optional<String> resolve(String propertyName, Map<String, Object> properties, Map<String, Object> testResourcesConfiguration) {
        run()
        if (propertyName == 'dynamodb.endpoint') {
            Optional.of('http://localhost:8000')
        } else if (propertyName == 'aws.access-key-id') {
            return Optional.of("fakeMyKeyId")
        } else if (propertyName == 'aws.secret-access-key') {
            return Optional.of("fakeSecretAccessKey")
        }
    }

    void run() {
        if (!running) {
            String cmd = "java -Djava.library.path=/Users/sdelamo/Applications/dynamodb_local_latest/DynamoDBLocal_lib -jar /Users/sdelamo/Applications/dynamodb_local_latest/DynamoDBLocal.jar -sharedDb"
            StringBuilder sout = new StringBuilder()
            StringBuilder serr = new StringBuilder()
            this.proc = cmd.execute()
            proc.consumeProcessOutput(sout, serr)
            println "out> $sout\nerr> $serr"
            Runtime.getRuntime().addShutdownHook({ ->
               close();
            });
            running = true
        }
    }

    @Override
    void close() throws Exception {
        System.out.println("calling close");
        if (proc != null) {
            proc.destroy()
        }

    }
}
