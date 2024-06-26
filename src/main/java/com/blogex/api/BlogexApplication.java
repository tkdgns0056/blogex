package com.blogex.api;

import com.blogex.api.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class BlogexApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogexApplication.class, args);
    }

}
