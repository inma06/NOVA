package com.teamnova.inma06.CertEmail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.teamnova.nova.R;

public class CertActivity extends AppCompatActivity {

  TextView userEmailTv;
  TextView userPasswordTv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cert);

    userEmailTv = findViewById(R.id.email_TV);

    Intent intent = getIntent();
    String userID = intent.getStringExtra("userID");

    userEmailTv.setText(userID.toString());

  }
}
