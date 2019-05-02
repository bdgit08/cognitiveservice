package com.mobile.bdgit08.cognitiveservice.computervision;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URI;

public class ComputerVision extends AsyncTask<String, Void, String> {
    private String urlImage;
    private String stringJson;
    private String subscriptionKey = "<Your Subscription Key>";
    private static final String uriBase = "https://southeastasia.api.cognitive.microsoft.com/vision/v2.0/analyze";
    private ResponseStringListener listener;


    public ComputerVision(String urlImage, String subscriptionKey,ResponseStringListener listener) {
        this.urlImage = urlImage;
        this.listener = listener;
        this.subscriptionKey = subscriptionKey;
    }

    @Override
    protected String doInBackground(String... strings) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters. All of them are optional.
            builder.setParameter("visualFeatures", "Categories,Description");
            builder.setParameter("language", "en");

            // Prepare the URI for the REST API method.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            StringEntity requestEntity =
                    new StringEntity("{\"url\":\"" + urlImage + "\"}");
            request.setEntity(requestEntity);

            // Call the REST API method and get the response entity.
            HttpResponse response = httpClient.execute(request);
            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Format and display the JSON response.
                stringJson = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(stringJson);
            }
        } catch (Exception e) {
            // Display error message.
            e.printStackTrace();
        }
        return stringJson;
    }

    @Override
    protected void onPostExecute(String s) {
        if (listener != null)
            listener.getResponseString(s);
    }
}
