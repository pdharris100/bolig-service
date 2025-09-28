package dk.bolig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File; // NEW

@SpringBootApplication
@EnableCaching
public class Application extends SpringBootServletInitializer  {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        // Log classpath entries in a Java 9+ friendly way
        String cp = System.getProperty("java.class.path");
        LOG.debug("******** Classpath files start");
        for (String entry : cp.split(File.pathSeparator)) {
            LOG.debug(entry);
        }
        LOG.debug("******** Classpath files end");
        LOG.debug("******** Started bolig microservice");
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}