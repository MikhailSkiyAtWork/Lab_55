package com.example.admin.personallibrarycatalogue;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
 * A placeholder fragment containing a simple view.
 */
public class AddBookActivityFragment extends Fragment {

    private ImageView imageView_;
    private final int SELECT_PHOTO = 1;

    public AddBookActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_book, container, false);

        imageView_ = (ImageView) rootView.findViewById(R.id.imageView);
        Button setImageButton = (Button) rootView.findViewById(R.id.set_image_button);

        final LibraryDatabaseHelper helper = new LibraryDatabaseHelper(getActivity());
        final SQLiteDatabase database = helper.getWritableDatabase();

        setImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        Button applyButton = (Button) rootView.findViewById(R.id.apply_button);

        applyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                EditText authorEditText = (EditText)rootView.findViewById(R.id.author_edit_text);
                String author = authorEditText.getText().toString();

                EditText titleEditText = (EditText)rootView.findViewById(R.id.title_edit_text);
                String title = titleEditText.getText().toString();

                EditText descriptionEditText = (EditText)rootView.findViewById(R.id.description_edit_text);
                String description = descriptionEditText.getText().toString();

                ImageView coverView = (ImageView)rootView.findViewById(R.id.imageView);
                byte[] cover = Util.getBytesFromDrawable(coverView.getDrawable());

                if (author.matches("")) {
                    Toast.makeText(getActivity(), "Please enter the author field", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (title.matches("")) {
                    Toast.makeText(getActivity(), "Please enter the title field", Toast.LENGTH_SHORT).show();
                    return;
                }


                Book book = new Book(title,author,description,cover);

                helper.addBook(book);

                Intent intent = new Intent(getActivity(),BooksListActivity.class);
                startActivity(intent);

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
}
