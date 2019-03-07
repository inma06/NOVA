package com.teamnova.inma06.Register;

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


/* 회원가입 액티비티 입니다.
*
*  이메일과 패스워드를 바르게 입력합니다
*  다음버튼을 누르면 이메일을 인증하는 액티비티가 나옵니다.
*  */

public class RegisterActivity extends AppCompatActivity {


  private static String TAG = LoginActivity.class.getSimpleName();

  //최상위 레이아웃 ( 배경 터치 할때 키보드 내리기 위함 )
  private ConstraintLayout constraintLayout;
  private InputMethodManager imm;

  private EditText userIDEt;
  private EditText userPWEt;

  private TextView backBtn; //이미 가입하셨나요?
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
      registerBtn.setClickable(false);
      registerBtn.setEnabled(false);

      //버튼 클릭 비활성화 -> 색상 변경
      registerBtn.setBackgroundTintList(ColorStateList.valueOf(
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
      registerBtn.setClickable(true);
      Log.d("TEST", "버튼활성화 OK");
      //버튼 클릭 활성화 -> 색상 변경
      registerBtn.setBackgroundTintList(ColorStateList.valueOf(
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
      registerBtn.setEnabled(false);
      registerBtn.setClickable(false);
      //버튼 클릭 비활성화 -> 색상 변경
      registerBtn.setBackgroundTintList(ColorStateList.valueOf(
          getResources().getColor(R.color.disableButton)));
    } else {
      //입력값이 존재할때
      emailMsgTv.setVisibility(View.INVISIBLE);
      isLoginPW = true;
    }

    if (isLoginID && isLoginPW) {
      registerBtn.setEnabled(true);
      registerBtn.setClickable(true);
      //입력값이 없을때
      //"이메일 형식이 아닙니다" 안내메시지가 보이지 않습니다.
      // -> 패스워드 검사부분 이기 때문.
      emailMsgTv.setVisibility(View.INVISIBLE);
      Log.d("TEST", "버튼활성화 OK");
      //버튼 클릭 활성화 -> 색상 변경
      registerBtn.setBackgroundTintList(ColorStateList.valueOf(
          getResources().getColor(R.color.enableButton)));
    }
  }





  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.register_activity);

    emailMsgTv = (TextView) findViewById(R.id.isPassID_Tv);
    userIDEt = (EditText) findViewById(R.id.etRegisterID); //ID는 이메일
    userPWEt= (EditText) findViewById(R.id.etRegisterPW); // 패스워드
    backBtn = (TextView) findViewById(R.id.backBtn); // 뒤로가기(로그인화면) 버튼
    registerBtn = (Button) findViewById(R.id.registerBtn); // 회원가입 버튼

    backBtn.setOnClickListener(new View.OnClickListener() {
      //"이미 가입하셨나요?" 뒤로(로그인화면) 가기 버튼

      @Override
      public void onClick(View v) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
      }
    });

    //로그인 버튼 비활성화 ( 기본값 )
    registerBtn.setEnabled(false);
    registerBtn.setClickable(false);

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




    //아이디 이벤트 리스너 시작
    userIDEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        certEmailEditText();
      }
    });

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
    //아이디 이벤트 리스너 끝


    //비밀번호 이벤트 리스너 시작

    userPWEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        certPasswordEditText();
      }
    });

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
              Log.e(TAG, "success->" + success);

              if(success) {
                Toast.makeText(RegisterActivity.this, "회원 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("userID",userIDEt.getText().toString());
                RegisterActivity.this.startActivity(intent);
              }
              else
              {
                Log.d("가입실패","실패입니다." );
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage("중복된 아이디 입니다.")
                    .setNegativeButton("다시 시도", null)
                    .create()
                    .show();
              }
            }
            catch (JSONException e)
            {
              Log.d("가입실패","중복된 아이디입니다." );

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

  }
}
