package com.teamnova.nova.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.teamnova.nova.R;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.teamnova.nova.Main.MainActivity;
import com.teamnova.nova.Test.Naver_Login_Test;
import com.teamnova.nova.Register.Register_1Activity;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 로그인 시도 하면, LoginRequest 에서
 * JSON 형태로 결과값을 받아와서 처리한다.
 *
 * /**
 * <br/> OAuth2.0 인증을 통해 Access Token을 발급받는 예제, 연동해제하는 예제,
 * <br/> 발급된 Token을 활용하여 Get 등의 명령을 수행하는 예제, 네아로 커스터마이징 버튼을 사용하는 예제 등이 포함되어 있다.
 * @author naver
 *
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
 * @author atanasio
 * */


public class LoginActivity extends Activity {
  private static String TAG = "LoginActivity";

  /**
   * client 정보를 넣어준다.
   */
  private static String OAUTH_CLIENT_ID = "iNQ1linm0f0ipXyHMuBW";
  private static String OAUTH_CLIENT_SECRET = "MhXHKeAa9q";
  private static String OAUTH_CLIENT_NAME = "빡세게 공부하자, 빡공";
  //네이버 회원정보 가져오기
  private static NaverLogin naverInfo;

  private static OAuthLogin mOAuthLoginInstance;
  private static Context mContext;

  /**
   * UI 요소들
   */
  private static String mApiResultText;
  private static String mOauthAT;
  private static String mOauthRT;
  private static String mOauthExpires;
  private static String mOauthTokenType;
  private static String mOauthState;

  private OAuthLoginButton mOAuthLoginButton; // 네아로버튼


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

  //이메일이 아닐때 출력될 메시지
  private TextView emailMsgTv;

  @Override
  protected void onResume() {
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    super.onResume();
  }
  public void onButtonClick(View v) throws Throwable {

    switch (v.getId()) {
      case R.id.buttonOAuth: {
        Log.e(TAG, "onButtonClick: 네아로 버튼 클릭");
        mOAuthLoginInstance.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
        break;
      }
      case R.id.btnLogin: {
        Log.e(TAG, "onButtonClick: 로그인버튼 클릭");
        break;
      }
      case R.id.btnRegister: {
        Log.e(TAG, "onButtonClick: 회원가입 버튼 클릭");
        break;
      }
      case R.id.buttonVerifier: {
        new RequestApiTask().execute();
        break;
      }
      case R.id.buttonRefresh: {
        new RefreshTokenTask().execute();
        break;
      }
      case R.id.buttonOAuthLogout: {
        mOAuthLoginInstance.logout(mContext);
        updateView();
        break;
      }
      case R.id.buttonOAuthDeleteToken: {
        new DeleteTokenTask().execute();
        break;
      }
      default:
        break;
    }
  }

  private class RefreshTokenTask extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground(Void... params) {
      return mOAuthLoginInstance.refreshAccessToken(mContext);
    }

    protected void onPostExecute(String res) {
      updateView();
    }
  }


  private class RequestApiTask extends AsyncTask<Void, Void, String> {
    @Override
    protected void onPreExecute() {
      Log.e(TAG, "onPreExecute: ");
      mApiResultText = (String) "";
    }

    @Override
    protected String doInBackground(Void... params) {
      String url = "https://openapi.naver.com/v1/nid/me";
      String at = mOAuthLoginInstance.getAccessToken(mContext);
      return mOAuthLoginInstance.requestApi(mContext, at, url);
    }

    protected void onPostExecute(String content) {
      Log.e(TAG, "onPostExecute: content -> " + content);
//      mApiResultText.setText((String) content);
    }
  }

  private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
      boolean isSuccessDeleteToken = mOAuthLoginInstance.logoutAndDeleteToken(mContext);

      if (!isSuccessDeleteToken) {
        // 서버에서 token 삭제에 실패했어도 클라이언트에 있는 token 은 삭제되어 로그아웃된 상태이다
        // 실패했어도 클라이언트 상에 token 정보가 없기 때문에 추가적으로 해줄 수 있는 것은 없음
        Log.e(TAG, "errorCode:" + mOAuthLoginInstance.getLastErrorCode(mContext));
        Log.e(TAG, "errorDesc:" + mOAuthLoginInstance.getLastErrorDesc(mContext));
      }

      return null;
    }

    protected void onPostExecute(Void v) {
      updateView();
    }
  }

  /**
   * startOAuthLoginActivity() 호출시 인자로 넘기거나, OAuthLoginButton 에 등록해주면 인증이 종료되는 걸 알 수 있다.
   */
  static private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
    @Override
    public void run(boolean success) {
      if (success) {
        String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
        String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
        long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
        String tokenType = mOAuthLoginInstance.getTokenType(mContext);

        Log.e(TAG, "accessToken: " + accessToken);

        Log.e(TAG, "refreshToken: " + refreshToken);

        Log.e(TAG, "String.valueOf(expiresAt): " + String.valueOf(expiresAt));

        Log.e(TAG, "tokenType: " + tokenType);

        Log.d(TAG, "mOAuthLoginInstance.getState(mContext).toString(): " + mOAuthLoginInstance.getState(mContext).toString());

      } else {
        String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
        String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
        Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
      }
    }

  };


  /*네이버 아이디로 로그인 (네아로) 서버로 요청을 보낸다*/
  private void initData() {
    mOAuthLoginInstance = OAuthLogin.getInstance();

    mOAuthLoginInstance.showDevelopersLog(true);
    mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);

    /*
     * 2015년 8월 이전에 등록하고 앱 정보 갱신을 안한 경우 기존에 설정해준 callback intent url 을 넣어줘야 로그인하는데 문제가 안생긴다.
     * 2015년 8월 이후에 등록했거나 그 뒤에 앱 정보 갱신을 하면서 package name 을 넣어준 경우 callback intent url 을 생략해도 된다.
     */
