package org.soluvas.socmedmon.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by ceefour on 27/06/2016.
 */
@RepositoryRestResource(excerptProjection = SiteSummary.Inline.class)
public interface SiteSummaryRepository extends JpaRepository<SiteSummary, Long> {
}
