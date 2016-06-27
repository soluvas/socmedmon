package org.soluvas.socmedmon.service;

import com.google.common.collect.ImmutableList;
import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.Reading;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soluvas.socmed.FacebookApp;
import org.soluvas.socmed.FacebookAuthorization;
import org.soluvas.socmed.TwitterApp;
import org.soluvas.socmed.TwitterAuthorization;
import org.soluvas.socmedmon.core.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by ceefour on 26/06/2016.
 */
@Service
public class SiteWatcher {

    private static final Logger log = LoggerFactory.getLogger(SiteWatcher.class);

    @Inject
    private WatchedSiteRepository watchedSiteRepo;
    @Inject
    private SiteStatRepository siteStatRepo;
    @Inject
    private SiteSummaryRepository siteSummaryRepo;
    @Inject
    private TwitterFactory twitterFactory;
    @Inject
    private TwitterApp twitterApp;
    @Inject
    private TwitterAuthorization twitterAuth;
    @Inject
    private FacebookFactory facebookFactory;
    @Inject
    private FacebookApp facebookApp;
    @Inject
    private FacebookAuthorization facebookAuth;

//    @Scheduled(cron = "0 */60 * * * *")
    @Scheduled(fixedDelay = 60 * 60 * 1000) // once per hour
    @Transactional
    public void fetchStatsTwitter() throws TwitterException {
        final DateTime pointTime = new DateTime().withMillisOfSecond(0);
        final ImmutableList<WatchedSite> watchedSites = ImmutableList.copyOf(watchedSiteRepo.findAllByKind(ExternalSite.TWITTER, new PageRequest(0, 1000)));
        log.info("Fetching Twitter stats for {} watched sites: {}",
                watchedSites.size(), watchedSites.stream().limit(10).toArray());
        final List<WatchedSite> twitterUsingIds = watchedSites.stream().filter(it -> ExternalSite.TWITTER == it.getKind() && null != it.getSiteId()).collect(Collectors.toList());
        final List<WatchedSite> twitterUsingScreenNames = watchedSites.stream().filter(it -> ExternalSite.TWITTER == it.getKind() && null == it.getSiteId()).collect(Collectors.toList());

        final Twitter twitter = twitterFactory.getInstance();
        twitter.setOAuthAccessToken(new AccessToken(twitterAuth.getAccessToken(), twitterAuth.getAccessTokenSecret()));

        if (!twitterUsingIds.isEmpty()) {
            final long[] ids = twitterUsingIds.stream().mapToLong(it -> Long.valueOf(it.getSiteId())).toArray();
            log.info("Fetching {} Twitter stats by ID: {}", ids.length, ids);
            try {
                final ResponseList<User> users = twitter.users().lookupUsers(ids);
                if (users.size() != ids.length) {
                    log.warn("Expected {} Twitter users but only got {}", ids.length, users.size());
                }
                saveTwitterStat(pointTime, watchedSites, users);
            } catch (TwitterException e) {
                log.error(String.format("Cannot fetch %s Twitter stats by ID: %s", ids.length, ids), e);
            }
        }
        if (!twitterUsingScreenNames.isEmpty()) {
            final String[] screenNames = twitterUsingScreenNames.stream().map(it -> it.getSiteScreenName()).toArray(String[]::new);
            log.info("Fetching {} Twitter stats by screen name: {}", screenNames.length, screenNames);
            try {
                final ResponseList<User> users = twitter.users().lookupUsers(screenNames);
                if (users.size() != screenNames.length) {
                    log.warn("Expected {} Twitter users but only got {}", screenNames.length, users.size());
                }
                saveTwitterStat(pointTime, watchedSites, users);
            } catch (TwitterException e) {
                log.error(String.format("Cannot fetch %s Twitter stats by screen name: %s", screenNames.length, screenNames), e);
            }
        }
    }

    protected void saveTwitterStat(DateTime pointTime, ImmutableList<WatchedSite> watchedSites, ResponseList<User> users) {
        log.info("Saving stats for {} Twitter users...", users.size());
        for (final User user : users) {
            final SiteStat siteStat = new SiteStat();
            final WatchedSite watchedSite = watchedSites.stream().filter(it -> ExternalSite.TWITTER == it.getKind() &&
                    (String.valueOf(user.getId()).equals(it.getSiteId()) ||
                            user.getScreenName().equalsIgnoreCase(it.getSiteScreenName()))).findAny().get();

//            siteStat.setId(new SiteStatId(watchedSite.getId(), new DateTime()));
            siteStat.setWatchedSiteId(watchedSite.getId());
            siteStat.setCreationTime(pointTime);
            siteStat.setFollowerCount(user.getFollowersCount());
            siteStat.setFollowedByCount(user.getFriendsCount());
            siteStat.setPostCount(user.getStatusesCount());
            siteStat.setPostLikedByCount(user.getFavouritesCount());
            siteStatRepo.save(siteStat);
            log.debug("Saved {} stat: {}", watchedSite, siteStat);

            summarize(pointTime, siteStat, watchedSite);
        }
    }

