package com.teamnova.nova.Register;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.teamnova.nova.Login.LoginActivity;
import com.teamnova.nova.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    final EditText userIDEt = (EditText) findViewById(R.id.etID);
    final EditText userPWEt= (EditText) findViewById(R.id.etPW);
    final EditText userConPWEt = (EditText) findViewById(R.id.etConPW);
    final EditText userNameEt = (EditText) findViewById(R.id.etName);
    final EditText userEmailEt = (EditText) findViewById(R.id.etEmail);

    Button registerBtn = (Button) findViewById(R.id.btnRegister);

    registerBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String userID = userIDEt.getText().toString();
        String userPW = userPWEt.getText().toString();
        String userName = userNameEt.getText().toString();
        String userEmail = userEmailEt.getText().toString();


        // responseListener 전달 받아서 실행
        // 예외처리 다이얼로그 띄우기 성공시 "확인" -> 로그인 액티비티로 전환
        Response.Listener<String> responseListener = new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try {
              JSONObject jsonResponse = new JSONObject(response);
              boolean success = jsonResponse.getBoolean("success");
              if(success) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage("회원 등록에 성공했습니다.")
                    .setPositiveButton("확인", null)
                    .create()
                    .show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(intent);
              }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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

        //회원가입 버튼 클릭시 -> 입력값 받아서 responseListener 로 전달
        RegisterRequest registerRequest = new RegisterRequest(userID, userPW, userName, userEmail, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);
      }
    });




  }
}
