package org.soluvas.socmedmon.core;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by ceefour on 26/06/2016.
 */
@Embeddable
public class SiteStatId implements Serializable {
    @Column(name = "watchedsite_id", nullable = false)
    public Long watchedSiteId;
    @Column(name = "creationtime", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime creationTime;

    public SiteStatId() {
    }

    public SiteStatId(String id) {
        final String[] split = id.split("_");
        this.watchedSiteId = Long.valueOf(split[0]);
        this.creationTime = new DateTime(split[1]);
    }

    public SiteStatId(Long watchedSiteId, DateTime creationTime) {
        this.watchedSiteId = watchedSiteId;
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return watchedSiteId + "_" + creationTime;
    }

    public Long getWatchedSiteId() {
        return watchedSiteId;
    }

    public void setWatchedSiteId(Long watchedSiteId) {
        this.watchedSiteId = watchedSiteId;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }
}
