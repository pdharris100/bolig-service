package dk.bolig;

import java.net.URL;
import java.net.URLClassLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

import dk.bolig.controller.EstimateController;

@SpringBootApplication
@EnableCaching
public class Application extends SpringBootServletInitializer  {

	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {    
        SpringApplication.run(Application.class, args);
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader)cl).getURLs();
        LOG.debug("******** Classpath files start");
        for(URL url: urls){
        	LOG.debug(url.getFile());
        }
        LOG.debug("******** Classpath files end");
        LOG.debug("******** Started bolig microservice");
    }
}
