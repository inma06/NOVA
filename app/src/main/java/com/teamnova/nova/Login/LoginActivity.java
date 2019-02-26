package com.teamnova.nova.Login;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.teamnova.nova.MainActivity;
import com.teamnova.nova.R;
import com.teamnova.nova.Register.RegisterActivity;

import org.json.JSONObject;

/*
 * 로그인 시도 하면, LoginRequest 에서
 * JSON 형태로 결과값을 받아와서 처리한다.
 *
 * ========JSON response[] 에 담기는 정보===========
 * response["userID"]; // String
 * response["userPW"]; // String ( Hash -> ARGON2 )
 * response["userName"]; // String
 * response["userEmail"]; // String
 * response["isUserID"]; // boolean
 * response["isUserPW"]; // boolean
 * */
public class LoginActivity extends AppCompatActivity {

  private static String TAG = "로그인액티비티";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    final EditText userIDEt = (EditText) findViewById(R.id.idEt);
    final EditText userPWEt = (EditText) findViewById(R.id.pwEt);
    final Button loginBtn = (Button) findViewById(R.id.btnLogin);
    final Button registerBtn = (Button) findViewById(R.id.btnRegister);

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
        final String userID = userIDEt.getText().toString();
        final String userPW = userPWEt.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try{
              JSONObject jsonResponse = new JSONObject(response);
              boolean isUserID = jsonResponse.getBoolean("isUserID");
              boolean isUserPW = jsonResponse.getBoolean("isUserPW");

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
                String userID = jsonResponse.getString("userID");
                String userPW = jsonResponse.getString("userPW");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("userPW", userPW);
                LoginActivity.this.startActivity(intent);
              }
            } catch (Exception e){
              e.printStackTrace();
            }
            Log.e("response -> ", response.toString());
          }
        };
        LoginRequest loginRequest = new LoginRequest(userID, userPW, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
      }
    });

  }
}
