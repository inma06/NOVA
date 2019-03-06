package com.teamnova.inma06.Register;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


/* 회원가입 액티비티 입니다.
*
*  이메일과 패스워드를 바르게 입력합니다
*  다음버튼을 누르면 이메일을 인증하는 액티비티가 나옵니다.
*  */

public class RegisterActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.register_activity);

    final EditText userIDEt = (EditText) findViewById(R.id.etRegisterID); //ID는 이메일
    final EditText userPWEt= (EditText) findViewById(R.id.etRegisterPW); // 패스워드
    final TextView backBtn = (TextView) findViewById(R.id.backBtn); // 뒤로가기(로그인화면) 버튼
    final Button registerBtn = (Button) findViewById(R.id.registerBtn); // 회원가입 버튼


    backBtn.setOnClickListener(new View.OnClickListener() {
      //"이미 가입하셨나요?" 뒤로(로그인화면) 가기 버튼

      @Override
      public void onClick(View v) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
      }
    });

    /* 가입하기 버튼을 눌러 DB에 ID와 PW를 등록합니다. */
    registerBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* 회원가입을 합니다. */
        String userID = userIDEt.getText().toString(); //아이디
        String userPW = userPWEt.getText().toString(); //패스워드

        Response.Listener<String> responseListener = new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try
            {
              JSONObject jsonResponse = new JSONObject(response);
              boolean success = jsonResponse.getBoolean("success");
              if(success) {
                Toast.makeText(RegisterActivity.this, "회원 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(intent);
              }
              else
              {
                Log.d("가입실패","실패입니다." );
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage("회원 등록에 실패했습니다.")
                    .setNegativeButton("다시 시도", null)
                    .create()
                    .show();
              }
            }
            catch (JSONException e)
            {
              e.printStackTrace();
            }
          }
        };
        RegisterRequest registerRequest = new RegisterRequest(userID, userPW, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        Log.d("가입시도","시도입니다." );
        queue.add(registerRequest);
        Log.d("가입시도","시도입니다.2" );
      }
    });

/*    userIDEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        String mailFormat = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        if (hasFocus == false) {
          String inputText = userIDEt.getText().toString();
          Pattern pattern = Pattern.compile(mailFormat);
          Matcher matcher = pattern.matcher(inputText);
          if (!matcher.matches()) {
            Log.d("TEST", "이메일이 맞지 않습니다.");
          }else {
            Log.d("TEST", "올바른 이메일 입니다.");
          }
        }
      }
    });*/


  }
}
