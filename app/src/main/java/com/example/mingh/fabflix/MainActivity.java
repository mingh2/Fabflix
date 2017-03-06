package com.example.mingh.fabflix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

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

        tvMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String title = adapter.getItem(position);
                tvMovie.setText(title);
            }
        });
//

    }

}
