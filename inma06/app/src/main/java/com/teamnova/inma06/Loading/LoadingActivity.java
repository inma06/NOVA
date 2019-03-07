package com.teamnova.inma06.Loading;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.teamnova.inma06.Login.LoginActivity;
import com.teamnova.nova.R;

public class LoadingActivity extends Activity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = new Intent(this, LoginActivity.class);
    intent.putExtra("state","launch");
    startActivity(intent);
    finish();

  }

}
