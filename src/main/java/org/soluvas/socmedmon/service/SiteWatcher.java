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
    private TwitterFactory twitterFactory;
    @Inject
    private TwitterApp twitterApp;
    @Inject
    private TwitterAuthorization twitterAuth;

//    @Scheduled(cron = "0 */60 * * * *")
    @Scheduled(fixedDelay = 60 * 60 * 1000) // once per hour
    public void fetchStats() throws TwitterException {
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
            final ResponseList<User> users = twitter.users().lookupUsers(ids);
            saveStat(watchedSites, users);
        }
        if (!twitterUsingScreenNames.isEmpty()) {
            final String[] screenNames = twitterUsingScreenNames.stream().map(it -> it.getSiteScreenName()).toArray(String[]::new);
            log.info("Fetching {} Twitter stats by screen name: {}", screenNames.length, screenNames);
            final ResponseList<User> users = twitter.users().lookupUsers(screenNames);
            saveStat(watchedSites, users);
        }
    }

    protected void saveStat(ImmutableList<WatchedSite> watchedSites, ResponseList<User> users) {
        for (final User user : users) {
            final SiteStat siteStat = new SiteStat();
            final WatchedSite watchedSite = watchedSites.stream().filter(it -> ExternalSite.TWITTER == it.getKind() &&
                    (String.valueOf(user.getId()).equals(it.getSiteId()) ||
                            user.getScreenName().equalsIgnoreCase(it.getSiteScreenName()))).findAny().get();
            siteStat.setId(new SiteStatId(watchedSite.getId(), new DateTime()));
            siteStat.setFollowerCount(user.getFollowersCount());
            siteStat.setFollowedByCount(user.getFriendsCount());
            siteStat.setPostCount(user.getStatusesCount());
            siteStat.setPostLikedByCount(user.getFavouritesCount());
            siteStatRepo.save(siteStat);
            log.debug("Saved {} stat: {}", watchedSite, siteStat);
        }
    }
}
