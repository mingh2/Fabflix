package com.example.mingh.fabflix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MovieListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);


        Bundle bundle = getIntent().getExtras();
        String url = (String)bundle.get("URL");
        Log.d("URL", url);
    }
}
