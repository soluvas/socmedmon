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
    FACEBOOK,
    TWITTER,
    INSTAGRAM,
    FOURSQUARE,
    /**
     * A Google Play app or asset.
     */
    GOOGLE_PLAY,
}
