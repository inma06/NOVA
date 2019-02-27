package com.teamnova.nova.Register;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.teamnova.nova.Login.LoginActivity;
import com.teamnova.nova.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Register_1Activity extends AppCompatActivity {

  private boolean isPassID = false;
  private boolean isPassPW = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register_1);

    final EditText userIDEt = (EditText) findViewById(R.id.etID); //ID는 이메일
    final EditText userPWEt= (EditText) findViewById(R.id.etPW);
    final EditText userConPWEt = (EditText) findViewById(R.id.etConPW); //비밀번호 확인

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
