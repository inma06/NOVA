package com.teamnova.inma06.Login;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.teamnova.inma06.Profile.HomeActivity;
import com.teamnova.inma06.Register.RegisterActivity;
import com.teamnova.nova.R;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 로그인 시도 하면, LoginRequest 에서
 * JSON 형태로 결과값을 받아와서 처리한다.
 *
 * ========JSON response[] 에 담기는 정보===========
 * response["userID"]; // String
 * response["userPW"]; // String ( Hash -> ARGON2 )
 * response["userName"]; // String
 * response["userNickName"]; // String
 * response["userPhoneNum"]; // String
 * response["userGrade"]; // String
 * response["isUserID"]; // boolean
 * response["isUserPW"]; // boolean
 * */






/*로그인 액티비티 입니다.

* 회원가입 버튼을 누르면
* 이메일과 패스워드를 입력받는 회원가입 액티비티가 나옵니다.
* */
public class LoginActivity extends AppCompatActivity {

  public Context mContext;

  private static String TAG = LoginActivity.class.getSimpleName();

  //최상위 레이아웃 ( 배경 터치 할때 키보드 내리기 위함 )
  private ConstraintLayout constraintLayout;
  private InputMethodManager imm;

  private EditText userIDEt;
  private EditText userPWEt;

  private String userID; //회원가입시 기입한 아이디

  private Button loginBtn;
  private Button registerBtn;

  //이메일 형식이 아니면 출력될 메시지
  private TextView emailMsgTv;


  /* 아이디 유효성 검사 */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void certEmailEditText() {
    //입력중일때 유효성 검사
    String mailFormat = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
    String inputText = userIDEt.getText().toString();
    Pattern pattern = Pattern.compile(mailFormat);
    Matcher matcher = pattern.matcher(inputText);


    if (!matcher.matches()) {
      Log.d("TEST", "이메일이 맞지 않습니다.");
      emailMsgTv.setVisibility(View.VISIBLE);

      loginBtn.setClickable(false);
      loginBtn.setBackgroundTintList(ColorStateList.valueOf(
          getResources().getColor(R.color.disableButton)));
    } else {
      emailMsgTv.setVisibility(View.INVISIBLE);

      loginBtn.setClickable(true);
      loginBtn.setBackgroundTintList(ColorStateList.valueOf(
          getResources().getColor(R.color.enableButton)));

      Log.d("TEST", "올바른 이메일 입니다.");
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void certPasswordEditText() {

    /* 패스워드 유효성 검사 */
    if (userPWEt.getText().toString().isEmpty()) {
    //입력값이 없을때
      emailMsgTv.setVisibility(View.INVISIBLE);
      loginBtn.setClickable(false);
      loginBtn.setBackgroundTintList(ColorStateList.valueOf(
          getResources().getColor(R.color.disableButton)));
    } else {
    //입력값이 존재할때
      emailMsgTv.setVisibility(View.INVISIBLE);

      loginBtn.setClickable(true);
      loginBtn.setBackgroundTintList(ColorStateList.valueOf(
          getResources().getColor(R.color.enableButton)));
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    userIDEt = (EditText) findViewById(R.id.etRegisterID);
    userPWEt = (EditText) findViewById(R.id.etRegisterPW);

    loginBtn = (Button) findViewById(R.id.btnLogin);
    //로그인 버튼 비활성화 ( 기본값 )
    loginBtn.setClickable(false);
    loginBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.disableButton)));

    registerBtn = (Button) findViewById(R.id.btnSuccessCertBtn);

    emailMsgTv = (TextView) findViewById(R.id.tvEmailMsg);

    //화면(레이아웃) 터치시 키보드 내리게 하는 부분
    imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    constraintLayout = (ConstraintLayout) findViewById(R.id.rootLayout);
    constraintLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          imm.hideSoftInputFromWindow(userIDEt.getWindowToken(), 0);
          imm.hideSoftInputFromWindow(userPWEt.getWindowToken(), 0);
      }
    });

    /*
    * 아이디와 비밀번호를 정규식에 맞게 검사하고
    * 검사에 통과하면
    * isLoginID = true;
    * isLoginPw = true;
    * -> loginBtn 이 활성화 되고 색상이 변경된다.
    * */

    Intent intent = getIntent();
    userID = intent.getStringExtra("userID"); // 회원가입 액티비티에서 인텐트로 넘어온 userID
    userIDEt.setText(userID);

    //아이디 이벤트 리스너
    userIDEt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        certEmailEditText();
      }
      @Override
      public void afterTextChanged(Editable s) {
        certEmailEditText();
      }
    });

    //비밀번호 이벤트 리스너 Password EditText
    userPWEt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        certPasswordEditText();
      }
      @Override
      public void afterTextChanged(Editable s) {
        certPasswordEditText();
      }
    });

    //회원가입 버튼
    registerBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(registerIntent);
      }
    });

    loginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d("TEST", "로그인버튼 클릭 OK");
        final String userID = userIDEt.getText().toString();
        final String userPW = userPWEt.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try{
              JSONObject jsonResponse = new JSONObject(response);

              boolean success = jsonResponse.getBoolean("success");

              if(success == false){
                // 패스워드 불일치
                Log.i(TAG, "onResponse: 패스워드 불일치");
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("비밀번호를 확인하세요.")
                    .setNegativeButton("다시 시도", null)
                    .create()
                    .show();
              } else {
                Log.i(TAG, "onResponse: 패스워드 일치");
                // 아이디 패스워드 일치 -> 아이디와 패스워드정보를 인텐트로 넘긴다.
                // 아이디와 패스워드가 일치할 때 userID 변수를 선언한다.
                // userID에 jsonResponse 메서드로 받아오면서 객체화 시킨다.
                // 단한번만 사용되고 없어지는 객체! (인스턴스)
                // TODO: 싱글턴 패턴 공부해야함.
                String userEmail = jsonResponse.getString("userID");
                String userPassword = jsonResponse.getString("userPW");
                String profileImageDir = jsonResponse.getString("profileImageDir");
                String profileBgImageDir = jsonResponse.getString("profileBgImageDir");
                String statusMag = jsonResponse.getString("statusMsg");
                String nickName = jsonResponse.getString("nickName");


                /*로그인을 하면 Login.php -> Json 을 반환한다.
                  TODO: 비밀번호 암호화 처리 */
                Log.d(TAG, "onResponse: 이메일 -> " + userEmail);
                Log.d(TAG, "onResponse: 비밀번호 -> " + userPassword);
                Log.d(TAG, "onResponse: 프로필 이미지 경로 -> " + profileImageDir);
                Log.d(TAG, "onResponse: 프로필 배경 이미지 경로 -> " + profileBgImageDir);

                /* ----------  로그인시 홈화면 액티비티로 -----------------*/
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("userID", userEmail);
                intent.putExtra("profileImageDir", profileImageDir);
                intent.putExtra("profileBgImageDir", profileBgImageDir);
                intent.putExtra("statusMsg", statusMag);
                intent.putExtra("nickName", nickName);

                Log.e(TAG, "onResponse:인텐트 실행" );
                LoginActivity.this.startActivity(intent);
                Log.e(TAG, "onResponse:인텐트 실행완료" );

              }
            } catch (Exception e){
              e.printStackTrace();
            }
            Log.e("response -> 리스폰 결과값 출력 ", response.toString());
          }
        };
        LoginRequest loginRequest = new LoginRequest(userID, userPW, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
      }
    });
  }
}
