package com.mobile.bdgit08.cognitiveservice.ocr;

import android.os.AsyncTask;

import com.mobile.bdgit08.cognitiveservice.computervision.ResponseStringListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecognizeTextOperation extends AsyncTask<String , Void , String> {
    private String TAG = RecognizeTextOperation.class.getSimpleName();
    private String operationLocation;
    private String stringJson = "";
    private String responseCode;
    private String subscriptionKey = "<Your Subscription Key>";
    private ResponseStringListener listener;
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 1000000;
    public static final int CONNECTION_TIMEOUT = 15000;

    RecognizeTextOperation(String operationLocation, String subscriptionKey, ResponseStringListener listener) {
        this.subscriptionKey = subscriptionKey;
        this.operationLocation = operationLocation;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        String inputLine;
        try {

            URL myUrl = new URL(operationLocation);
            //Create a connection
            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();
            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);

            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());
            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            //Check if the line we are reading is not null
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            //Close our InputStream and Buffered reader
            stringJson = stringBuilder.toString();
            reader.close();
            streamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof IOException) {
                return "{\"error\":\"Connections Lost!!\"}";
            }
        }
        return stringJson;
    }

    @Override
    protected void onPostExecute(String s) {
        if (listener != null)
            listener.getResponseString(stringJson, responseCode);
    }


}
