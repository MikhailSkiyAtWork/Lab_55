package com.example.admin.personallibrarycatalogue;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Mikhail on 15.06.2015.
 */
public final class TwitterUtil {

    private RequestToken requestToken_ = null;
    private TwitterFactory twitterFactory_ = null;
    private Twitter twitter_;

    private TwitterUtil() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(ConstantValues.TWITTER_CONSUMER_KEY);
        configurationBuilder.setOAuthConsumerSecret(ConstantValues.TWITTER_CONSUMER_SECRET);
        twitter4j.conf.Configuration configuration = configurationBuilder.build();
        twitterFactory_ = new TwitterFactory(configuration);
        twitter_ = twitterFactory_.getInstance();
    }

    public TwitterFactory getTwitterFactory()
    {
        return twitterFactory_;
    }

    public void setTwitterFactory(AccessToken accessToken)
    {
        twitter_ = twitterFactory_.getInstance(accessToken);
    }

    public Twitter getTwitter()
    {
        return twitter_;
    }
    public RequestToken getRequestToken() {
        if (requestToken_ == null) {
            try {
                requestToken_ = twitterFactory_.getInstance().getOAuthRequestToken(ConstantValues.TWITTER_CALLBACK_URL);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
        return requestToken_;
    }

    static TwitterUtil instance = new TwitterUtil();

    public static TwitterUtil getInstance() {
        return instance;
    }


    public void reset() {
        instance = new TwitterUtil();
    }
}
