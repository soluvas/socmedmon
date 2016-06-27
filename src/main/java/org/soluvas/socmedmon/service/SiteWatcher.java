package org.soluvas.socmedmon.service;

import com.google.common.collect.ImmutableList;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soluvas.socmed.TwitterApp;
import org.soluvas.socmed.TwitterAuthorization;
import org.soluvas.socmedmon.core.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import java.util.List;
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

//    @Scheduled(cron = "0 */60 * * * *")
    @Scheduled(fixedDelay = 60 * 60 * 1000) // once per hour
    @Transactional
    public void fetchStats() throws TwitterException {
        final DateTime pointTime = new DateTime().withMillisOfSecond(0);
        final ImmutableList<WatchedSite> watchedSites = ImmutableList.copyOf(watchedSiteRepo.findAll());
        log.info("Fetching stats for {} watched sites: {}",
                watchedSites.size(), watchedSites.stream().limit(10).toArray());
        final List<WatchedSite> twitterUsingIds = watchedSites.stream().filter(it -> ExternalSite.TWITTER == it.getKind() && null != it.getSiteId()).collect(Collectors.toList());
        final List<WatchedSite> twitterUsingScreenNames = watchedSites.stream().filter(it -> ExternalSite.TWITTER == it.getKind() && null == it.getSiteId()).collect(Collectors.toList());

        final Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(twitterApp.getApiKey(), twitterApp.getApiSecret());
        twitter.setOAuthAccessToken(new AccessToken(twitterAuth.getAccessToken(), twitterAuth.getAccessTokenSecret()));

        if (!twitterUsingIds.isEmpty()) {
            final long[] ids = twitterUsingIds.stream().mapToLong(it -> Long.valueOf(it.getSiteId())).toArray();
            log.info("Fetching {} Twitter stats by ID: {}", ids.length, ids);
            try {
                final ResponseList<User> users = twitter.users().lookupUsers(ids);
                saveStat(pointTime, watchedSites, users);
            } catch (TwitterException e) {
                log.error(String.format("Cannot fetch %s Twitter stats by ID: %s", ids.length, ids), e);
            }
        }
        if (!twitterUsingScreenNames.isEmpty()) {
            final String[] screenNames = twitterUsingScreenNames.stream().map(it -> it.getSiteScreenName()).toArray(String[]::new);
            log.info("Fetching {} Twitter stats by screen name: {}", screenNames.length, screenNames);
            try {
                final ResponseList<User> users = twitter.users().lookupUsers(screenNames);
                saveStat(pointTime, watchedSites, users);
            } catch (TwitterException e) {
                log.error(String.format("Cannot fetch %s Twitter stats by screen name: %s", screenNames.length, screenNames), e);
            }
        }
    }

    protected void saveStat(DateTime pointTime, ImmutableList<WatchedSite> watchedSites, ResponseList<User> users) {
        for (final User user : users) {
            final SiteStat siteStat = new SiteStat();
            WatchedSite watchedSite = watchedSites.stream().filter(it -> ExternalSite.TWITTER == it.getKind() &&
                    (String.valueOf(user.getId()).equals(it.getSiteId()) ||
                            user.getScreenName().equalsIgnoreCase(it.getSiteScreenName()))).findAny().get();
            // merge entity
            watchedSite = watchedSiteRepo.findOne(watchedSite.getId());

//            siteStat.setId(new SiteStatId(watchedSite.getId(), new DateTime()));
            siteStat.setWatchedSiteId(watchedSite.getId());
            siteStat.setCreationTime(pointTime);
            siteStat.setFollowerCount(user.getFollowersCount());
            siteStat.setFollowedByCount(user.getFriendsCount());
            siteStat.setPostCount(user.getStatusesCount());
            siteStat.setPostLikedByCount(user.getFavouritesCount());
            siteStatRepo.save(siteStat);
            log.debug("Saved {} stat: {}", watchedSite, siteStat);

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
            siteSummary.getFollowerCount().setLatest(siteStat.getFollowerCount());
            siteSummary.getFollowedByCount().setLatest(siteStat.getFollowedByCount());
            siteSummary.getPostCount().setLatest(siteStat.getPostCount());
            siteSummary.getPostLikedByCount().setLatest(siteStat.getPostLikedByCount());
            siteSummaryRepo.save(siteSummary);
        }
    }
}
