package com.example.admin.personallibrarycatalogue;

import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.personallibrarycatalogue.data.Book;
import com.example.admin.personallibrarycatalogue.data.DatabaseContract;
import com.example.admin.personallibrarycatalogue.data.LibraryDatabaseHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * The fragment is using for adding and editinig book
 */
public class AddBookActivityFragment extends Fragment {

    public final int SELECT_PHOTO = 1;
    public final int WIDTH  = 72;
    public final int HEIGHT = 60;


    @Nullable
    private Integer id_;
    private ImageView imageView_;

    public AddBookActivityFragment newInstance(String title, String author) {
        AddBookActivityFragment fragment = new AddBookActivityFragment();

        // arguments
        Bundle arguments = new Bundle();

        // Using values from string.xml resources
        Resources resources = getResources();
        arguments.putString(String.format(resources.getString(R.string.title_settings)), title);
        arguments.putString(String.format(resources.getString(R.string.author_settings)), author);
        fragment.setArguments(arguments);
        return fragment;
    }

    public AddBookActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_add_book, container, false);
        final LibraryDatabaseHelper helper = new LibraryDatabaseHelper(getActivity());

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            id_ = getArguments().getInt("id");
            Book book = helper.getBookById(id_);
            fillAllFields(rootView, book);
        }

        imageView_ = (ImageView) rootView.findViewById(R.id.cover_image_view);

        // Actions for change cover button
        Button setImageButton = (Button) rootView.findViewById(R.id.set_image_button);

        setImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

                ComponentName activity = photoPickerIntent.resolveActivity(getActivity().getPackageManager());
                photoPickerIntent.setType("image/*");

                if (activity != null) {
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                } else {
                    Toast.makeText(getActivity().getBaseContext(), "There are no activities for such intent",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Actions for ok - button (Checking fields, insert into database, start new activity)
        Button applyButton = (Button) rootView.findViewById(R.id.apply_button);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText authorEditText = (EditText) rootView.findViewById(R.id.author_edit_text);
                String author = authorEditText.getText().toString();

                EditText titleEditText = (EditText) rootView.findViewById(R.id.title_edit_text);
                String title = titleEditText.getText().toString();

                EditText descriptionEditText = (EditText) rootView.findViewById(R.id.description_edit_text);
                String description = descriptionEditText.getText().toString();

                EditText yearEditText = (EditText) rootView.findViewById(R.id.year_edit_text);

                int year = 0;

                if (yearEditText.getText().toString().length()>0) {
                     year = Integer.parseInt(yearEditText.getText().toString());
                }

                EditText isbnEditText = (EditText) rootView.findViewById(R.id.isbn_edit_text);
                String isbn = isbnEditText.getText().toString();

                ImageView coverView = (ImageView) rootView.findViewById(R.id.cover_image_view);

                Bitmap source = ((BitmapDrawable)coverView.getDrawable()).getBitmap();
                Bitmap image = Bitmap.createScaledBitmap(source,WIDTH,HEIGHT,true);

                byte[] cover = Util.getBytesFromBitmap(image);

                if (author.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.missing_author_warning), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (title.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.missing_title_warning), Toast.LENGTH_SHORT).show();
                    return;
                }

                Book book = new Book(id_, title, author, description, cover, year, isbn);

                // in case user edit information just update data
                if (id_ != null) {

                    Uri bookWithIdUri = DatabaseContract.BooksTable.buildBookUri(id_);
                    ContentValues values = LibraryDatabaseHelper.createBooksValues(book);
                    getActivity().getContentResolver().update(bookWithIdUri, values, null, null);

                } else {

                    Uri bookWithIdUri = DatabaseContract.BooksTable.CONTENT_URI;
                    ContentValues values = LibraryDatabaseHelper.createBooksValues(book);
                    getActivity().getContentResolver().insert(bookWithIdUri, values);
                }

                Intent intent = new Intent(getActivity(), BooksListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }
        });

        // Actions for cancel button (clear fields and leave activity)
        Button cancelButton = (Button) rootView.findViewById(R.id.cancel);
       cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return rootView;
    }
   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == getActivity().RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        imageView_.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private void fillAllFields(View view, int id) {


        Uri bookWithIdUri = DatabaseContract.BooksTable.buildBookUri(id);
        Cursor cursor = getActivity().getContentResolver().query(bookWithIdUri,
                null,
                bookSelectionQuery,
                new String[]{id_.toString()},
                null);

        Book book = LibraryDatabaseHelper.getBook(cursor);

        EditText titleEditText = (EditText) view.findViewById(R.id.title_edit_text);
        titleEditText.setText(book.getTitle());

        EditText authorEditText = (EditText) view.findViewById(R.id.author_edit_text);
        authorEditText.setText(book.getAuthor());

        EditText descriptionEditText = (EditText) view.findViewById(R.id.description_edit_text);
        descriptionEditText.setText(book.getDescription());

        EditText yearEditText = (EditText)view.findViewById(R.id.year_edit_text);
        yearEditText.setText(Integer.toString(book.getYear()));

        EditText isbnEditText = (EditText)view.findViewById(R.id.isbn_edit_text);
        isbnEditText.setText(book.getIsbn());

        ImageView coverView = (ImageView) view.findViewById(R.id.cover_image_view);
        coverView.setImageBitmap(Util.getBitmapFromBytes(book.getCover()));

    }
