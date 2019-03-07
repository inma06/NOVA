package com.teamnova.inma06.Loading;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.teamnova.nova.R;

public class Loading extends AppCompatActivity {

  ImageView logoIV;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_loading);

    logoIV = findViewById(R.id.logoIV);


  }
}
