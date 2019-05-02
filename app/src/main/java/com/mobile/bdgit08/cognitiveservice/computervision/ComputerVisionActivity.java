package com.mobile.bdgit08.cognitiveservice.computervision;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mobile.bdgit08.cognitiveservice.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ComputerVisionActivity extends AppCompatActivity implements ResponseStringListener{
    private EditText editTextSubscription;
    private EditText editTextUrl;
    private ImageView imageView;
    private TextView textView;
    private TextView textViewResponseCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_vision);
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
        ComputerVision computerVision = new ComputerVision(urlImage, editTextSubscription.getText().toString(), this);
        computerVision.execute();
    }

    @Override
    public void getResponseString(String responseJson, String responseCode) {
        try {
            JSONObject json = new JSONObject(responseJson);
            textView.setText(json.toString());
            textViewResponseCode.setText(responseCode);
        } catch (JSONException e) {
            e.printStackTrace();
            textView.setText(responseCode);
        }
    }
}
