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
    TextView userIDTv = (TextView) findViewById(R.id.id_Tv);
    TextView userPWTv = (TextView) findViewById(R.id.pw_Tv);
    TextView userNameTv = (TextView) findViewById(R.id.name_Tv);
    TextView userNickNameTv = (TextView) findViewById(R.id.nickName_Tv);
    TextView userPhoneNumTv = (TextView) findViewById(R.id.phoneNum_Tv);
    TextView userGradeTv = (TextView) findViewById(R.id.grade_Tv);

    Intent intent = getIntent();
    String userID = intent.getStringExtra("userID");
    String userPW = intent.getStringExtra("userPW");
    String userName = intent.getStringExtra("userName");
    String userNickName = intent.getStringExtra("userNickName");
    String userPhoneNum = intent.getStringExtra("userPhoneNum");
    String userGrade = intent.getStringExtra("userGrade");

    userIDTv.setText("아이디 -> " + userID);
    userPWTv.setText("패스워드 -> " + userPW);
    userNameTv.setText("이름 -> " + userName);
    userNickNameTv.setText("닉네임 -> " + userNickName);
    userPhoneNumTv.setText("연락처 -> " + userPhoneNum);
    userGradeTv.setText("기수 -> " + userGrade + "기");

  }
}
