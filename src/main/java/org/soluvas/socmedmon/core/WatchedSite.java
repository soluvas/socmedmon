package org.soluvas.socmedmon.core;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.io.Serializable;

/**
 * An online asset that can be monitored.
 * Created by ceefour on 26/06/2016.
 */
@Entity
public class WatchedSite implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ExternalSite kind;
    private String siteId;
    private String siteScreenName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExternalSite getKind() {
        return kind;
    }

    public void setKind(ExternalSite kind) {
        this.kind = kind;
    }

    /**
     * Canonical, usually 64-bit long integer, of the site to be watched. For {@link ExternalSite#FACEBOOK},
     * this is the Facebook Page ID or User ID.
     * One of {@link #getSiteId()} and {@link #getSiteScreenName()} is required.
     * @return
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * Screen name of the site to be watched. For {@link ExternalSite#FACEBOOK},
     * this is the Facebook Page username. For {@link ExternalSite#TWITTER}, this is the Twitter screen name.
     * One of {@link #getSiteId()} and {@link #getSiteScreenName()} is required.
     * @return
     */
    public String getSiteScreenName() {
        return siteScreenName;
    }

    public void setSiteScreenName(String siteScreenName) {
        this.siteScreenName = siteScreenName;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("kind", kind)
                .add("siteId", siteId)
                .add("siteScreenName", siteScreenName)
                .toString();
    }
}
