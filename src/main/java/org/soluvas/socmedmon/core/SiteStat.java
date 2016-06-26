package org.soluvas.socmedmon.core;

import com.google.common.base.MoreObjects;
import org.joda.time.DateTime;
import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ceefour on 26/06/2016.
 */
@Entity
public class SiteStat implements Serializable {

    //    @Component
//    public static class PKConverter implements org.springframework.core.convert.converter.Converter<String, PK> {
//
//        @Override
//        public PK convert(String source) {
//            final String[] split = source.split("_");
//            return new PK(Long.valueOf(split[0]), new DateTime(split[1]));
//        }
//    }

    @EmbeddedId
    private SiteStatId id;
//    @Id
//    @Column(name = "watchedsite_id")
//    private Long watchedSite_id;
//    @Id @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
//    @Column(name = "creationtime")
//    private DateTime creationTime;
    @ManyToOne
    @JoinColumn(name = "watchedsite_id", insertable = false, updatable = false)
    private WatchedSite watchedSite;

    private int followerCount;
    private int followedByCount;
    private int postLikedByCount;
    private int postCount;

    public SiteStatId getId() {
        return id;
    }

    public void setId(SiteStatId id) {
        this.id = id;
    }

    public Long getWatchedSite_id() {
        return id.watchedSiteId;
    }

    public DateTime getCreationTime() {
        return id.creationTime;
    }

    public WatchedSite getWatchedSite() {
        return watchedSite;
    }

    /**
     * Number of followers of this site/social media account.
     * @return
     */
    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    /**
     * Number of accounts followed by this site/social media account.
     * @return
     */
    public int getFollowedByCount() {
        return followedByCount;
    }

    public void setFollowedByCount(int followedByCount) {
        this.followedByCount = followedByCount;
    }

    /**
     * Number of posts liked by this site/social media account.
     * @return
     */
    public int getPostLikedByCount() {
        return postLikedByCount;
    }

    public void setPostLikedByCount(int postLikedByCount) {
        this.postLikedByCount = postLikedByCount;
    }

    /**
     * Number of posts by this site/social media account.
     * @return
     */
    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("watchedSite_id", id.watchedSiteId)
                .add("creationTime", id.creationTime)
                .add("followerCount", followerCount)
                .add("followedByCount", followedByCount)
                .add("postLikedByCount", postLikedByCount)
                .add("postCount", postCount)
                .toString();
    }
}
