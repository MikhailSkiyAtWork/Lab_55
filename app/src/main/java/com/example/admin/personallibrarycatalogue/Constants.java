package com.example.admin.personallibrarycatalogue;

/**
 * Created by Mikhail Valuyskiy on 08.06.2015.
 */
public class Constants {



    public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
    public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
    public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";

    public static final String	OAUTH_CALLBACK_SCHEME	= "x-oauthflow-twitter";
    public static final String	OAUTH_CALLBACK_HOST		= "callback";
    public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;

}
