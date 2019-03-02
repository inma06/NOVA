package com.teamnova.inma06.Login;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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

  private static String TAG = "로그인액티비티";

  //최상위 레이아웃 ( 배경 터치 할때 키보드 내리기 위함 )
  private ConstraintLayout constraintLayout;
  private InputMethodManager imm;

  private EditText userIDEt;
  private EditText userPWEt;

  private Button loginBtn;
  private Button registerBtn;

  //아이디 패스워드 유효성 검사를 위한 변수
  private boolean isLoginID;
  private boolean isLoginPW;

  //이메일 형식이 아니면 출력될 메시지
  private TextView emailMsgTv;


  /* 아이디 유효성 검사 */
  private void certEmailEditText() {
    //입력중일때 유효성 검사
    String mailFormat = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
    String inputText = userIDEt.getText().toString();
    Pattern pattern = Pattern.compile(mailFormat);
    Matcher matcher = pattern.matcher(inputText);
    Log.d("버튼활성화유무", "isLoginID->" + isLoginID + " isLoginPW->" + isLoginPW);
    if (!matcher.matches()) {
      Log.d("TEST", "이메일이 맞지 않습니다.");
      emailMsgTv.setVisibility(View.VISIBLE);
      isLoginID = false;
      loginBtn.setEnabled(false);

      //버튼 클릭 비활성화 -> 색상 변경
      loginBtn.setBackgroundTintList(ColorStateList.valueOf(
          getResources().getColor(R.color.disableButton)));
    } else {
      emailMsgTv.setVisibility(View.INVISIBLE);
      Log.d("TEST", "올바른 이메일 입니다.");
      isLoginID = true;
    }
        /*
        아이디와 패스워드가 바르게 입력되었을 때
        버튼을 활성화 하고 색상을 변경합니다.
        */
    if (isLoginID && isLoginPW) {
      loginBtn.setClickable(true);
      Log.d("TEST", "버튼활성화 OK");
      //버튼 클릭 활성화 -> 색상 변경
      loginBtn.setBackgroundTintList(ColorStateList.valueOf(
          getResources().getColor(R.color.enableButton)));
    }
  }



  private void certPasswordEditText() {

    /* 패스워드 유효성 검사 */
  /*  이메일 형식이 아니거나, 공백이면?
      패스워드가 입력되지 않으면?

  <!-- 조건이 맞지 않을 때 -->
  "이메일 형식이 아닙니다" 메시지 출력
  "버튼 클릭 비활성화" 버튼 색상 흐리게 ( setBackgroundTintList )

  /*
  <!-- 조건이 맞을 때 -->

*/
    if (userPWEt.getText().toString().isEmpty()) {
    //입력값이 없을때
    //"이메일 형식이 아닙니다" 안내메시지가 출력되지 않습니다.
    // -> INVISIBLE 패스워드 검사 부분 이기 때문.
      emailMsgTv.setVisibility(View.INVISIBLE);
      isLoginPW = false;
      loginBtn.setEnabled(false);
    //버튼 클릭 비활성화 -> 색상 변경
      loginBtn.setBackgroundTintList(ColorStateList.valueOf(
          getResources().getColor(R.color.disableButton)));
    } else {
    //입력값이 존재할때
      emailMsgTv.setVisibility(View.INVISIBLE);
      isLoginPW = true;
    }

    if (isLoginID && isLoginPW) {
      loginBtn.setEnabled(true);
      //입력값이 없을때
      //"이메일 형식이 아닙니다" 안내메시지가 보이지 않습니다.
      // -> 패스워드 검사부분 이기 때문.
      emailMsgTv.setVisibility(View.INVISIBLE);
      Log.d("TEST", "버튼활성화 OK");
      //버튼 클릭 활성화 -> 색상 변경
      loginBtn.setBackgroundTintList(ColorStateList.valueOf(
          getResources().getColor(R.color.enableButton)));
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_main_activity);

    userIDEt = (EditText) findViewById(R.id.etID);
    userPWEt = (EditText) findViewById(R.id.etPW);

    loginBtn = (Button) findViewById(R.id.btnLogin);
    //로그인 버튼 비활성화 ( 기본값 )
    loginBtn.setEnabled(false);

    registerBtn = (Button) findViewById(R.id.btnSuccessCertBtn);

    emailMsgTv = (TextView) findViewById(R.id.tvEmailMsg);
    isLoginID = false;
    isLoginPW = false;

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

    userIDEt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        certEmailEditText();
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

    //Password EditText
    userPWEt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        certPasswordEditText();
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
              boolean isUserID = jsonResponse.getBoolean("isUserID");
              boolean isUserPW = jsonResponse.getBoolean("isUserPW");
              String userName = jsonResponse.getString("userName");
              String userNickName = jsonResponse.getString("userNickName");
              String userPhoneNum = jsonResponse.getString("userPhoneNum");
              String userGrade = jsonResponse.getString("userGrade");

              /*
              * 패스워드가 불일치 할 때
              * response ->: {"isUserID":true,"isUserPW":false}
              * 계정이 존재하지 않을 때
              * response ->: {"isUserID":false,"isUserPW":false}
              * */
              if(isUserID && isUserPW == false){
                // 패스워드 불일치
                Log.e(TAG, "onResponse:패스워드 불일치" );
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("비밀번호를 확인하세요.")
                    .setNegativeButton("다시 시도", null)
                    .create()
                    .show();
              } else if(isUserID == false) {
                // 계정이 존재하지 않을 때
                Log.e(TAG, "onResponse:계정 불일치" );
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("존재하지 않는 계정입니다.")
                    .setNegativeButton("다시 시도", null)
                    .create()
                    .show();
              } else if(isUserID && isUserPW) {
                // 아이디 패스워드 일치 -> 아이디와 패스워드정보를 인텐트로 넘긴다.
                // TODO: 2일 26일 026 계정정보 [이름 나이 기수 성별 등...] 패스워드는 개발자를 위한것.
                Log.e(TAG, "onResponse:아이디 패스워드 일치" );

                // 아이디와 패스워드가 일치할 때 userID 변수를 선언한다.
                // userID에 jsonResponse 메서드로 받아오면서 객체화 시킨다.
                // 단한번만 사용되고 없어지는 객체! (인스턴스)

                // TODO: 싱글턴 패턴 공부해야함.
                String userID = jsonResponse.getString("userID");
                String userPW = jsonResponse.getString("userPW");

                // TODO:
                userName = jsonResponse.getString("userName");
                userNickName = jsonResponse.getString("userNickName");
                userPhoneNum = jsonResponse.getString("userPhoneNum");
                userGrade = jsonResponse.getString("userGrade");

                /*로그인을 하면 Login.php -> Json 을 반환한다.
                  회원정보를 인텐트로 담아서 HomeActivity 로 보낸다.*/

                Log.d(TAG, "onResponse: " + userID);
                Log.d(TAG, "onResponse: " + userPW);
                Log.d(TAG, "onResponse: " + userName );
                Log.d(TAG, "onResponse: " + userNickName);
                Log.d(TAG, "onResponse: " + userPhoneNum);
                Log.d(TAG, "onResponse: " + userGrade);

/*              Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("userPW", userPW);
                intent.putExtra("userName", userName);
                intent.putExtra("userNickName", userNickName);
                intent.putExtra("userPhoneNum", userPhoneNum);
                intent.putExtra("userGrade", userGrade);

                Log.e(TAG, "onResponse:인텐트 실행" );
                LoginActivity.this.startActivity(intent);
                Log.e(TAG, "onResponse:인텐트 실행완료" );*/
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
