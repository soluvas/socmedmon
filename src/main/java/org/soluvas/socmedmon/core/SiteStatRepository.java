package org.soluvas.socmedmon.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by ceefour on 26/06/2016.
 */
@RepositoryRestResource(excerptProjection = SiteStat.Inline.class)
public interface SiteStatRepository extends JpaRepository<SiteStat, Long> {
}
