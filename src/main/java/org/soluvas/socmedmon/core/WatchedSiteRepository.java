package org.soluvas.socmedmon.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by ceefour on 26/06/2016.
 */
public interface WatchedSiteRepository extends JpaRepository<WatchedSite, Long> {

    Page<WatchedSite> findAllByKind(@Param("kind") ExternalSite kind, Pageable pageable);
}
