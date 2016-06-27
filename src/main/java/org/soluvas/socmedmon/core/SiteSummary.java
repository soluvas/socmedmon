package org.soluvas.socmedmon.core;

import com.google.common.base.MoreObjects;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.rest.core.config.Projection;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Latest stats for a {@link WatchedSite}, including comparison with previous periods.
 * Created by ceefour on 27/06/2016.
 */
@Entity
public class SiteSummary implements Serializable {

    public static enum PeriodTrend {
        NONE,
        /**
         * Depending on the metric, up/increasing can be either better or worse.
         */
        UP,
        /**
         * Depending on the metric, down/decreasing can be either better or worse.
         */
        DOWN
    }

    @Embeddable
    public static class Metric {
        private Integer latest;
        private Integer dayPrev;
        private Integer dayDelta;
        private Float dayChange;
        @Enumerated(EnumType.STRING)
        private PeriodTrend dayTrend;
        private Integer weekPrev;
        private Integer weekDelta;
        private Float weekChange;
        @Enumerated(EnumType.STRING)
        private PeriodTrend weekTrend;
        private Integer monthPrev;
        private Integer monthDelta;
        private Float monthChange;
        @Enumerated(EnumType.STRING)
        private PeriodTrend monthTrend;

        public Integer getLatest() {
            return latest;
        }

        public void setLatest(Integer latest) {
            this.latest = latest;
        }

        /**
         * Yesterday's value.
         * @return
         */
        public Integer getDayPrev() {
            return dayPrev;
        }

        public void setDayPrev(Integer dayPrev) {
            this.dayPrev = dayPrev;
        }

        public Integer getDayDelta() {
            return dayDelta;
        }

        public void setDayDelta(Integer dayDelta) {
            this.dayDelta = dayDelta;
        }

        /**
         * Change in percentage.
         * @return
         */
        public Float getDayChange() {
            return dayChange;
        }

        public void setDayChange(Float dayChange) {
            this.dayChange = dayChange;
        }

        public PeriodTrend getDayTrend() {
            return dayTrend;
        }

        public void setDayTrend(PeriodTrend dayTrend) {
            this.dayTrend = dayTrend;
        }

        public Integer getWeekPrev() {
            return weekPrev;
        }

        public void setWeekPrev(Integer weekPrev) {
            this.weekPrev = weekPrev;
        }

        public Integer getWeekDelta() {
            return weekDelta;
        }

        public void setWeekDelta(Integer weekDelta) {
            this.weekDelta = weekDelta;
        }

        /**
         * Change in percentage.
         * @return
         */
        public Float getWeekChange() {
            return weekChange;
        }

        public void setWeekChange(Float weekChange) {
            this.weekChange = weekChange;
        }

        public PeriodTrend getWeekTrend() {
            return weekTrend;
        }

        public void setWeekTrend(PeriodTrend weekTrend) {
            this.weekTrend = weekTrend;
        }

        public Integer getMonthPrev() {
            return monthPrev;
        }

        public void setMonthPrev(Integer monthPrev) {
            this.monthPrev = monthPrev;
        }

        public Integer getMonthDelta() {
            return monthDelta;
        }

        public void setMonthDelta(Integer monthDelta) {
            this.monthDelta = monthDelta;
        }

        /**
         * Change in percentage.
         * @return
         */
        public Float getMonthChange() {
            return monthChange;
        }

        public void setMonthChange(Float monthChange) {
            this.monthChange = monthChange;
        }

        public PeriodTrend getMonthTrend() {
            return monthTrend;
        }

        public void setMonthTrend(PeriodTrend monthTrend) {
            this.monthTrend = monthTrend;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues()
                    .add("latest", latest)
                    .add("dayPrev", dayPrev)
                    .add("dayDelta", dayDelta)
                    .add("dayChange", dayChange)
                    .add("dayTrend", dayTrend)
                    .add("weekPrev", weekPrev)
                    .add("weekDelta", weekDelta)
                    .add("weekChange", weekChange)
                    .add("weekTrend", weekTrend)
                    .add("monthPrev", monthPrev)
                    .add("monthDelta", monthDelta)
                    .add("monthChange", monthChange)
                    .add("monthTrend", monthTrend)
                    .toString();
        }
    }

    @Projection(name = "inline", types = SiteSummary.class)
    public interface Inline {
        Long getWatchedSiteId();
        WatchedSite getWatchedSite();
        DateTime getModificationTime();
        Metric getFollowerCount();
        Metric getFollowedByCount();
        Metric getPostLikedByCount();
        Metric getPostCount();
    }

    @Id @Column(name = "watchedsite_id")
    private Long watchedSiteId;
    @MapsId
    @OneToOne(mappedBy = "summary")
    @JoinColumn(name = "watchedsite_id")
    private WatchedSite watchedSite;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modificationTime;

    @Embedded
    private Metric followerCount;
    @Embedded
    private Metric followedByCount;
    @Embedded
    private Metric postLikedByCount;
    @Embedded
    private Metric postCount;

    public Long getWatchedSiteId() {
        return watchedSiteId;
    }

    public void setWatchedSiteId(Long watchedSiteId) {
        this.watchedSiteId = watchedSiteId;
    }

    public WatchedSite getWatchedSite() {
        return watchedSite;
    }

    public void setWatchedSite(WatchedSite watchedSite) {
        this.watchedSite = watchedSite;
    }

    public DateTime getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(DateTime modificationTime) {
        this.modificationTime = modificationTime;
    }

    public Metric getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(Metric followerCount) {
        this.followerCount = followerCount;
    }

    public Metric getFollowedByCount() {
        return followedByCount;
    }

    public void setFollowedByCount(Metric followedByCount) {
        this.followedByCount = followedByCount;
    }

    public Metric getPostLikedByCount() {
        return postLikedByCount;
    }

    public void setPostLikedByCount(Metric postLikedByCount) {
        this.postLikedByCount = postLikedByCount;
    }

    public Metric getPostCount() {
        return postCount;
    }

    public void setPostCount(Metric postCount) {
        this.postCount = postCount;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("watchedSiteId", watchedSiteId)
                .add("modificationTime", modificationTime)
                .add("followerCount", followerCount)
                .add("followedByCount", followedByCount)
                .add("postLikedByCount", postLikedByCount)
                .add("postCount", postCount)
                .toString();
    }
}