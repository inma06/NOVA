package com.teamnova.inma06.Register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.teamnova.inma06.Login.LoginActivity;
import com.teamnova.nova.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
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
  private Button checkBtn;
  private Button nextBtn;

  //아이디 패스워드 유효성 검사를 위한 변수
  private boolean isLoginID;
  private boolean isLoginPW;

  //이메일 형식이 아니면 출력될 메시지
  private TextView isPassID_TV;


  /* 랜덤한 숫자 생성 */
  /**
   * 전달된 파라미터에 맞게 난수를 생성한다
   * @param len : 생성할 난수의 길이
   * @param dupCd : 중복 허용 여부 (1: 중복허용, 2:중복제거)
   *
   * Created by 닢향
   * http://niphyang.tistory.com
   */
  public static String numberGen(int len, int dupCd ) {

    Random rand = new Random();
    String numStr = ""; //난수가 저장될 변수

    for(int i=0;i<len;i++) {

      //0~9 까지 난수 생성
      String ran = Integer.toString(rand.nextInt(10));

      if(dupCd==1) {
        //중복 허용시 numStr에 append
        numStr += ran;
      }else if(dupCd==2) {
        //중복을 허용하지 않을시 중복된 값이 있는지 검사한다
        if(!numStr.contains(ran)) {
          //중복된 값이 없으면 numStr에 append
          numStr += ran;
        }else {
          //생성된 난수가 중복되면 루틴을 다시 실행한다
          i-=1;
        }
      }
    }
    return numStr;
  }


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
      isPassID_TV.setVisibility(View.VISIBLE);
      isLoginID = false;
      nextBtn.setClickable(false);
      nextBtn.setEnabled(false);

      //버튼 클릭 비활성화 -> 색상 변경
      nextBtn.setBackgroundColor(
          getResources().getColor(R.color.disableButton));
    } else {
      isPassID_TV.setVisibility(View.INVISIBLE);
      Log.d("TEST", "올바른 이메일 입니다.");
      isLoginID = true;
    }
        /*
        아이디와 패스워드가 바르게 입력되었을 때
        버튼을 활성화 하고 색상을 변경합니다.
        */
    if (isLoginID && isLoginPW) {
      nextBtn.setClickable(true);
      Log.d("TEST", "버튼활성화 OK");
      //버튼 클릭 활성화 -> 색상 변경
      nextBtn.setBackgroundColor(
          getResources().getColor(R.color.enableButton));
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
      isPassID_TV.setVisibility(View.INVISIBLE);
      isLoginPW = false;
      nextBtn.setEnabled(false);
      nextBtn.setClickable(false);
      //버튼 클릭 비활성화 -> 색상 변경
      nextBtn.setBackgroundColor(
          getResources().getColor(R.color.disableButton));
    } else {
      //입력값이 존재할때
      isPassID_TV.setVisibility(View.INVISIBLE);
      isLoginPW = true;
    }

    if (isLoginID && isLoginPW) {
      nextBtn.setEnabled(true);
      nextBtn.setClickable(true);
      //입력값이 없을때
      //"이메일 형식이 아닙니다" 안내메시지가 보이지 않습니다.
      // -> 패스워드 검사부분 이기 때문.
      isPassID_TV.setVisibility(View.INVISIBLE);
      Log.d("TEST", "버튼활성화 OK");
      //버튼 클릭 활성화 -> 색상 변경
      nextBtn.setBackgroundColor(
          getResources().getColor(R.color.enableButton));
    }
  }





  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    isPassID_TV = (TextView) findViewById(R.id.isPassID_TV);
    userIDEt = (EditText) findViewById(R.id.etRegisterID); //ID는 이메일
    userPWEt= (EditText) findViewById(R.id.etRegisterPW); // 패스워드
    checkBtn = (Button) findViewById(R.id.idCheckBtn);
    backBtn = (TextView) findViewById(R.id.backBtn); // 뒤로가기(로그인화면) 버튼
    nextBtn = (Button) findViewById(R.id.registerBtn); // 회원가입 버튼


    checkBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        final ProgressDialog dialog= ProgressDialog.show(RegisterActivity.this,
            "아이디 중복 검사","검사 중...",true);
        if(isLoginID){

          String userID = userIDEt.getText().toString(); //아이디
//        DB에 중복된 아이디가 있는지 검사합니다.

          Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              try
              {
                JSONObject jsonResponse = new JSONObject(response);
                boolean success = jsonResponse.getBoolean("success");
                Log.e(TAG, "success->" + success);

                if(success) {
                  //사용할수 있는 아이디 입니다.
                  AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                  builder.setMessage("사용 가능한 아이디 입니다.")
                      .setNegativeButton("확인", null)
                      .create()
                      .show();
                  isPassID_TV.setText("사용할 수 있는 이메일 입니다.");
                  isPassID_TV.setVisibility(View.VISIBLE);
                  userIDEt.setEnabled(false);
                }
                else
                {
                  Log.d("중복아이디","실패입니다." );
                  AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                  builder.setMessage("중복된 아이디 입니다.")
                      .setNegativeButton("다시 시도", null)
                      .create()
                      .show();
                }
              }
              catch (JSONException e)
              {
                Log.d("가입실패","서버오류오류오류발생" );
                e.printStackTrace();
              }
              dialog.dismiss();
            }
          };
          CheckRequest checkRequest = new CheckRequest(userID, responseListener);
          RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
;
          Log.d("가입시도","시도입니다." );
          queue.add(checkRequest);
          Log.d("가입시도","시도입니다.2" );
        } else {
          AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
          builder.setMessage("이메일 형식이 아닙니다.")
              .setNegativeButton("다시 시도", null)
              .create()
              .show();
        }
      }
    });



    //"이미 가입하셨나요?" 뒤로(로그인화면) 가기 버튼
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
      }
    });

    //로그인 버튼 비활성화 ( 기본값 )
    nextBtn.setEnabled(false);
    nextBtn.setClickable(false);

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


    /* 가입하기 버튼을 눌러 DB에 ID와 PW를 등록합니다.
     * -> 수정됨.
      * -> 가입하기 버튼을 누르면 메일인증하기 액티비티로 넘어갑니다.
      * -> 최종 가입은 메일 인증이 완료되면 DB에 저장됩니다.*/
    nextBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* 회원가입을 합니다. */
        String userID = userIDEt.getText().toString(); //아이디
        String userPW = userPWEt.getText().toString(); //패스워드
        String certCode = numberGen(4,2);

        /* 입력한 아이디와 패스워드를 인텐트에 담아서 "인증하기 액티비티로" 보냅니다.*/
        Intent intent = new Intent(RegisterActivity.this, CertMailActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("userPW", userPW);
        intent.putExtra("certCode", certCode);
        startActivity(intent);
        Log.e(TAG, "onClick: 회원가입 (다음) 버튼을 눌렀습니다.");

       /* 회원가입을 하여 아이디와 패스워드를 DB에 저장합니다 ( 수정되기 이전 )

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
        Log.d("가입시도","시도입니다.2" );*/

      }
    });

  }
}
