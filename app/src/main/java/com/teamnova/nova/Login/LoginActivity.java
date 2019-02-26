package com.teamnova.nova.Login;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {


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
              boolean success = jsonResponse.getBoolean("success");
              if(success) {
                String userID = jsonResponse.getString("userID");
                String userPW = jsonResponse.getString("userPW");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("userPW", userPW);
                LoginActivity.this.startActivity(intent);
              } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("로그인에 실패하였습니다.")
                    .setNegativeButton("다시 시도", null)
                    .create()
                    .show();
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
