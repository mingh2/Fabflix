package com.example.mingh.fabflix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> movies = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AutoCompleteTextView tvMovie = (AutoCompleteTextView) findViewById(R.id.autocompleteView);
        final MovieAutoCompleteAdapter adapter = new MovieAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line);

        tvMovie.setAdapter(adapter);


//        tvMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                String title = adapter.getItem(position);
//                tvMovie.setText(title);
//            }
//        });
//
        tvMovie.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Intent intent = new Intent(getApplicationContext(), MovieListActivity.class);
                    try {
                        intent.putExtra("URL", "http://35.163.190.44:8080/FabflixMobile/MovieList?title=" +
                                URLEncoder.encode(tvMovie.getText().toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return false;
                    }

                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });

    }

}
