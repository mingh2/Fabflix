//package com.example.mingh.fabflix;
//
//import android.content.Context;
//import android.util.Log;
//import android.widget.Filter;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//
//import java.io.IOException;
//import java.io.StringReader;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//
///**
// * Created by MINGH on 3/4/17.
// */
//
//public class MovieFilter extends Filter {
//
//    MovieAutoCompleteAdapter adapter;
//    List<String> originalList;
//    List<String> filteredList;
//    Context context;
//
//    public MovieFilter(MovieAutoCompleteAdapter adapter, List<String> originalList, Context context) {
//        super();
//        this.adapter = adapter;
//        this.originalList = originalList;
//        this.filteredList = new ArrayList<>();
//        this.context = context;
//    }
//
//    @Override
//    protected FilterResults performFiltering(CharSequence constraint) {
//        filteredList.clear();
//        final FilterResults results = new FilterResults();
//
//        if (constraint == null || constraint.length() == 0) {
//            filteredList.addAll(originalList);
//        } else {
//            final String filterPattern = constraint.toString().toLowerCase().trim();
//            String url = "http://35.163.190.44:8080/FabflixMobile/Main?title=" + filterPattern;
//
//            final Map<String, String> params = new HashMap<String, String>();
//            RequestQueue queue = Volley.newRequestQueue(context);
//
//            // Your filtering logic goes in here
////            for (final Dog dog : originalList) {
////                if (dog.breed.toLowerCase().contains(filterPattern)) {
////                    filteredList.add(dog);
////                }
////            }
//            StringRequest getRequest = new StringRequest(Request.Method.GET, url,
//
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//                            Log.d("response", response);
////                        ((TextView)findViewById(R.id.http_response)).setText(response);
//                            Document doc = null;
//                            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//                            try {
//
//                                DocumentBuilder db = dbf.newDocumentBuilder();
//
//                                InputSource is = new InputSource();
//                                is.setCharacterStream(new StringReader(response));
//                                doc = db.parse(is);
//
//                            } catch (ParserConfigurationException e) {
//                                Log.e("Error: ", e.getMessage());
//                            } catch (SAXException e) {
//                                Log.e("Error: ", e.getMessage());
//                            } catch (IOException e) {
//                                Log.e("Error: ", e.getMessage());
//                            }
//
//                            NodeList errors = doc.getElementsByTagName("error");
//                            if (errors != null && errors.getLength() > 0) {
//                                for (int i = 0; i < errors.getLength(); ++i) {
//                                    Element error = (Element) errors.item(i);
//                                    Log.e("Error: ", error.getAttribute("name"));
//                                    Log.e("Error Message: ", error.getElementsByTagName("message").item(0).getTextContent());
//                                }
//                            } else {
//                                NodeList movies = doc.getElementsByTagName("movie");
//                                if(movies != null && movies.getLength() > 0)
//                                {
//                                    for(int i = 0; i < movies.getLength(); ++i)
//                                    {
//                                        Element movie = (Element)movies.item(i);
//                                        String title = movie.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
//                                        filteredList.add(title);
//                                        Log.d("title", title);
//                                    }
//                                }
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            // error
//                            Log.d("security.error", error.toString());
//                        }
//                    }
//            ) {
//                @Override
//                protected Map<String, String> getParams() {
//                    return params;
//                }
//            };
//
//            queue.add(getRequest);
//
//        }
//
//        results.values = filteredList;
//        results.count = filteredList.size();
//        return results;
//    }
//
//    @Override
//    protected void publishResults(CharSequence constraint, FilterResults results) {
//        if (results.count == 0)
//            adapter.notifyDataSetInvalidated();
//        else {
//            adapter.filteredMovies = (List<String>) results.values;
//            adapter.notifyDataSetChanged();
//        }
//    }
//}
//
