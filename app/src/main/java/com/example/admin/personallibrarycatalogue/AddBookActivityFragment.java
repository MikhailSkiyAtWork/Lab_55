package com.example.admin.personallibrarycatalogue;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
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
    public final int WIDTH = 72;
    public final int HEIGHT = 60;
    private ImageView imageView_;
    private EditText authorEditText_;
    private EditText titleEditText_;
    private EditText descriptionEditText_;
    private EditText yearEditText_;
    private EditText isbnEditText_;
    private ImageView coverView_;
    @Nullable
    private Integer id_;


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

        authorEditText_ = (EditText) rootView.findViewById(R.id.author_edit_text);
        titleEditText_ = (EditText) rootView.findViewById(R.id.title_edit_text);
        descriptionEditText_ = (EditText) rootView.findViewById(R.id.description_edit_text);
        yearEditText_ = (EditText) rootView.findViewById(R.id.year_edit_text);
        isbnEditText_ = (EditText) rootView.findViewById(R.id.isbn_edit_text);
        coverView_ = (ImageView) rootView.findViewById(R.id.cover_image_view);

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            id_ = getArguments().getInt("id");
            fillAllFields(rootView, id_);
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
                    Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.no_apropriate_apps_msg),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Actions for ok - button (Checking fields, insert into database, start new activity)
        Button applyButton = (Button) rootView.findViewById(R.id.apply_button);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String author = authorEditText_.getText().toString();
                String title = titleEditText_.getText().toString();
                String description = descriptionEditText_.getText().toString();

                int year = 0;
                try {
                    year = Integer.parseInt(yearEditText_.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Input is not an integer!", Toast.LENGTH_SHORT).show();
                }

                String isbn = isbnEditText_.getText().toString();

                Bitmap source = ((BitmapDrawable) coverView_.getDrawable()).getBitmap();
                byte[] cover = null;
                if (source!=null) {
                    Bitmap image = Bitmap.createScaledBitmap(source, WIDTH, HEIGHT, true);
                    cover = Util.getBytesFromBitmap(image);
                }



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
                getActivity().finish();

            }
        });

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
        Book book = ContentResolverHelper.getBook(getActivity(), id);
        titleEditText_.setText(book.getTitle());
        authorEditText_.setText(book.getAuthor());
        descriptionEditText_.setText(book.getDescription());
        yearEditText_.setText(Integer.toString(book.getYear()));
        isbnEditText_.setText(book.getIsbn());
        coverView_.setImageBitmap(Util.getBitmapFromBytes(book.getCover()));
    }
}
