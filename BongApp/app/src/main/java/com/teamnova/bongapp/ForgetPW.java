package com.teamnova.bongapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgetPW extends AppCompatActivity {

  private static Button btnForgetPW;
  private static Button btnSendEmail;
  private static EditText etUserEmail;
  private static ConstraintLayout btnCancel;
  private static String userID;
  private static String userPW;
  private static String userEmail;
  private static Boolean isTrueEmail;
  private static InputMethodManager imm;
  private static ConstraintLayout forgetLayout;



  //뒤로가기 눌렀을때 로그인 액티비티로 이동
  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Intent intent = new Intent(ForgetPW.this, Login.class);
    startActivity(intent);
    ForgetPW.this.finish();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forget_pw);

    forgetLayout = (ConstraintLayout) findViewById(R.id.forgetLayout);
    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
    .permitDiskReads()
    .permitDiskWrites()
    .permitNetwork().build());
    isTrueEmail = false;
    btnSendEmail = (Button) findViewById(R.id.forgetPW_btnSendEmail);
    btnForgetPW = (Button) findViewById(R.id.forgetPW_btnOK);
    btnCancel = (ConstraintLayout) findViewById(R.id.forgetPW_cancelBtn);
    etUserEmail = (EditText) findViewById(R.id.forgetPW_etUserEmail);

    /*
    * 회원 리스트에서 일치하는 Email 이 있는지 검사한다.
    * 일치하는 Email 이 존재한다면 해당 Email 로 회원정보( ID / PW )를 발송한다.
    *
    * */

    //레이아웃 눌렀을 때 키보드 사라지게
    forgetLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        imm.hideSoftInputFromWindow(forgetLayout.getWindowToken(), 0);
      }
    });

    //취소버튼(로그인 화면으로) 눌렀을 때
    btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ForgetPW.this, Login.class);
        startActivity(intent);
        finish();
      }
    });

    //이메일 발송 버튼 눌렀을 때
    btnSendEmail.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(isTrueEmail){
          GMailSender sender = new GMailSender("pakbongho@gmail.com", "elqkfel0608<?>");
          try {
            sender.sendMail( getPackageName()+"에서 보낸 회원정보 찾기 메일입니다",
                "아이디 : " + userID + "\n" +
                    "비밀번호: " + userPW + "\n" +
                    "감사합니다",
                "pakbongho@gmail.com",
                userEmail);
            isTrueEmail = false;
            userID = "";
            userPW = "";
            userEmail = "";
            etUserEmail.setText("");
            Toast.makeText(ForgetPW.this, "이메일이 정상적으로 발송되었습니다.", Toast.LENGTH_SHORT).show();
          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          Toast.makeText(ForgetPW.this, "이메일을 확인 하세요", Toast.LENGTH_SHORT).show();
        }
      }
    });

    //이메일 확인 버튼 눌렀을 때
    btnForgetPW.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        for(int i = 0; i < SignUp.userList.size(); i++) {
          userID = "";
          userPW = "";
          userEmail = "";
          Log.e(Integer.toString(i) + "의 아이디", SignUp.userList.get(i).userID );
          Log.e(Integer.toString(i) + "의 비밀번호", SignUp.userList.get(i).userPW );
          Log.e(Integer.toString(i) + "의 이메일", SignUp.userList.get(i).userEmail );
          if (etUserEmail.getText().toString().equals(SignUp.userList.get(i).userEmail)) {
            //일치하는 Email 이 있을 경우
            userID = "";
            userPW = "";
            userEmail = "";

            userID = SignUp.userList.get(i).getUserID();
            userPW = SignUp.userList.get(i).getUserPW();
            userEmail = SignUp.userList.get(i).getUserEmail();
            isTrueEmail = true;
            Toast.makeText(ForgetPW.this, "이메일이 확인되었습니다.", Toast.LENGTH_SHORT).show();
            return;
          }
        }
        Toast.makeText(ForgetPW.this, "올바르지 않은 이메일 입니다.", Toast.LENGTH_SHORT).show();
        etUserEmail.setText("");
        etUserEmail.requestFocus();
      }
    });
  }
}