//    mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME, OAUTH_callback_intent_url);
  }

  // 네이버 아이디로 로그인 (네아로) 버튼 셋팅
  // 누르면 값 받아오게 하기.
  private void initView() {
    mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
    mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
    updateView();
  }

  /*결과를 출력한다*/
  private void updateView() {

    mOauthAT = mOAuthLoginInstance.getAccessToken(mContext);
    mOauthRT = mOAuthLoginInstance.getRefreshToken(mContext);
    mOauthExpires = String.valueOf(mOAuthLoginInstance.getExpiresAt(mContext));
    mOauthTokenType = mOAuthLoginInstance.getTokenType(mContext);
    mOauthState = mOAuthLoginInstance.getState(mContext).toString();

  }

  /* 아이디의 유효성 검사 */
  private void setUserIDEt() {
    userIDEt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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
          loginBtn.setBackgroundColor(Color.parseColor("#808080"));
        }else {
          Log.d("TEST", "올바른 이메일 입니다.");
          emailMsgTv.setVisibility(View.INVISIBLE);
          isLoginID = true;
        }
        /*
        아이디와 패스워드가 바르게 입력되었을 때
        버튼을 활성화 하고 색상을 변경합니다.
        */
        if(isLoginID && isLoginPW){
          loginBtn.setClickable(true);
          loginBtn.setEnabled(true);
          emailMsgTv.setVisibility(View.INVISIBLE);
          Log.d("TEST", "버튼활성화 OK");
          loginBtn.setBackgroundColor(Color.parseColor("#fa7931"));
        }
      }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
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
          loginBtn.setBackgroundColor(Color.parseColor("#808080"));
        }else {
          emailMsgTv.setVisibility(View.INVISIBLE);
          Log.d("TEST", "올바른 이메일 입니다.");
          isLoginID = true;
        }

        /*
        아이디와 패스워드가 바르게 입력되었을 때
        버튼을 활성화 하고 색상을 변경합니다.
        */
        if(isLoginID && isLoginPW){
          loginBtn.setEnabled(true);
          Log.d("TEST", "버튼활성화 OK");
          emailMsgTv.setVisibility(View.INVISIBLE);
          loginBtn.setBackgroundColor(Color.parseColor("#fa7931"));
        }
      }
      @Override
      public void afterTextChanged(Editable s) {
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
          loginBtn.setBackgroundColor(Color.parseColor("#808080"));
        }else {
          emailMsgTv.setVisibility(View.INVISIBLE);
          Log.d("TEST", "올바른 이메일 입니다.");
          isLoginID = true;
        }
        /*
        아이디와 패스워드가 바르게 입력되었을 때
        버튼을 활성화 하고 색상을 변경합니다.
        */
        if(isLoginID && isLoginPW){
          loginBtn.setEnabled(true);
          Log.d("TEST", "버튼활성화 OK");
          loginBtn.setBackgroundColor(Color.parseColor("#fa7931"));
        }
      }
    });
  }

  /* 비밀번호의 유효성 검사 */
  private void setUserPWEt() {
    userPWEt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //입력하기 전에
        //비어있는지 유효성검사
        if(userPWEt.getText().toString().isEmpty()) {
          //입력값이 없을때
          emailMsgTv.setVisibility(View.INVISIBLE);
          isLoginPW = false;
          loginBtn.setEnabled(false);
          loginBtn.setBackgroundColor(Color.parseColor("#808080"));
        } else {
          //입력값이 존재할때
          emailMsgTv.setVisibility(View.INVISIBLE);
          isLoginPW = true;
        }
        /*
        아이디와 패스워드가 바르게 입력되었을 때
        버튼을 활성화 하고 색상을 변경합니다.
        */
        if(isLoginID && isLoginPW){
          loginBtn.setEnabled(true);
          emailMsgTv.setVisibility(View.INVISIBLE);
          Log.d("TEST", "버튼활성화 OK");
          loginBtn.setBackgroundColor(Color.parseColor("#fa7931"));
        }
      }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        //입력 중 일때 -> 입력값이 존재하는가?
        if(userPWEt.getText().toString().isEmpty()) {
          //입력값이 없을때
          emailMsgTv.setVisibility(View.INVISIBLE);
          isLoginPW = false;
          loginBtn.setEnabled(false);
          loginBtn.setBackgroundColor(Color.parseColor("#808080"));
        } else {
          //입력값이 존재할때
          emailMsgTv.setVisibility(View.INVISIBLE);
          isLoginPW = true;
        }
        /*
        아이디와 패스워드가 바르게 입력되었을 때
        버튼을 활성화 하고 색상을 변경합니다.
        */
        if(isLoginID && isLoginPW){
          loginBtn.setEnabled(true);
          emailMsgTv.setVisibility(View.INVISIBLE);
          Log.d("TEST", "버튼활성화 OK");
          loginBtn.setBackgroundColor(Color.parseColor("#fa7931"));
        }
      }
      @Override
      public void afterTextChanged(Editable s) {
        //입력이 끝났을 때
        if(userPWEt.getText().toString().isEmpty()) {
          //입력값이 없을때
          emailMsgTv.setVisibility(View.INVISIBLE);
          isLoginPW = false;
          loginBtn.setEnabled(false);
          loginBtn.setBackgroundColor(Color.parseColor("#808080"));

        } else {
          //입력값이 존재할때
          emailMsgTv.setVisibility(View.INVISIBLE);
          isLoginPW = true;
        }
        /*
        아이디와 패스워드가 바르게 입력되었을 때
        버튼을 활성화 하고 색상을 변경합니다.
        */
        if(isLoginID && isLoginPW){
          loginBtn.setEnabled(true);
          emailMsgTv.setVisibility(View.INVISIBLE);
          Log.d("TEST", "버튼활성화 OK");
          loginBtn.setBackgroundColor(Color.parseColor("#fa7931"));
        }
      }
    });
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_main_activity);
    //화면(레이아웃) 터치시 키보드 내리게 하는 부분
    imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    constraintLayout = (ConstraintLayout) findViewById(R.id.rootLayout);
    constraintLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.e(TAG, "onClick: 배경화면 클릭!!!!!!!" );
        imm.hideSoftInputFromWindow(userIDEt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(userPWEt.getWindowToken(), 0);
      }
    });

    //네이버 회원정보 가져오기
    NaverLogin naverInfo = new NaverLogin();

    mContext = this;

    initData();
    initView();

    userIDEt = (EditText) findViewById(R.id.etID);
    userPWEt = (EditText) findViewById(R.id.etPW);
    loginBtn = (Button) findViewById(R.id.btnLogin);
    //로그인 버튼 비활성화 회색으로 변경 ( 기본값 )
    loginBtn.setEnabled(false);
    loginBtn.setBackgroundColor(Color.parseColor("#808080"));

    registerBtn = (Button) findViewById(R.id.btnRegister);

