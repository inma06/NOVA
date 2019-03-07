package com.teamnova.bongapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

  public static ArrayList<UserArrayList> userList = new ArrayList();
  private static EditText etUserID;
  private static EditText etUserPW;
  private static EditText etConfirmPW;
  private static EditText etEmail;
  private static Button btnSignUp;
  private static ConstraintLayout signUpLayout;
  private static InputMethodManager imm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);

    etUserID = (EditText) findViewById(R.id.signUp_ID);
    etUserPW = (EditText) findViewById(R.id.signUp_PW);
    etConfirmPW = (EditText) findViewById(R.id.signUp_confirmPW);
    etEmail = (EditText) findViewById(R.id.signUp_email);
    btnSignUp = (Button) findViewById(R.id.signUp_signUpBtn);
    signUpLayout = (ConstraintLayout) findViewById(R.id.signUpLayout);
    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    //레이아웃을 눌렀을 때 키보드 사라지게 하기
    signUpLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        imm.hideSoftInputFromWindow(signUpLayout.getWindowToken(), 0);
      }
    });

    //이미 회원이십니까? signUp_cancelBtn
    findViewById(R.id.signUp_cancelBtn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);
        SignUp.this.finish();
      }
    });

    etUserID.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        String userID = etUserID.getText().toString();
        for(int i = 0; i < SignUp.userList.size(); i++) {
          if( userID.equals(SignUp.userList.get(i).userID) ) {
            //만약 입력한 userID가 userList 에 이미 존재 한다면
            etUserID.setBackgroundColor(Color.RED);
          } else {
            //존재하지 않는다면 -> 정상
            etUserID.setBackgroundColor(Color.GREEN);
          }

        }
      }
      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    etConfirmPW.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        String password = etUserPW.getText().toString();
        String confirm = etConfirmPW.getText().toString();
        if( password.equals(confirm) ) {
          //만약 비밀번호가 일치하면
          etUserPW.setBackgroundColor(Color.GREEN);
          etConfirmPW.setBackgroundColor(Color.GREEN);
        } else {
          //비밀번호가 일치하지 않으면
          //흔들리는 애니메이션 추가하면 좋을듯
          etUserPW.setBackgroundColor(Color.RED);
          etConfirmPW.setBackgroundColor(Color.RED);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    btnSignUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        // UserID 입력 확인
        if( etUserID.getText().toString().length() == 0) {
          Toast.makeText(SignUp.this, "User name을 확인하세요!", Toast.LENGTH_SHORT).show();
          etUserID.requestFocus();
          return;
        }

        // UserID 중복 확인
        for(int i = 0; i < SignUp.userList.size(); i++) {
          if( etUserID.getText().toString().equals(SignUp.userList.get(i).userID) ){
            Toast.makeText(SignUp.this, "중복된 아이디 입니다", Toast.LENGTH_SHORT).show();
            etUserID.setText("");
            etUserID.requestFocus();
            return;
          }
        }

        // UserPW 입력 확인
        if( etUserPW.getText().toString().length() == 0) {
          Toast.makeText(SignUp.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
          etUserPW.requestFocus();
          return;
        }
        // confirmPW 입력 확인
        if( etConfirmPW.getText().toString().length() == 0) {
          Toast.makeText(SignUp.this, "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();
          etConfirmPW.requestFocus();
          return;
        }
        // e-mail 입력 확인
        if( etEmail.getText().toString().length() == 0) {
          Toast.makeText(SignUp.this, "이메일을 입력하세요!", Toast.LENGTH_SHORT).show();
          etEmail.requestFocus();
          return;
        }
        // e-mail 중복 확인
        for(int i = 0; i < SignUp.userList.size(); i++) {
          if( etEmail.getText().toString().equals(SignUp.userList.get(i).userEmail) ){
            Toast.makeText(SignUp.this, "중복된 이메일 입니다", Toast.LENGTH_SHORT).show();
            etEmail.setText("");
            etEmail.requestFocus();
            return;
          }
        }

        // 비밀번호 일치 확인
        if( !etUserPW.getText().toString().equals(etConfirmPW.getText().toString()) ) {
          Toast.makeText(SignUp.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
          etUserPW.setText("");
          etConfirmPW.setText("");
          etUserPW.requestFocus();
          return;
        }

        userList.add(0, new UserArrayList(
            etUserID.getText().toString(),
            etUserPW.getText().toString(),
            etEmail.getText().toString()));

        Intent result = new Intent();
        result.putExtra("userID", etUserID.getText().toString());
        setResult(RESULT_OK, result);
        finish();
      }
    });
  }
}
