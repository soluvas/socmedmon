package org.soluvas.socmedmon.core;

import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by ceefour on 26/06/2016.
 */
@Component
public class SiteStatIdConverter implements BackendIdConverter {

    @Override
    public Serializable fromRequestId(String id, Class<?> entityType) {
        return new SiteStatId(id);
    }

    @Override
    public String toRequestId(Serializable id, Class<?> entityType) {
        return id.toString();
    }

    @Override
    public boolean supports(Class<?> delimiter) {
        return SiteStat.class.equals(delimiter) || SiteStatId.class.equals(delimiter);
    }
}
