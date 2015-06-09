package com.example.admin.personallibrarycatalogue;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;

/**
 * Created by Mikhail Valuyskiy on 08.06.2015.
 */
public class OAuthRequestTokenTask extends AsyncTask<Void,Void,Void>{

    final String TAG = getClass().getName();
    private Context context;
    private OAuthProvider provider;
    private OAuthConsumer consumer;

    public OAuthRequestTokenTask(Context context, OAuthConsumer consumer, OAuthProvider provider){
        this.context = context;
        this.consumer = consumer;
        this.provider = provider;
    }

    /**
     *
     * Retrieve the OAuth Request Token and present a browser to the user to authorize the token.
     *
     */

    @Override
    protected Void doInBackground(Void... params){
        try {
            Log.i(TAG, "Retrieving request token from G servers");
            final String url = provider.retrieveRequestToken(consumer,Constants.OAUTH_CALLBACK_URL);
            Log.i(TAG,"Popping a browser with the authorize URL: " + url);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url)).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_NO_HISTORY
                    | Intent.FLAG_FROM_BACKGROUND);

            context.startActivity(intent);
        } catch (Exception e){
            Log.e(TAG,"Error during OAUth retrieve request token",e);
        }
        return null;
    }
}
