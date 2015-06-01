package com.example.admin.personallibrarycatalogue;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

    private final static String TITLE = "Title";
    private final static String AUTHOR = "Author";
    private BooksListAdapter booksListAdapter_;
    private LibraryDatabaseHelper helper_;

    public BooksListActivityFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_books_list, container, false);

        helper_ = new LibraryDatabaseHelper(getActivity());

        List<Book> booksList = new ArrayList<Book>();
        booksList = helper_.getAllBooks();

        final ListView listView = (ListView) rootView.findViewById(R.id.books_list_view);
        booksListAdapter_ = new BooksListAdapter(this.getActivity(), booksList);
        listView.setAdapter(booksListAdapter_);

        // Set ContextMenu for listView
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book) parent.getItemAtPosition(position);
                String author = book.getAuthor();
                String title = book.getTitle();

                Intent intent = new Intent();
                intent.setClass(getActivity(), AddBookActivity.class);

                int returned_id = helper_.getId(title, author);

                intent.putExtra("id",returned_id);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v,menuInfo);
       // if (v.getId() == R.id.book_list_item){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(R.string.Menu);
            menu.add(R.string.edit_book_action);
            menu.add(R.string.delete_book_action);
       // }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
//        int menuItemIndex = item.getItemId();
//      //  String[] menuItems = getResources().getStringArray(R.array.menu);
//       // String menuItemName = menuItems[menuItemIndex];
        switch (item.getItemId())
        {
            case R.string.edit_book_action:
                
                break;

            case R.string.delete_book_action:
                break;

            default:
                return super.onContextItemSelected(item);
        }



        return true;
    }



}
