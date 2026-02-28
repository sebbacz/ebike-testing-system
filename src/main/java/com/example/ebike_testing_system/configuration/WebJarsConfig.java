package com.example.ebike_testing_system.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Bypass ClassGraph/WebJarAssetLocator and serve WebJar resources directly
 * from classpath:/META-INF/resources/webjars/.
 */
@Configuration
//@EnableWebMvc
public class WebJarsConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1) Serve all /webjars/** requests from inside the fat JAR under META-INF/resources/webjars/
        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                // We disable the “resourceChain” (versioning, resolver chain) because default
                // WebJarsResourceResolver triggers ClassGraph under the hood. We want to avoid that.
                .resourceChain(true);

        // 2) If you have other static assets under /static/ (e.g. your own CSS/JS),
        //    keep this (or similar) handler so Spring still serves them.
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry
                .addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry
                .addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry
                .addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");


    }
}