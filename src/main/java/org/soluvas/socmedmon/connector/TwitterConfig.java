package org.soluvas.socmedmon.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soluvas.socmed.TwitterApp;
import org.soluvas.socmed.TwitterAuthorization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.PropertyConfiguration;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by ceefour on 29/10/2015.
 */
@Profile("socmedmonApp")
@Configuration
public class TwitterConfig {

    private static final Logger log = LoggerFactory.getLogger(TwitterConfig.class);
    public static final String APP_ID = "lumen";
    public static final String AGENT_ID = "lumenrobot";

    @Inject
    private Environment env;
    @Inject
    private ObjectMapper mapper;

    @Bean
    public TwitterApp twitterApp() throws IOException {
        return mapper.readValue(new File("config/" + APP_ID + ".TwitterApp.jsonld"), TwitterApp.class);
    }

    @Bean
    public TwitterAuthorization twitterAuthorization() throws IOException {
        return mapper.readValue(new File("config/agent/" + APP_ID + "-" + AGENT_ID + ".TwitterAuthorization.jsonld"), TwitterAuthorization.class);
    }

    @Bean
    public TwitterFactory twitterFactory() throws IOException {
        final twitter4j.conf.Configuration configuration = new ConfigurationBuilder()
                .setOAuthConsumerKey(twitterApp().getApiKey())
                .setOAuthConsumerSecret(twitterApp().getApiSecret())
                .build();
        return new TwitterFactory(configuration);
    }

}
