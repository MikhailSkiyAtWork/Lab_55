package com.example.admin.personallibrarycatalogue;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.admin.personallibrarycatalogue.data.Book;
import com.example.admin.personallibrarycatalogue.data.LibraryDatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A placeholder fragment containing a simple view.
 */
public class BooksListActivityFragment extends Fragment {

    private BooksListAdapter booksListAdapter_;

    public BooksListActivityFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_books_list, container, false);

        LibraryDatabaseHelper helper = new LibraryDatabaseHelper(getActivity());

        List<Book> booksList = new ArrayList<Book>();
        booksList = helper.getAllBooks();

        final ListView listView = (ListView) rootView.findViewById(R.id.books_list_view);
        booksListAdapter_ = new BooksListAdapter(this.getActivity(), booksList);
        listView.setAdapter(booksListAdapter_);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book)parent.getItemAtPosition(position);
                String author = book.getAuthor();
                String title = book.getTitle();

                Intent intent = new Intent();
                intent.setClass(getActivity(),AddBookActivity.class);
                intent.putExtra("Title", title);
                intent.putExtra("Author", author);

                startActivity(intent);
            }
        });


        return rootView;
    }
}
