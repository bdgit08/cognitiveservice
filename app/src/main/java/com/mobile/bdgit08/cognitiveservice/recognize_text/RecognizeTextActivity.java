package com.mobile.bdgit08.cognitiveservice.recognize_text;

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
import com.mobile.bdgit08.cognitiveservice.computervision.ResponseStringListener;

public class RecognizeTextActivity extends AppCompatActivity implements ResponseStringListener, ResponseLocationHeader {

    private EditText editTextSubscription;
    private EditText editTextUrl;
    private ImageView imageView;
    private TextView textView;
    private TextView textViewResponseCode;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize_text);
        setTitle("Recognize Text");
        editTextUrl = findViewById(R.id.edittext_urlimage);
        editTextSubscription = findViewById(R.id.edittext_subscription_key);
        Button button = findViewById(R.id.button_analyze);
        textView = findViewById(R.id.textview_result_analyze);
        imageView = findViewById(R.id.imageview_analyze);
        textViewResponseCode = findViewById(R.id.textview_responsecode);
        progressBar = findViewById(R.id.progress_bar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
                if (checkEditText()) {
                    progressBar.setVisibility(View.VISIBLE);
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
        RecognizeText recognizeText = new RecognizeText(urlImage, editTextSubscription.getText().toString(), this);
        recognizeText.execute();

    }

    @Override
    public void getResponseString(String result, String responseCode) {
        progressBar.setVisibility(View.GONE);
        try {
            textView.setText(result);
            textViewResponseCode.setText(responseCode);
        } catch (Exception e) {
            e.printStackTrace();
            textView.setText(responseCode);
        }
    }

    @Override
    public void getLocationHeader(String locationHeader, String responseCode) {
        final Handler handler = new Handler();
        try {
            final RecognizeTextOperation recognizeTextOperation = new RecognizeTextOperation(locationHeader, editTextSubscription.getText().toString(), this);
            // you must add delay operation for execute, if you not , response at RecognizeTextOperation will make status you status waiting
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recognizeTextOperation.execute();
                }
            }, 5000);
            textViewResponseCode.setText(responseCode);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
