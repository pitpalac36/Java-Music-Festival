package festival.rest.start;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan("festival")
public class StartServices {
    public static void main(String[] args) {
        System.out.println("hey");
        SpringApplication.run(StartServices.class, args);
    }
}
