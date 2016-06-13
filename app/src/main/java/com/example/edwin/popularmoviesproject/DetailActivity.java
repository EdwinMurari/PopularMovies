package com.example.edwin.popularmoviesproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        String posterURL = intent.getStringExtra("imageurl");
        String synopsis = intent.getStringExtra("overview");
        double rating = intent.getDoubleExtra("rating", 0.0);
        String release = intent.getStringExtra("release");

        TextView movieTitle = (TextView) findViewById(R.id.title_textview);
        movieTitle.setText(title);

        ImageView imageView = (ImageView) findViewById(R.id.moviePoster);
        Picasso.with(getApplicationContext()).load(posterURL).into(imageView);

        TextView overview = (TextView) findViewById(R.id.synopsis_text);
        overview.setText(synopsis);

        TextView ratingTV = (TextView) findViewById(R.id.rating_text);
        ratingTV.setText(Double.toString(rating));

        TextView releaseTV = (TextView) findViewById(R.id.release_text);
        releaseTV.setText(release);
    }
}
