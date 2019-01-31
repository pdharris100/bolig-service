package dk.bolig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dk.bolig.controller.EstimateController;

@SpringBootApplication
public class BoligServer {
	private static final Logger LOG = LoggerFactory.getLogger(EstimateController.class);
    public static void main(String[] args) {
        SpringApplication.run(BoligServer.class, args);
        LOG.debug("******** Started");
    }
}
