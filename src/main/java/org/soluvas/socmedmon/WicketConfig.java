package org.soluvas.socmedmon;

import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.spring.SpringWebApplicationFactory;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

//@Configuration
@Profile({"socmedmonApp"})
public class WicketConfig {

    @Inject
    protected Environment env;

    @Bean
    public FilterRegistrationBean wicketFilter() {
        final FilterRegistrationBean reg = new FilterRegistrationBean(new WicketFilter());
        reg.addInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
        reg.addInitParameter(WicketFilter.APP_FACT_PARAM, SpringWebApplicationFactory.class.getName());
        reg.addInitParameter("applicationBean", "webApp");
        reg.addInitParameter("configuration", env.getRequiredProperty("wicket.configuration"));
        //        reg.addInitParameter("applicationClassName", MyWebApplication.class.name);
        return reg;
    }
}
