package io.hawt;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="io.hawt")
public class GithubSpringBootApplication {

    /**
     * A main method to start this application.
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        String token = System.getProperty("github.token","");
        if (token.length() == 0) {
          System.out.println("Error: No github.token property was specified.");
          System.exit(1);
        }

        SpringApplication.run(GithubSpringBootApplication.class, args);
    }

    @Bean
    CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {

            @Override
            public void beforeApplicationStart(CamelContext context) {
                Utils.enableStatsAndInflightBrowse(context);
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {                
            }
        };
    }
}
