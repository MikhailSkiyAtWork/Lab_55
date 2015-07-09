package com.example.admin.personallibrarycatalogue;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.admin.personallibrarycatalogue.data.Book;
import com.example.admin.personallibrarycatalogue.data.LibraryDatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final String APP_NAME = "com.example.admin.personallibrarycatalogue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LibraryDatabaseHelper helper_ = new LibraryDatabaseHelper(this);

        // If app was launched first time or there are no books, show user sugestion to add book
        if (!(helper_.isDatabaseContainsItems())) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, new FirstLaunchFragment())
                    .commit();
        } else {
            Intent intent = new Intent(this, BooksListActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.add_book_action:
                Intent intent = new Intent(this,AddBookActivity.class);
                startActivity(intent);
             default:
                 return super.onOptionsItemSelected(item);
        }
    }
}
