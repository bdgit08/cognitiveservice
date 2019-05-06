package com.mobile.bdgit08.cognitiveservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mobile.bdgit08.cognitiveservice.computervision.ComputerVisionActivity;
import com.mobile.bdgit08.cognitiveservice.recognize_text.RecognizeTextActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_analyze:
                Intent intent = new Intent(getApplicationContext(),ComputerVisionActivity.class);
                startActivity(intent);
                break;
            case R.id.button_optical_text_recognition :
                Intent intent2 = new Intent(getApplicationContext(), RecognizeTextActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
