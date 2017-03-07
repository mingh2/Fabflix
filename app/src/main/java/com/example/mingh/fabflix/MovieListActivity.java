package com.example.mingh.fabflix;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MovieListActivity extends AppCompatActivity {

    private RecyclerView movieListView;
    private RecyclerView.Adapter movieAdapter;
    private RecyclerView.LayoutManager movieLayoutManager;
    private ArrayList<String> movieList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        movieListView = (RecyclerView) findViewById(R.id.movie_list);

        movieListView.setHasFixedSize(true);

        movieList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        String url = (String)bundle.get("URL");
        Log.d("URL", url);
        try {
            movieList = new FetchMovies().execute(new String[]{url}).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        movieLayoutManager = new LinearLayoutManager(this);
        movieListView.setLayoutManager(movieLayoutManager);

        movieAdapter = new MovieListAdapter(movieList);
        movieListView.setAdapter(movieAdapter);

    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class FetchMovies extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            ArrayList<String> results = new ArrayList<>();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
            Document doc = null;
            try {
                db = dbf.newDocumentBuilder();
                InputStream is = new URL(params[0]).openConnection().getInputStream();
                doc = db.parse(is);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }


            NodeList errors = doc.getElementsByTagName("error");
            if (errors != null && errors.getLength() > 0) {
                for (int i = 0; i < errors.getLength(); ++i) {
                    Element error = (Element) errors.item(i);
                    Log.e("Error: ", error.getAttribute("name"));
                    Log.e("Error Message: ", error.getElementsByTagName("message").item(0).getTextContent());
                }
            } else {
                NodeList movies = doc.getElementsByTagName("movie");
                if(movies != null && movies.getLength() > 0)
                {
                    for(int i = 0; i < movies.getLength(); ++i)
                    {
                        Element movie = (Element)movies.item(i);
                        String title = movie.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
                        results.add(title);
                        Log.d("title", title);
                    }

                }
            }

            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result)
        {
            movieAdapter = new MovieListAdapter(result);
            movieListView.setAdapter(movieAdapter);
        }
    }


}
