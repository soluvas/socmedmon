package org.soluvas.socmedmon.core;

/**
 * Created by ceefour on 7/10/15.
 * Via soluvas-buzz.
 */
public enum ExternalSite {
    /**
     * Plain web site. When watched using Socmedmon, makes sure it's giving proper 200 response and not showing generic Apache/nginx page.
     */
    WEB,
    /**
     * Facebook Page.
     * For Facebook user, see {@link #FACEBOOK_USER}.
     * For Facebook group, see {@link #FACEBOOK_GROUP}.
     */
    FACEBOOK,
    /**
     * Facebook User. For Facebook page, see {@link #FACEBOOK}.
     */
    FACEBOOK_USER,
    /**
     * Facebook Group. For Facebook page, see {@link #FACEBOOK}.
     */
    FACEBOOK_GROUP,
    TWITTER,
    INSTAGRAM,
    FOURSQUARE,
    /**
     * A Google Play app or asset.
     */
    GOOGLE_PLAY,
}
