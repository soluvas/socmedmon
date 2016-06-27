package org.soluvas.socmedmon.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by ceefour on 28/06/2016.
 */
@Profile("socmedmonApp")
@Configuration
public class MyMvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/**")
                .addResourceLocations("file:mobile/www/"); // don't excessive cache, we change sometimes
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        super.addViewControllers(registry);
        // http://stackoverflow.com/a/27383522/122441
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
