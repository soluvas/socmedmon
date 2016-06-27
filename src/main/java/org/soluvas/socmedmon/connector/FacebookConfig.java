package org.soluvas.socmedmon.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import facebook4j.FacebookFactory;
import facebook4j.conf.ConfigurationBuilder;
import facebook4j.conf.PropertyConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soluvas.socmed.FacebookApp;
import org.soluvas.socmed.FacebookAuthorization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by ceefour on 29/10/2015.
 */
@Profile("socmedmonApp")
@Configuration
public class FacebookConfig {

    private static final Logger log = LoggerFactory.getLogger(FacebookConfig.class);
    public static final String APP_ID = "lumen";
    public static final String AGENT_ID = "arkanlumen";

    @Inject
    private Environment env;
    @Inject
    private ObjectMapper mapper;

    @Bean
    public FacebookApp facebookApp() throws IOException {
        return mapper.readValue(new File("config/" + APP_ID + ".FacebookApp.jsonld"), FacebookApp.class);
    }

    @Bean
    public FacebookAuthorization facebookAuthorization() throws IOException {
        return mapper.readValue(new File("config/agent/" + APP_ID + "-" + AGENT_ID + ".FacebookAuthorization.jsonld"), FacebookAuthorization.class);
    }

    @Bean
    public FacebookFactory facebookFactory() throws IOException {
        final facebook4j.conf.Configuration configuration = new ConfigurationBuilder()
                .setRestBaseURL("https://graph.facebook.com/v2.6/") // we need fan_count
                .setOAuthAppId(facebookApp().getApiKey())
                .setOAuthAppSecret(facebookApp().getApiSecret())
                .build();
        return new FacebookFactory(configuration);
    }

}
