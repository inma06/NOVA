package com.teamnova.inma06.Loading;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.teamnova.inma06.Login.LoginActivity;
import com.teamnova.nova.R;

public class LoadingActivity extends Activity {
  ImageView imageView = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_loading);
    imageView = findViewById(R.id.logoIV);
    startLoading();
  }
  private void startLoading() {
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        imageView.setImageResource(R.drawable.loading);
        Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
      }
    }, 5000);
  }

}