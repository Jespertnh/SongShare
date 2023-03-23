package nl.jesper.songshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class }) // Voor debuggen met postman anders zit security in de weg.
@SpringBootApplication
public class SongShareApplication {

    public static void main(String[] args) {
        SpringApplication.run(SongShareApplication.class, args);
    }

}
