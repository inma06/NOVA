package com.teamnova.bongapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

  private static EditText etUserID;
  private static EditText etUserPW;
  private static TextView btnSignUp;
  private static TextView btnForgetPW;
  private static Button btnLogin;
  private static SharedPreferences userListSpf;
  private static SharedPreferences.Editor userListEditor;
  private static InputMethodManager imm;
  private static ConstraintLayout loginLayout;
  private BackPressCloseHandler backPressCloseHandler;
  private static CheckBox cbRememberID;
  private static Boolean isRememberID;


  //뒤로가기 두번 누르면 앱 종료되게
  @Override
  public void onBackPressed() {
//        super.onBackPressed();

    backPressCloseHandler.onBackPressed();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    userListSpf = getSharedPreferences("userListData", MODE_PRIVATE);
    userListEditor = userListSpf.edit();

    //SPF 초기화
//    userListEditor.clear();

    //userList(ArrayList)에 있는 값들 SPF 저장 하기
    for(int i = 0; i < SignUp.userList.size(); i++) {
      userListEditor.putString("USER_ID_" + Integer.toString(i), SignUp.userList.get(i).getUserID());
      userListEditor.putString("USER_PW_" + Integer.toString(i), SignUp.userList.get(i).getUserPW());
      userListEditor.putString("USER_EMAIL_" + Integer.toString(i), SignUp.userList.get(i).getUserEmail());
    }
    //userList(ArrayList)의 갯수 -> 회원 수 저장 ( ArrayList 에 몇번 add 할 것인지에 사용하기 위함)
    userListEditor.putInt("USER_COUNT", SignUp.userList.size());

    //아이디 기억하기에 체크상태 저장
    userListEditor.putBoolean("IS_SAVE_ID", cbRememberID.isChecked());
    userListEditor.commit();
    if(cbRememberID.isChecked()){
      //아이디 기억하기 체크박스에 체크되어 있으면
      //etUserID에 있는 ID값 SPF에 저장하기
      userListEditor.remove("SAVE_ID"); // 이미 저장되어 있는 값 지우기
      userListEditor.putString("SAVE_ID", etUserID.getText().toString());
      userListEditor.commit();
    }

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    loginLayout = (ConstraintLayout) findViewById(R.id.loginLayout);

    cbRememberID = (CheckBox) findViewById(R.id.login_RememberID);

    etUserID = (EditText) findViewById(R.id.login_ID);
    etUserPW = (EditText) findViewById(R.id.login_PW);
    btnSignUp = (TextView) findViewById(R.id.login_signUpBtn);
    btnLogin = (Button) findViewById(R.id.login_loginBtn);
    btnForgetPW = (TextView) findViewById(R.id.login_forgetPW);
    backPressCloseHandler = new BackPressCloseHandler(this);

    //회원 정보 SPF 에서 가져오기
    userListSpf = getSharedPreferences("userListData", MODE_PRIVATE);

    //아이디 기억하기 여부 확인
    isRememberID = userListSpf.getBoolean("IS_SAVE_ID", false);
    //아이디 기억하기 되어 있으면
    if(isRememberID) {
      //아이디 가져와서 etUserID에 넣어주기
      etUserID.setText(userListSpf.getString("SAVE_ID", ""));
      cbRememberID.setChecked(true);
    }else cbRememberID.setChecked(false);

    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


    //레이아웃을 눌렀을 때 키보드 사라지게 하기
    loginLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        imm.hideSoftInputFromWindow(loginLayout.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(cbRememberID.getWindowToken(), 0);
      }
    });

    String userID;
    String userPW;
    String userEMAIL;


    int count = userListSpf.getInt("USER_COUNT", 0);
    Log.e("불러온 회원 수", Integer.toString(count));
    try {
      /*
       * ==========// 중복 생성 방지 //=============
       *
       * 어플이 처음 시작된 상태면
       * SharedPreferences 에 저장된 USER_COUNT 값이 현재 회원의 size 보다 크다. (Login 액티비티 벗어나면 초기화상태)
       * 만약 아니라면
       * 1. Login 액티비티가 Destroy 되지 않은 것
       * 2. 회원 리스트를 아무것도 추가하지 않은 상태인 것
       * -> 때문에 추가로 회원리스트를 만들지 않는다.
       * */
      if(count > SignUp.userList.size()) {
        for(int i = 0; i < count; i++) {
          userID = userListSpf.getString("USER_ID_" + Integer.toString(i), "");
          userPW = userListSpf.getString("USER_PW_" + Integer.toString(i),"");
          userEMAIL = userListSpf.getString("USER_EMAIL_" + Integer.toString(i),"");
          SignUp.userList.add(0, new UserArrayList(userID, userPW, userEMAIL));
          Log.e("userList.add", "추가하기 성공");
        }
      }
    }catch(Exception e){
      e.printStackTrace();
    }


    //비밀번호 찾기 눌렀을 때
    btnForgetPW.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Login.this, ForgetPW.class);
        startActivity(intent);
        finish();
      }
    });
    //회원가입을 눌렀을 때
    btnSignUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        //이미 열린 SignUp 액티비티가 있으면 그걸 쓰고, 없으면 만들어서 써라
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //intent 를 보내면서 다음 액티비티로부터 데이터를 받기 위해 식별번호(1000)을 준다.
        startActivityForResult(intent, 1000);
      }
    });

    //로그인을 눌렀을 때
    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String strUserPW = etUserPW.getText().toString();
        String strUserID = etUserID.getText().toString();

        for(int i = 0; i < SignUp.userList.size(); i++) {
          Log.e(Integer.toString(i) + "의 아이디", SignUp.userList.get(i).getUserID());
          Log.e(Integer.toString(i) + "의 비밀번호", SignUp.userList.get(i).getUserPW());
          Log.e(Integer.toString(i) + "의 이메일", SignUp.userList.get(i).getUserEmail());
          //입력한 ID가 ArrayList 에 존재하는 ID 이면
          if(strUserID.equals(SignUp.userList.get(i).userID)) {
            //입력한 PW가 ArrayList(index = 해당 ID의 index)에 PW와 맞는지 검사
            if(strUserPW.equals(SignUp.userList.get(i).userPW)) {
              Toast.makeText(Login.this, "환영합니다", Toast.LENGTH_SHORT).show();
              //ID와 PW가 일치하면 MainActivity 로 이동한다.
              Intent intent = new Intent(Login.this, MainActivity.class);
              startActivity(intent);
              Login.this.finish();
              return;
            }else {
              Toast.makeText(Login.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
              etUserPW.setText(""); // 비밀번호 입력란을 비운다.
              etUserPW.requestFocus(); // 비밀번호 입력란으로 포커스를 이동한다.
              return;
            }
          }
        }
        //존재하지 않는 아이디일 경우
        Toast.makeText(Login.this, "아이디를 확인해 주세요", Toast.LENGTH_SHORT).show();
        etUserID.setText("");
        etUserPW.setText("");
        etUserID.requestFocus();
      }
    });

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    //setResult 를 통해 받아온 요청번호, 상태, 데이터
    Log.d("RESULT", requestCode + "");
    Log.d("RESULT", resultCode + "");
    Log.d("RESULT", data + "");

    if(requestCode == 1000 && resultCode == RESULT_OK) {
      Toast.makeText(this, "회원가입을 완료했습니다!", Toast.LENGTH_SHORT).show();
      Log.e("회원수", Integer.toString(SignUp.userList.size()) + " 명 가입 됨.");
      etUserID.setText(data.getStringExtra("userID"));
      etUserPW.requestFocus();
    }
  }

}
