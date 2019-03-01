package com.teamnova.inma06.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.teamnova.inma06.Login.LoginActivity;
import com.teamnova.nova.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 이메일을 인증하는 액티비티 입니다.*/
/* 아이디가 존재하는지 여부를 서버에 요청합니다.
*  서버는 DB에 아이디가 존재하는지를 Register.php 로 판단하여
*  결과값을 리턴합니다. */

public class Register_Second_Activity extends AppCompatActivity {


  /* 서버에서 받아온 결과값이 담깁니다.*/
  private boolean isPassID = false;
  private boolean isPassPW = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.register_activity_second);


    // 유효성 검사 후 출력 될 메시지
    final TextView passIdTv = findViewById(R.id.isPassID_Tv);
    final TextView passPwTv = findViewById(R.id.isPassPW_Tv);
    final TextView passPwConTv = findViewById(R.id.isPassConPW_Tv);
    // 유효성 검사 후 출력 될 메시지


    final EditText userIDEt = (EditText) findViewById(R.id.etID); //ID(Email)
    final EditText userPWEt= (EditText) findViewById(R.id.etPW); // Password
    final EditText userConPWEt = (EditText) findViewById(R.id.etConPW); //Password confirm

    final TextView backBtn = (TextView) findViewById(R.id.backBtn); //뒤로가기 버튼
    final Button certBtn = (Button) findViewById(R.id.btnCert); //인증메일 발송 버튼
    final Button reCertBtn = (Button) findViewById(R.id.btnReCert); //인증메일 발송 버튼
    final Button registerBtn = (Button) findViewById(R.id.btnRegister); // 회원가입 버튼





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

    //인증메일 발송버튼
    certBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(Register_Second_Activity.this, "인증메일클릭", Toast.LENGTH_SHORT).show();
      }
    });

    //회원가입 버튼
    registerBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /*
        * 예외처리
        *
        * 공백, ID 중복체크, PW-ConPW 일치체크
        * 특수문자 사용금지 -> 정규표현식
        * 욕설 필터링
        * 전화번호 한글 입력불가 -> 정규표현식
        * 기수는 숫자로만 입력하기
        *
        * * */
        //회원가입 클릭하면 유저가 입력한 EditText 받아오는 것.
        //서버로 전달될때 사용함.
        String userID = userIDEt.getText().toString();
        String userPW = userPWEt.getText().toString();


        // responseListener 전달 받아서 실행
        // 예외처리 다이얼로그 띄우기 성공시 "확인" -> 로그인 액티비티로 전환
        Response.Listener<String> responseListener = new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try {
              JSONObject jsonResponse = new JSONObject(response);
              boolean success = jsonResponse.getBoolean("success");
              if(success) {
                Toast.makeText(Register_Second_Activity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Register_Second_Activity.this, LoginActivity.class);
                Register_Second_Activity.this.startActivity(intent);
              }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Register_Second_Activity.this);
                builder.setMessage("회원 등록에 실패했습니다.")
                    .setNegativeButton("다시 시도", null)
                    .create()
                    .show();
              }
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }
        };
//        //회원가입 버튼 클릭시 -> 입력값 받아서 서버로 (responseListener) 로 전달
//        RegisterRequest registerRequest = new RegisterRequest(userID, userPW, userName, userNickName,
//            userPhoneNum, userGrade, responseListener);
//        RequestQueue queue = Volley.newRequestQueue(Register_Second_Activity.this);
//        queue.add(registerRequest);
      }
    });







  }
}
