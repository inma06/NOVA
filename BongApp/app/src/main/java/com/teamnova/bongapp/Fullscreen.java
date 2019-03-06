package com.teamnova.bongapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Fullscreen extends AppCompatActivity {
  private ImageView fullScreen;
  private String str;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fullscreen);

    fullScreen = (ImageView) findViewById(R.id.fullScreenImage);
    Intent intent = new Intent();
    str = intent.getStringExtra("mainThumb");

    Glide.with(this)
        .load(str)
        .into(fullScreen);

  }
}
