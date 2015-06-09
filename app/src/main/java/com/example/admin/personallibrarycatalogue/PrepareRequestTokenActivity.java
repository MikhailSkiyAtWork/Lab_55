package com.example.admin.personallibrarycatalogue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

/**
 * Created by Mikhail Valuyskiy on 08.06.2015.
 */
public class PrepareRequestTokenActivity extends Activity {
    final String TAG = getClass().getName();

    private OAuthConsumer authConsumer_;
    private OAuthProvider authProvider_;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        try {
            this.authConsumer_ = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
            this.authProvider_ = new CommonsHttpOAuthProvider(Constants.REQUEST_URL, Constants.ACCESS_URL, Constants.AUTHORIZE_URL);
        }catch (Exception e){
            Log.e(TAG, "Error creating consumer_ or provider_",e);
        }
        Log.i(TAG,"Starting task to retrieve request token.");
        new OAuthRequestTokenTask(this, authConsumer_, authProvider_).execute();
    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final Uri uri = intent.getData();
        if (uri != null && uri.getScheme().equals(Constants.OAUTH_CALLBACK_HOST)){
            Log.i(TAG,"Callback received :" + uri);
            Log.i(TAG,"Retrieving Access Token");
            new RetrieveAccessTokenTask(this, authConsumer_, authProvider_,preferences).execute(uri);
    }
    }

    public class RetrieveAccessTokenTask extends AsyncTask<Uri,Void,Void>{
        private Context context_;
        private OAuthProvider provider_;
        private OAuthConsumer consumer_;
        private SharedPreferences preferences_;

        public RetrieveAccessTokenTask(Context context, OAuthConsumer consumer,OAuthProvider provider, SharedPreferences preferences){
            this.context_ = context;
            this.consumer_ = consumer;
            this.provider_ = provider;
            this.preferences_ = preferences;

        }

        @Override
        protected Void doInBackground(Uri... params){
            final Uri uri = params[0];
            final String oauthVerifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);

            try {
                provider_.retrieveAccessToken(consumer_, oauthVerifier);

                final SharedPreferences.Editor preferencesEditor = preferences_.edit();
                preferencesEditor.putString(OAuth.OAUTH_TOKEN, consumer_.getToken());
                preferencesEditor.putString(OAuth.OAUTH_TOKEN_SECRET, consumer_.getTokenSecret());
                preferencesEditor.commit();

                String token = preferences_.getString(OAuth.OAUTH_TOKEN, "");
                String secret = preferences_.getString(OAuth.OAUTH_TOKEN_SECRET, "");

                consumer_.setTokenWithSecret(token, secret);
                context_.startActivity(new Intent(context_, BooksListActivityFragment.class));

                executeAfterAccessTokenRetrieval();
                Log.i(TAG, "OAuth - Access token retrieved");
            } catch (Exception e){
                Log.e(TAG, "OAuth - Access Token Retrieval Error",e);

            }
            return null;
        }

        private void executeAfterAccessTokenRetrieval(){
            String msg = getIntent().getExtras().getString("tweet_msg");
            try {
                TwitterUtils.sendTweet(preferences_, msg);
            } catch (Exception e){
                Log.e(TAG,"OAuth - Error sending to Twitter",e);
            }
        }
    }

}