    //    @Scheduled(cron = "0 */60 * * * *")
    @Scheduled(fixedDelay = 60 * 60 * 1000) // once per hour
    @Transactional
    public void fetchStatsFacebook() throws TwitterException {
        final DateTime pointTime = new DateTime().withMillisOfSecond(0);
        final ImmutableList<WatchedSite> watchedSites = ImmutableList.copyOf(watchedSiteRepo.findAllByKind(ExternalSite.FACEBOOK, new PageRequest(0, 1000)));
        log.info("Fetching Facebook stats for {} watched sites: {}",
                watchedSites.size(), watchedSites.stream().limit(10).toArray());
        final List<WatchedSite> facebookUsingIds = watchedSites.stream().filter(it -> ExternalSite.FACEBOOK == it.getKind() && null != it.getSiteId()).collect(Collectors.toList());
        final List<WatchedSite> facebookUsingScreenNames = watchedSites.stream().filter(it -> ExternalSite.FACEBOOK == it.getKind() && null == it.getSiteId()).collect(Collectors.toList());

        final Facebook facebook = facebookFactory.getInstance(new facebook4j.auth.AccessToken(facebookAuth.getAccessToken()));

        // PPTIKITB/?fields=name,fan_count,username
        // {
//                "name": "Pusat Penelitian Teknologi Informasi dan Komunikasi",
//                        "fan_count": 526,
//                        "username": "PPTIKITB",
//                        "talking_about_count": 11,
//                        "were_here_count": 59,
//                        "id": "752403644873915"
//            }
        if (!facebookUsingIds.isEmpty()) {
            final List<String> ids = facebookUsingIds.stream().map(WatchedSite::getSiteId).collect(Collectors.toList());
            log.info("Fetching {} Facebook stats by ID: {}", ids.size(), ids);
            try {
                for (WatchedSite watch : facebookUsingIds) {
                    final facebook4j.Page page = facebook.pages().getPage(watch.getSiteId(),
                            new Reading().fields("name", "username", "fan_count", "talking_about_count", "were_here_count"));
                    log.debug("Received page: {}", page);
                    saveFacebookStat(pointTime, watch, page);
                }
            } catch (Exception e) {
                log.error(String.format("Cannot fetch %s Facebook stats by ID: %s", ids.size(), ids), e);
            }
        }
        if (!facebookUsingScreenNames.isEmpty()) {
            final List<String> screenNames = facebookUsingScreenNames.stream().map(WatchedSite::getSiteScreenName).collect(Collectors.toList());
            log.info("Fetching {} Facebook stats by screen names: {}", screenNames.size(), screenNames);
            try {
                for (WatchedSite watch : facebookUsingScreenNames) {
                    final facebook4j.Page page = facebook.pages().getPage(watch.getSiteScreenName(),
                            new Reading().fields("name", "username", "likes", "fan_count", "talking_about_count", "were_here_count"));
                    log.debug("Received page: {}", page);
                    saveFacebookStat(pointTime, watch, page);
                }
            } catch (Exception e) {
                log.error(String.format("Cannot fetch %s Facebook stats by screen names: %s", screenNames.size(), screenNames), e);
            }
        }
    }

    protected void saveFacebookStat(DateTime pointTime, WatchedSite watchedSite, facebook4j.Page page) {
        final SiteStat siteStat = new SiteStat();

//            siteStat.setId(new SiteStatId(watchedSite.getId(), new DateTime()));
        siteStat.setWatchedSiteId(watchedSite.getId());
        siteStat.setCreationTime(pointTime);
        siteStat.setFollowerCount(page.getFanCount());
        siteStat.setFollowedByCount(page.getLikes());
        siteStatRepo.save(siteStat);
        log.debug("Saved {} stat: {}", watchedSite, siteStat);

        summarize(pointTime, siteStat, watchedSite);
    }

