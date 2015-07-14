package com.example.admin.personallibrarycatalogue;


import android.content.ContentValues;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.personallibrarycatalogue.data.Book;
import com.example.admin.personallibrarycatalogue.data.DatabaseContract;
import com.example.admin.personallibrarycatalogue.data.LibraryDatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import twitter4j.auth.RequestToken;


/**
 * The fragment which responsible for showing list of all books
 */
public class BooksListActivityFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LIBRARY_LOADER = 0;
    private static final String SPACE = " ";
    private static final String I_HAVE_BOOK = "I have book";
    private static final String AND_WANT_TO_LET_KNOW = "and want to let world about it!";
    private final static String ID = "id";
    private ListView listView_;
    private static String status_ = "";
    private BooksListAdapter booksListAdapter_;


    private static final String[] BOOK_COLUMNS = {

            DatabaseContract.BooksTable.TABLE_NAME + "." + DatabaseContract.BooksTable._ID,
            DatabaseContract.BooksTable.TITLE,
            DatabaseContract.BooksTable.AUTHOR,
            DatabaseContract.BooksTable.DESCRIPTION,
            DatabaseContract.BooksTable.COVER,
            DatabaseContract.BooksTable.YEAR,
            DatabaseContract.BooksTable.ISBN
    };

    public BooksListActivityFragment() {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                getActivity(),
                DatabaseContract.BooksTable.CONTENT_URI,
                BOOK_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        booksListAdapter_.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        booksListAdapter_.swapCursor(null);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_books_list, container, false);


        listView_ = (ListView) rootView.findViewById(R.id.books_list_view);
        booksListAdapter_ = new BooksListAdapter(getActivity(), null, 0);
        listView_.setAdapter(booksListAdapter_);

        // Set ContextMenu for listView
        registerForContextMenu(listView_);

        listView_.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) listView_.getItemAtPosition(position);
                Book book = LibraryDatabaseHelper.getBook(cursor);
                updateBook(book);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LIBRARY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        menu.setHeaderTitle(R.string.Menu);
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Cursor cursor = (Cursor) listView_.getItemAtPosition(info.position);
        Book book = LibraryDatabaseHelper.getBook(cursor);

        switch (item.getItemId()) {
            case R.id.edit_book:
                updateBook(book);
                break;

            case R.id.delete_book:
                deleteBook(book);
                booksListAdapter_.notifyDataSetChanged();
                break;

            case R.id.share_in_twitter:
                statusBuilder(cursor);
                launchPrompt(getActivity());

            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    /**
     * Set up activity for changing some information about book (title, author etc.)
     */
    public void updateBook(Book book) {
        Intent intent = new Intent(getActivity(), AddBookActivity.class);
        intent.putExtra(EXTRA_ID, book.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void deleteBook(Book book) {
        Uri bookWithIdUri = DatabaseContract.BooksTable.buildBookUri(book.getId());
        // Delete from database with help of Content Provider
        getActivity().getContentResolver().delete(bookWithIdUri, null, null);
    }

    // Returns the typed message
    public String getStatus() {
        return status_;
    }

    public void statusBuilder(Cursor cursor){
        Book book = LibraryDatabaseHelper.getBook(cursor);
        status_ = I_HAVE_BOOK + SPACE + book.getTitle() + SPACE + book.getYear() + SPACE + book.getAuthor() + SPACE + AND_WANT_TO_LET_KNOW;
    }


    // Set up subwindow, where user can type the message
    public void launchPrompt(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptView);

       final TextView info = (TextView)promptView.findViewById(R.id.length_info);

        final EditText inputText = (EditText) promptView.findViewById(R.id.share_input);
        inputText.setText(status_);

        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int accessibleLength = 140 - inputText.getText().toString().length();
                info.setText( Integer.toString(accessibleLength));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!inputText.getText().toString().equalsIgnoreCase("")){
                    int accessibleLength = 140 - inputText.getText().toString().length();
                    if ((accessibleLength<140)&&(accessibleLength>10)) {
                        info.setTextColor(Color.GREEN);
                        info.setText(Integer.toString(accessibleLength));
                    } else {
                        info.setTextColor(Color.RED);
                        info.setText(Integer.toString(accessibleLength));
                    }

                }
            }
        });


        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // get user input and set it to result
                        status_ = (inputText.getText().toString());
                        if (status_.length() > 140) {
                            Toast.makeText(getActivity(), "The length of tweet must be less 140", Toast.LENGTH_LONG).show();
                        } else if (status_.length()>0){
                            logIn();
                        } else Toast.makeText(getActivity(), "Please fill the field", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        // create an alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void logIn() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (!sharedPreferences.getBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN, false)) {
            new TwitterAuthenticateTask().execute();
        } else {
            new TwitterUpdateStatusTask().execute(getStatus());
        }
    }

    class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {

        @Override
        protected void onPostExecute(RequestToken requestToken) {
            if (requestToken != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
                startActivity(intent);
            }
        }

        @Override
        protected RequestToken doInBackground(String... params) {
            return TwitterUtil.getInstance().getRequestToken();
        }
    }


}
