package dynamodbtemp;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller
public class HomeController {

    private final DynamoRepository dynamoRepository;

    public HomeController(DynamoRepository dynamoRepository) {
        this.dynamoRepository = dynamoRepository;
    }

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    String index() {
        return "Hello world";
    }
}