    protected SiteSummary summarize(DateTime pointTime, SiteStat siteStat, WatchedSite watchedSite) {
        SiteSummary siteSummary = siteSummaryRepo.findOne(watchedSite.getId());
        if (null == siteSummary) {
            siteSummary = new SiteSummary();
            siteSummary.setWatchedSite(watchedSite);
        }
        if (null == siteSummary.getFollowerCount()) {
            siteSummary.setFollowerCount(new SiteSummary.Metric());
        }
        if (null == siteSummary.getFollowedByCount()) {
            siteSummary.setFollowedByCount(new SiteSummary.Metric());
        }
        if (null == siteSummary.getPostCount()) {
            siteSummary.setPostCount(new SiteSummary.Metric());
        }
        if (null == siteSummary.getPostLikedByCount()) {
            siteSummary.setPostLikedByCount(new SiteSummary.Metric());
        }
        siteSummary.setModificationTime(pointTime);

        // Latest
        siteSummary.getFollowerCount().setLatest(siteStat.getFollowerCount());
        siteSummary.getFollowedByCount().setLatest(siteStat.getFollowedByCount());
        siteSummary.getPostCount().setLatest(siteStat.getPostCount());
        siteSummary.getPostLikedByCount().setLatest(siteStat.getPostLikedByCount());

        // Comparative Metrics
        final Optional<SiteStat> dayPrev = siteStatRepo.findAllBySiteBefore(watchedSite.getId(), pointTime.minusDays(1), new PageRequest(0, 1, Sort.Direction.DESC, "creationTime")).getContent().stream().findFirst();
        final Optional<SiteStat> weekPrev = siteStatRepo.findAllBySiteBefore(watchedSite.getId(), pointTime.minusWeeks(1), new PageRequest(0, 1, Sort.Direction.DESC, "creationTime")).getContent().stream().findFirst();
        final Optional<SiteStat> monthPrev = siteStatRepo.findAllBySiteBefore(watchedSite.getId(), pointTime.minusMonths(1), new PageRequest(0, 1, Sort.Direction.DESC, "creationTime")).getContent().stream().findFirst();
        log.debug("Prev stat for {}: day={} week={} month={}",
                watchedSite, dayPrev.orElse(null), weekPrev.orElse(null), monthPrev.orElse(null));
        setComparativeMetric(siteSummary.getFollowerCount(), dayPrev.map(SiteStat::getFollowerCount),
                weekPrev.map(SiteStat::getFollowerCount), monthPrev.map(SiteStat::getFollowerCount));
        setComparativeMetric(siteSummary.getFollowedByCount(), dayPrev.map(SiteStat::getFollowedByCount),
                weekPrev.map(SiteStat::getFollowedByCount), monthPrev.map(SiteStat::getFollowedByCount));
        setComparativeMetric(siteSummary.getPostCount(), dayPrev.map(SiteStat::getPostCount),
                weekPrev.map(SiteStat::getPostCount), monthPrev.map(SiteStat::getPostCount));
        setComparativeMetric(siteSummary.getPostLikedByCount(), dayPrev.map(SiteStat::getPostLikedByCount),
                weekPrev.map(SiteStat::getPostLikedByCount), monthPrev.map(SiteStat::getPostLikedByCount));

        siteSummary = siteSummaryRepo.save(siteSummary);
        log.debug("Updated {} summary: {}", watchedSite, siteSummary);
        return siteSummary;
    }

    /**
     * Update temporal-comparative metric values for a metric. It does not modify database.
     * @param metric
     * @param dayPrevValue
     * @param weekPrevValue
     * @param monthPrevValue
     */
    protected void setComparativeMetric(SiteSummary.Metric metric, Optional<Integer> dayPrevValue,
                                        Optional<Integer> weekPrevValue, Optional<Integer> monthPrevValue) {
        // DayPrev
        if (dayPrevValue.isPresent()) {
            metric.setDayPrev(dayPrevValue.get());
            metric.setDayDelta(metric.getLatest() - dayPrevValue.get());
            if (dayPrevValue.get() != 0) {
                metric.setDayChange(metric.getDayDelta() * 100f / dayPrevValue.get());
            } else {
                metric.setDayChange(0f);
            }
            metric.setDayTrend(metric.getDayDelta() > 0 ?
                    SiteSummary.PeriodTrend.UP :
                    (metric.getDayDelta() > 0 ? SiteSummary.PeriodTrend.DOWN : SiteSummary.PeriodTrend.NONE));
        }
        // WeekPrev
        if (weekPrevValue.isPresent()) {
            metric.setWeekPrev(weekPrevValue.get());
            metric.setWeekDelta(metric.getLatest() - weekPrevValue.get());
            if (weekPrevValue.get() != 0) {
                metric.setWeekChange(metric.getWeekDelta() * 100f / weekPrevValue.get());
            } else {
                metric.setWeekChange(0f);
            }
            metric.setWeekTrend(metric.getWeekDelta() > 0 ?
                    SiteSummary.PeriodTrend.UP :
                    (metric.getWeekDelta() > 0 ? SiteSummary.PeriodTrend.DOWN : SiteSummary.PeriodTrend.NONE));
        }
        // MonthPrev
        if (monthPrevValue.isPresent()) {
            metric.setMonthPrev(monthPrevValue.get());
            metric.setMonthDelta(metric.getLatest() - monthPrevValue.get());
            if (monthPrevValue.get() != 0) {
                metric.setMonthChange(metric.getMonthDelta() * 100f / monthPrevValue.get());
            } else {
                metric.setMonthChange(0f);
            }
            metric.setMonthTrend(metric.getMonthDelta() > 0 ?
                    SiteSummary.PeriodTrend.UP :
                    (metric.getMonthDelta() > 0 ? SiteSummary.PeriodTrend.DOWN : SiteSummary.PeriodTrend.NONE));
        }
    }
}
