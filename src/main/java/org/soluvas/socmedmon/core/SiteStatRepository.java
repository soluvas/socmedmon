package org.soluvas.socmedmon.core;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by ceefour on 26/06/2016.
 */
@RepositoryRestResource(excerptProjection = SiteStat.Inline.class)
public interface SiteStatRepository extends JpaRepository<SiteStat, Long> {

    /**
     * Usually, you sort by creationTime DESC and get 1 (latest before given pivot {@link DateTime}), e.g.
     *
     * <pre>{@code
     * final Optional<SiteStat> dayPrev = siteStatRepo.findAllBySiteBefore(watchedSite.getId(), pointTime.minusDays(1), new PageRequest(0, 1, Sort.Direction.DESC, "creationTime")).getContent().stream().findFirst();
     * final Optional<SiteStat> weekPrev = siteStatRepo.findAllBySiteBefore(watchedSite.getId(), pointTime.minusWeeks(1), new PageRequest(0, 1, Sort.Direction.DESC, "creationTime")).getContent().stream().findFirst();
     * final Optional<SiteStat> monthPrev = siteStatRepo.findAllBySiteBefore(watchedSite.getId(), pointTime.minusMonths(1), new PageRequest(0, 1, Sort.Direction.DESC, "creationTime")).getContent().stream().findFirst();
     * }</pre>
     *
     * @param watchedSiteId
     * @param atOrBefore
     * @param pageable
     * @return
     */
    @Query("SELECT e FROM SiteStat e WHERE e.watchedSiteId = :watchedSiteId AND e.creationTime <= :atOrBefore")
    Page<SiteStat> findAllBySiteBefore(@Param("watchedSiteId") long watchedSiteId,
                                       @Param("atOrBefore") DateTime atOrBefore, Pageable pageable);

}
