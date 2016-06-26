package org.soluvas.socmedmon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Aplikasi utama untuk menjalankan helpdesk.
 */
@SpringBootApplication(exclude = {GroovyTemplateAutoConfiguration.class})
@Profile({"socmedmonApp"})
@EnableScheduling // this isn't automatic by Spring Boot, see https://spring.io/guides/gs/scheduling-tasks/
//@Import(LumenCoreConfig.class)
class SocmedmonApp implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SocmedmonApp.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(SocmedmonApp.class)
                .profiles("socmedmonApp"/*, "rabbitmq", "drools"*/)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Joining thread, you can press Ctrl+C to shutdown application");
        Thread.currentThread().join();
    }
}
