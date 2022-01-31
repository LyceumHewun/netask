package cc.lyceum.netask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Lyceum
 */
@EnableScheduling
@EnableConfigurationProperties
@SpringBootApplication
public class NetaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetaskApplication.class, args);
    }

}
