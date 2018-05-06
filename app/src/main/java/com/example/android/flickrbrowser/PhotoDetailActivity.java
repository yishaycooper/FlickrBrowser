package com.example.android.flickrbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        // uses the method in BaseActivity to enable toolbar home button
        activateToolbar(true);

        Intent intent = getIntent();// the Intent that started this cativity
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);// we have to use tho appropriate get method
        if (photo != null) {
            TextView photoTitile = (TextView) findViewById(R.id.photo_title);

//  photoTitile.setText("Title: " + photo.getTitle());
// instead of doing the obove use placeholders. the getString replaces any placeholders in the strings resouces
// with the value in this cace photo.getTitle()
            Resources resources = getResources();
            String text = resources.getString(R.string.photo_title_text, photo.getTitle());
            TextView photoTags = (TextView) findViewById(R.id.photo_tags);

            photoTags.setText(resources.getString(R.string.photo_tags_text, photo.getTags()));
//            photoTags.setText("Tags: " + photo.getTags());

            TextView photoAuthor = (TextView) findViewById(R.id.photo_author);
            photoAuthor.setText(photo.getAuthor());

            ImageView photoImage = (ImageView) findViewById(R.id.photo_image);
            Picasso.with(this).load(photo.getLink())//pass this as a context
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(photoImage);
        }
    }

}
