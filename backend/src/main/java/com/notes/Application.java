package com.notes;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EntityScan(basePackageClasses = {Application.class})
public class Application implements WebMvcConfigurer {

    public static void main(String[] args) {
        run(Application.class, args);
    }
}
