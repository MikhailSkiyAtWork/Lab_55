package com.example.admin.personallibrarycatalogue;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;


public class BooksListActivity extends ActionBarActivity {

    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);

        contextOfApplication = getApplicationContext();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, new BooksListActivityFragment())
                    .commit();
        }
    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

    @Override
    public void onResume(){
        super.onResume();
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(ConstantValues.TWITTER_CALLBACK_URL)) {
            String verifier = uri.getQueryParameter(ConstantValues.URL_PARAMETER_TWITTER_OAUTH_VERIFIER);
            String status = ((BooksListActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment)).getStatus();
            new TwitterGetAccessTokenTask().execute(verifier,status);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_books_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.add_book_action:
                Intent intent = new Intent(this, AddBookActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
