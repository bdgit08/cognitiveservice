package com.mobile.bdgit08.cognitiveservice.analyze_image;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mobile.bdgit08.cognitiveservice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnalyzeImageActivity extends AppCompatActivity implements ResponseStringListener {
    private EditText editTextSubscription;
    private EditText editTextUrl;
    private ImageView imageView;
    private TextView textView;
    private TextView textViewResponseCode;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_image);
        setTitle("Analyze Image");
        progressBar = findViewById(R.id.progress_bar);
        editTextUrl = findViewById(R.id.edittext_urlimage);
        editTextSubscription = findViewById(R.id.edittext_subscription_key);
        Button button = findViewById(R.id.button_analyze);
        textView = findViewById(R.id.textview_result_analyze);
        imageView = findViewById(R.id.imageview_analyze);
        textViewResponseCode = findViewById(R.id.textview_responsecode);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEditText()) {
                    progressBar.setVisibility(View.VISIBLE);
                    textView.setText("");
                    setImage(editTextUrl.getText().toString());
                } else {
                    checkEditTextEmpty(editTextUrl);
                    checkEditTextEmpty(editTextSubscription);
                }
            }
        });
    }

    private void checkEditTextEmpty(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError("Cant be Empty");
        }
    }

    private Boolean checkEditText() {
        return !editTextSubscription.getText().toString().trim().isEmpty() && !editTextUrl.getText().toString().trim().isEmpty();
    }

    private void setImage(final String urlImage) {
        final Handler handler = new Handler();
        Glide.with(imageView).load(urlImage).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                analyzeImage(urlImage);
                return false;
            }

        }).into(imageView);
    }

    private void analyzeImage(String urlImage) {
        AnalyzeImage analyzeImage = new AnalyzeImage(urlImage, editTextSubscription.getText().toString(), this);
        analyzeImage.execute();
    }

    @Override
    public void getResponseString(String responseJson, String responseCode) {
        progressBar.setVisibility(View.GONE);
        try {
            setObjectAnalyze(responseJson);
            textViewResponseCode.setText(responseCode);
        } catch (Exception e) {
            e.printStackTrace();
            textView.setText(responseCode);
        }
    }

    private void setObjectAnalyze(String object) throws JSONException {
        JSONObject jsonObject = new JSONObject(object);
        StringBuilder stringBuilder = new StringBuilder();
        if (jsonObject.getJSONArray("categories").length() > 0) {
            stringBuilder.append("Name     : " + jsonObject.getJSONArray("categories").getJSONObject(0).getString("name") + "\n\n");
            if (jsonObject.getJSONArray("categories").getJSONObject(0).getJSONObject("detail").getJSONArray("celebrities").length() > 0)
                stringBuilder.append("Headliner/Celebrity      : " + jsonObject.getJSONArray("categories").getJSONObject(0).getJSONObject("detail").getJSONArray("celebrities").getJSONObject(0).getString("name") + "\n\n");
        }
        if (jsonObject.getJSONArray("faces").length() > 0) {
            stringBuilder.append("Age       : " + jsonObject.getJSONArray("faces").getJSONObject(0).getString("age") + "\n\n");
            stringBuilder.append("Gender    : " + jsonObject.getJSONArray("faces").getJSONObject(0).getString("gender") + "\n\n");
        }
        if (jsonObject.getJSONArray("objects").length() > 0)
            stringBuilder.append("Object    : " + jsonObject.getJSONArray("objects").getJSONObject(0).getString("object") + "\n\n");
        if (jsonObject.getJSONArray("brands").length() > 0)
            stringBuilder.append("Brand     : " + jsonObject.getJSONArray("brands").getJSONObject(0).getString("name") + "\n\n");
        stringBuilder.append("[ JSON ] " + "\n\n" + object + "\n\n");
        textView.setText(stringBuilder);
    }
}
