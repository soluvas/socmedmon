package org.soluvas.socmedmon.core;

import com.google.common.base.MoreObjects;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.rest.core.config.Projection;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ceefour on 26/06/2016.
 */
@Entity
public class SiteStat implements Serializable {

    /**
     * Created by ceefour on 27/06/2016.
     */
    @Projection(name = "inline", types = SiteStat.class)
    public interface Inline {
        Long getId();

        Long getWatchedSiteId();

        public DateTime getCreationTime();

        public WatchedSite getWatchedSite();

        public Integer getFollowerCount();
        public Integer getFollowedByCount();
        public Integer getPostLikedByCount();
        public Integer getPostCount();
    }

    //    @Component
//    public static class PKConverter implements org.springframework.core.convert.converter.Converter<String, PK> {
//
//        @Override
//        public PK convert(String source) {
//            final String[] split = source.split("_");
//            return new PK(Long.valueOf(split[0]), new DateTime(split[1]));
//        }
//    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @EmbeddedId
//    private SiteStatId id;
//    @Id
    @Column(name = "watchedsite_id", nullable = false)
    private Long watchedSiteId;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(nullable = false)
//    @Column(name = "creationtime")
    private DateTime creationTime;
    @ManyToOne
    @JoinColumn(name = "watchedsite_id", insertable = false, updatable = false)
    private WatchedSite watchedSite;

    private Integer followerCount;
    private Integer followedByCount;
    private Integer postLikedByCount;
    private Integer postCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWatchedSiteId() {
        return watchedSiteId;
    }

    public void setWatchedSiteId(Long watchedSiteId) {
        this.watchedSiteId = watchedSiteId;
    }

    /**
     * This is not actual creation time but rather temporal point-time.
     * @return
     */
    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }

    public WatchedSite getWatchedSite() {
        return watchedSite;
    }

    /**
     * Number of followers or members of this site/social media account.
     * @return
     */
    public Integer getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    /**
     * Number of accounts followed by this site/social media account.
     * @return
     */
    public Integer getFollowedByCount() {
        return followedByCount;
    }

    public void setFollowedByCount(Integer followedByCount) {
        this.followedByCount = followedByCount;
    }

    /**
     * Number of posts liked by this site/social media account.
     * @return
     */
    public Integer getPostLikedByCount() {
        return postLikedByCount;
    }

    public void setPostLikedByCount(Integer postLikedByCount) {
        this.postLikedByCount = postLikedByCount;
    }

    /**
     * Number of posts by this site/social media account.
     * @return
     */
    public Integer getPostCount() {
        return postCount;
    }

    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("watchedSiteId", watchedSiteId)
                .add("creationTime", creationTime)
                .add("followerCount", followerCount)
                .add("followedByCount", followedByCount)
                .add("postLikedByCount", postLikedByCount)
                .add("postCount", postCount)
                .toString();
    }
}
