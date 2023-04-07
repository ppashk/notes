package com.notes.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;

@Import({
        ClientExceptionHandler.class,
})
@ConditionalOnClass({WebMvcConfigurer.class, HttpServletResponse.class})
@Configuration
public class WebConfigurationsImporter {
}
