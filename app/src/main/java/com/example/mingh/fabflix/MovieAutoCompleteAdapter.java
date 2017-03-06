package com.example.mingh.fabflix;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by MINGH on 3/4/17.
 */

public class MovieAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private static final int MAX_RESULTS = 10;
    private Context context;
    private List<String> filteredMovies;


    public MovieAutoCompleteAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        filteredMovies = new ArrayList<String>();
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
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                if (constraint != null && constraint.length() != 0) {
                    final String filterPattern = constraint.toString().toLowerCase().trim();

                    String url = null;
                    try {
                        url = "http://35.163.190.44:8080/FabflixMobile/Main?title=" + URLEncoder.encode(filterPattern,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.d("URL", url);
                    final Map<String, String> param = new HashMap<String, String>();
                    RequestQueue queue = Volley.newRequestQueue(context);

                    StringRequest getRequest = new StringRequest(Request.Method.GET, url,

                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    filteredMovies.clear();

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
                                            for(int i = 0; i < movies.getLength(); ++i)
                                            {
                                                Log.d("filterMovie", filteredMovies.get(i));
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
                            return param;
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
                if(results != null && results.count > 0){
                    notifyDataSetChanged();
                }else{
                    notifyDataSetInvalidated();
                }
            }
        };

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.row_movie,parent,false);

        String title = filteredMovies.get(position);

        TextView titleTv = (TextView) view.findViewById(R.id.row_title);

        titleTv.setText(title);

        return view;

    }


}
