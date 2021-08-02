package com.example.youtubedb;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableAdminServer
@EnableJpaAuditing
public class YoutubeDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(YoutubeDbApplication.class, args);
    }

}
