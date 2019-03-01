package com.teamnova.inma06.Register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.teamnova.inma06.Login.LoginActivity;
import com.teamnova.nova.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Register_First_Activity extends AppCompatActivity {
  /* 이메일 패스워드 유효성 검사 #주의 : 이메일 중복검사 아님 */
  /* 가입시 아이디와 패스워드를 올바르게 입력했는지 */
  private boolean isPassID = false;
  private boolean isPassPW = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.register_activity_first);

    final EditText userIDEt = (EditText) findViewById(R.id.etID); //ID는 이메일
    final EditText userPWEt= (EditText) findViewById(R.id.etPW);
    final EditText userConPWEt = (EditText) findViewById(R.id.etConPW); //비밀번호 확인
    final TextView alreadyIDTv = (TextView) findViewById(R.id.isAlreadyID_Tv);

    //이미 가입하셨나요? 클릭시 로그인화면으로 가기
    alreadyIDTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Register_First_Activity.this, LoginActivity.class);
        startActivity(intent);
      }
    });


    userIDEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        String mailFormat = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        if (hasFocus == false) {
          String inputText = userIDEt.getText().toString();
          Pattern pattern = Pattern.compile(mailFormat);
          Matcher matcher = pattern.matcher(inputText);
          if (!matcher.matches()) {
            Log.d("TEST", "이메일이 맞지 않습니다.");
            isPassID = false;
          }else {
            Log.d("TEST", "올바른 이메일 입니다.");
          }
        }
      }
    });
  }
}
