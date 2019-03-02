package com.teamnova.inma06.Register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teamnova.inma06.Login.LoginActivity;
import com.teamnova.nova.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/* 회원가입 액티비티 입니다.
*
*  이메일과 패스워드를 바르게 입력합니다
*  다음버튼을 누르면 이메일을 인증하는 액티비티가 나옵니다.
*  */

public class RegisterActivity extends AppCompatActivity {
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
    final TextView alreadyIDTv = (TextView) findViewById(R.id.backBtn);
    final Button nextBtn = (Button) findViewById(R.id.nextBtn);

    //이미 가입하셨나요? 클릭시 로그인화면으로 가기
    alreadyIDTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
      }
    });

    /* 다음 버튼을 누르면 이메일 인증하기 액티비티가 나옵니다. */
    nextBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d("회원가입 첫화면", "onClick: 성공1 ");
        Intent intent = new Intent(RegisterActivity.this, Register_Second_Activity.class);
        Log.d("회원가입 첫화면", "onClick: 성공2 ");
        startActivity(intent);
        Log.d("회원가입 첫화면", "onClick: 성공3 ");
        Toast.makeText(RegisterActivity.this, "이메일을 인증 하세요!", Toast.LENGTH_SHORT).show();
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
