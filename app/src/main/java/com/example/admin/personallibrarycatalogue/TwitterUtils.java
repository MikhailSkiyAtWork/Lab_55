package com.example.admin.personallibrarycatalogue;

import android.content.SharedPreferences;
import android.os.StrictMode;

import oauth.signpost.OAuth;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Created by Mikhail Valuyskiy on 08.06.2015.
 */
public class TwitterUtils {

    public static boolean isAuthenticated(SharedPreferences preferences){

        String token = preferences.getString(OAuth.OAUTH_TOKEN,"");
        String secret = preferences.getString(OAuth.OAUTH_TOKEN_SECRET,"");

        AccessToken accessToken = new AccessToken(token,secret);
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
        twitter.setOAuthAccessToken(accessToken);

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            twitter.getAccountSettings();
            return true;
        } catch (TwitterException e){
            return false;
        }
    }

    public static void sendTweet(SharedPreferences preferences, String message) throws Exception{
        String token = preferences.getString(OAuth.OAUTH_TOKEN,"");
        String secret = preferences.getString(OAuth.OAUTH_TOKEN_SECRET,"");

        AccessToken a = new AccessToken(token,secret);
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(Constants.CONSUMER_KEY,Constants.CONSUMER_SECRET);
        twitter.setOAuthAccessToken(a);
        twitter.updateStatus(message);
    }
}
