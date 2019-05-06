package com.mobile.bdgit08.cognitiveservice.ocr;

import android.os.AsyncTask;
import android.util.Log;

import com.mobile.bdgit08.cognitiveservice.computervision.ResponseStringListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

public class OpticalCharacterRecognition extends AsyncTask<String, Void, String> {
    private static final String TAG = "OpticalCharacterRecogni";
    private String urlImage;
    private String stringJson;
    private String responseCode;
    private String subscriptionKey = "<Your Subscription Key>";
    private String uriBase = "https://southeastasia.api.cognitive.microsoft.com/vision/v2.0/recognizeText";
    private ResponseLocationHeader listener;

    public OpticalCharacterRecognition(String urlImage, String subscriptionKey, ResponseLocationHeader listener) {
        this.urlImage = urlImage;
        this.listener = listener;
        this.subscriptionKey = subscriptionKey;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpClient httpclient = HttpClients.createDefault();
        try {
            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters. All of them are optional.
            builder.setParameter("mode", "Printed");

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
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            responseCode = "Response Code : " + response.getStatusLine().getStatusCode();
            String headers = Arrays.toString(response.getHeaders("Operation-Location"));
            headers = headers.substring(headers.indexOf("https:")).replace("]","");
            Log.d("header", "doInBackground: "+headers);
            String [] splitString = headers.split("/");
            Log.d(TAG, "doInBackground: split"+splitString[splitString.length -1]);
            stringJson = headers;
        } catch (Exception e) {
            // Display error message.
            e.printStackTrace();
            if (e instanceof IOException) {
                return "{\"error\":\"Connections Lost!!\"}";
            }
        }
        return stringJson;
    }

    @Override
    protected void onPostExecute(String stringJson) {
        if (listener != null)
            listener.getLocationHeader(stringJson, responseCode);
    }
}
