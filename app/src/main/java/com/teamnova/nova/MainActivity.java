package com.teamnova.nova;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    TextView userIDTv = (TextView) findViewById(R.id.idTv);
    TextView userPWTv = (TextView) findViewById(R.id.pwTv);
    TextView welcomeMsg = (TextView) findViewById(R.id.welcomeMessageTv);

    Intent intent = getIntent();
    String userID = intent.getStringExtra("userID");
    String userPW = intent.getStringExtra("userPW");
    String message = "환영합니다, " + userID + "님!";

    userIDTv.setText(userID);
    userPWTv.setText(userPW);
    welcomeMsg.setText(message);

  }
}
