package com.example.youtubedb;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableAdminServer
@EnableJpaAuditing
public class YoutubeDbApplication {
    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yaml,"
            + "classpath:aws.yaml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(YoutubeDbApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}