//    naverLoginBtn = (ImageButton) findViewById(R.id.naverLoginBtn);

    emailMsgTv = (TextView) findViewById(R.id.tvEmailMsg);
    isLoginID = false;
    isLoginPW = false;


    ///////////////////////////////////////////////
    /*
    * 아이디와 비밀번호를 정규식에 맞게 검사하고
    * 검사에 통과하면
    * isLoginID = true;
    * isLoginPw = true;
    * -> loginBtn 이 활성화 되고 색상이 변경된다.
    * */
    /* 아이디의 유효성 검사 */
    setUserIDEt();
    /* 비밀번호의 유효성 검사 */
    setUserPWEt();

//    registerBtn.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        Intent registerIntent = new Intent(LoginActivity.this, Register_1Activity.class);
//        LoginActivity.this.startActivity(registerIntent);
//      }
//    });

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
                String userName = jsonResponse.getString("userName");
                String userNickName = jsonResponse.getString("userNickName");
                String userPhoneNum = jsonResponse.getString("userPhoneNum");
                String userGrade = jsonResponse.getString("userGrade");

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("userPW", userPW);
                intent.putExtra("userName", userName);
                intent.putExtra("userNickName", userNickName);
                intent.putExtra("userPhoneNum", userPhoneNum);
                intent.putExtra("userGrade", userGrade);

                Log.e(TAG, "onResponse:인텐트 실행" );
                LoginActivity.this.startActivity(intent);
                Log.e(TAG, "onResponse:인텐트 실행완료" );
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

/*    naverLoginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "onClick: 네이버 로그인 버튼 클릭!");

        Intent intent = new Intent(LoginActivity.this, Naver_Login_Test.class);
        startActivity(intent);

        Log.d(TAG, "onClick: 네이버 로그인 액티비티로 이동");
      }
    });*/
  }
}
