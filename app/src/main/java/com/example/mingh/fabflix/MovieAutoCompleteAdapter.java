package com.example.mingh.fabflix;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by MINGH on 3/4/17.
 */

public class MovieAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private final List<String> movies;
    private List<String> filteredMovies;
    private Context context;

    public MovieAutoCompleteAdapter(Context context, List<String> movies) {
        super(context, 0, movies);
        this.movies = movies;
        this.filteredMovies = movies;
        this.context = context;
    }

    @Override
    public int getCount() {
        return filteredMovies.size();
    }

    @Override
    public String getItem(int index) {
        return filteredMovies.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        Filter movieFiler = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                filteredMovies.clear();

                final FilterResults results = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    filteredMovies.addAll(movies);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    String url = "http://35.163.190.44:8080/FabflixMobile/Main?title=" + filterPattern;

                    final Map<String, String> params = new HashMap<String, String>();
                    RequestQueue queue = Volley.newRequestQueue(context);

                    StringRequest getRequest = new StringRequest(Request.Method.GET, url,

                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("response", response);
//                        ((TextView)findViewById(R.id.http_response)).setText(response);
                                    Document doc = null;
                                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                                    try {

                                        DocumentBuilder db = dbf.newDocumentBuilder();

                                        InputSource is = new InputSource();
                                        is.setCharacterStream(new StringReader(response));
                                        doc = db.parse(is);

                                    } catch (ParserConfigurationException e) {
                                        Log.e("Error: ", e.getMessage());
                                    } catch (SAXException e) {
                                        Log.e("Error: ", e.getMessage());
                                    } catch (IOException e) {
                                        Log.e("Error: ", e.getMessage());
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
                                                filteredMovies.add(title);
                                                Log.d("title", title);
                                            }
                                        }
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                    Log.d("security.error", error.toString());
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            return params;
                        }
                    };

                    queue.add(getRequest);

                }
                Log.d("movies", filteredMovies.get(0));

                results.values = filteredMovies;
                results.count = filteredMovies.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count == 0)
                    notifyDataSetInvalidated();
                else {
                    filteredMovies = (List<String>) results.values;
                    notifyDataSetChanged();
                }
            }
        };

        return movieFiler;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item from filtered list.
        String title = filteredMovies.get(position);

        // Inflate your custom row layout as usual.
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.row_movie, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.row_title);
        tvName.setText(title);

        return convertView;
    }
}
