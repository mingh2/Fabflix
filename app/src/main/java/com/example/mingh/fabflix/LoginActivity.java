package com.example.mingh.fabflix;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class LoginActivity extends AppCompatActivity {

    EditText usernameText, passwordText;
    Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextInputLayout usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        final TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);

        usernameWrapper.setHint("Username");
        passwordWrapper.setHint("Password");

        usernameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);
        logInButton = (Button) findViewById(R.id.loginBtn);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    private void login()
    {

        logInButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final Map<String, String> params = new HashMap<String, String>();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = "http://35.163.190.44:8080/FabflixMobile/Login?";
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        url += "username=" + username + "&password=" + password;

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>()
                {
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
                        if(errors.getLength() != 0) {
                            for (int i = 0; i < errors.getLength(); ++i) {
                                Element error = (Element) errors.item(i);
                                Log.e("Error: ", error.getAttribute("name"));
                                Log.e("Error Message: ", error.getElementsByTagName("message").item(0).getTextContent());
                            }
                        }else {
                            NodeList messages = doc.getElementsByTagName("message");
                            final String message = messages.item(0).getFirstChild().getNodeValue();
                            if(message.startsWith("Login Success"))
                            {
                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                logInButton.setEnabled(true);
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                                progressDialog.dismiss();
                                            }
                                        }, 2000);
                            }

                            else {
                                new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            Toast.makeText(getBaseContext(), message.substring(15), Toast.LENGTH_LONG).show();
                                            logInButton.setEnabled(true);
                                            progressDialog.dismiss();
                                        }
                                    }, 2000);

                            }
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("security.error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }
        };

        queue.add(getRequest);

    }


    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if(nl != null && nl.getLength() > 0) {
            Element el = (Element)nl.item(0);
            if(el.getFirstChild() != null) {
                textVal = el.getFirstChild().getNodeValue();
            }
        }

        return textVal;
    }

    private int getIntValue(Element ele, String tagName) {
        try {
            return Integer.parseInt(getTextValue(ele,tagName).trim());
        } catch(NumberFormatException e)
        {
            System.out.println("Invalid Year Information: " + getTextValue(ele,tagName));
            return -1;
        }
    }

    public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(new DOMSource(doc),
                new StreamResult(new OutputStreamWriter(out, "UTF-8")));
    }


}
