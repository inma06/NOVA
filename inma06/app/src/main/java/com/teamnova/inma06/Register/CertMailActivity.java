package com.teamnova.inma06.Register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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


public class CertMailActivity extends AppCompatActivity {

  private static String TAG = "CertMailActivity.java";

  private static Button btnSendEmail;
  private static String userID;
  private static String userPW;

  private static String certCode;
  private static Boolean reSend;
  private static TextView userID_TV;
  private static Button certBtn;
  private static EditText certNumber_ET;

  private static Intent intent;

  private static Button nextBtn;


  private static boolean isCert = false;



  private static InputMethodManager imm;
  private static ConstraintLayout forgetLayout;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register_cert);

    //인텐트로 넘어온 userID와 userPW를 변수에 담습니다.
    intent = getIntent();
    userID = intent.getStringExtra("userID");
    userPW = intent.getStringExtra("userPW");
    certCode = intent.getStringExtra("certCode");

    forgetLayout = findViewById(R.id.layout);
    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .permitDiskReads()
        .permitDiskWrites()
        .permitNetwork().build());
    reSend = true;
    btnSendEmail = findViewById(R.id.sendMail_Btn);

    certNumber_ET = findViewById(R.id.certNumber_ET);

    nextBtn = findViewById(R.id.nextBtn);

    certBtn = findViewById(R.id.certBtn);

    certBtn.setBackgroundColor(
        getResources().getColor(R.color.disableButton));




    userID_TV = findViewById(R.id.userID_TV);
    userID_TV.setText(userID);


    //레이아웃 눌렀을 때 키보드 사라지게
    forgetLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        imm.hideSoftInputFromWindow(forgetLayout.getWindowToken(), 0);
      }
    });

    //인증 메일 보내기 버튼
    btnSendEmail.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        //TODO: 인증 메일 Volley 사용해서 php 에서 보내는 것으로 바꿀 것.
        // 주의 ->시간남을 때 할 것


        final ProgressDialog dialog= ProgressDialog.show(CertMailActivity.this,
            "인증 메일 발송","메일을 발송하고 있습니다...",true);



        if(reSend){
          GMailSender sender = new GMailSender("pakbongho@gmail.com", "elqkfel0608<?>");
          try {
            sender.sendMail( "회원가입을 축하 드립니다. 인증코드를 입력하세요.",
                "인증코드 : " + certCode + "\n" +
                    "감사합니다",
                "pakbongho@gmail.com",
                userID);
            reSend = false;
            Toast.makeText(CertMailActivity.this, "인증 메일이 발송되었습니다.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
          } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();
          }
          dialog.dismiss();
        } else {
          Toast.makeText(CertMailActivity.this, "이미 발송 되었습니다.", Toast.LENGTH_SHORT).show();
          dialog.dismiss();
        }
        dialog.dismiss();
      }
    });


    certNumber_ET.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if(certNumber_ET.getText().toString().isEmpty() == false) {
          certBtn.setBackgroundColor(
              getResources().getColor(R.color.enableButton));
        }
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(certNumber_ET.getText().toString().isEmpty() == false) {
          certBtn.setBackgroundColor(
              getResources().getColor(R.color.enableButton));
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
        if(certNumber_ET.getText().toString().isEmpty() == false) {
          certBtn.setBackgroundColor(
              getResources().getColor(R.color.enableButton));
        }
      }
    });


    // 인증 확인 버튼
    certBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(certNumber_ET.getText().toString().equals(certCode)) {
          AlertDialog.Builder builder = new AlertDialog.Builder(CertMailActivity.this);
          builder.setMessage("인증되었습니다.")
              .setNegativeButton("확인", null)
              .create()
              .show();
          certNumber_ET.setEnabled(false);
          isCert = true;

        } else {
          AlertDialog.Builder builder = new AlertDialog.Builder(CertMailActivity.this);
          builder.setMessage("인증번호를 확인하세요.")
              .setNegativeButton("다시 시도", null)
              .create()
              .show();
          isCert = false;
        }
      }
    });


    // 가입완료 버튼
    nextBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(isCert) {

          final ProgressDialog dialog= ProgressDialog.show(CertMailActivity.this,
              "","회원 가입 진행중...",true);

          Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              try
              {
                JSONObject jsonResponse = new JSONObject(response);
                boolean success = jsonResponse.getBoolean("success");
                Log.e(TAG, "success->" + success);

                if(success) {
                  Toast.makeText(CertMailActivity.this, "회원 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                  Intent intent = new Intent(CertMailActivity.this, LoginActivity.class);
//                  intent.putExtra("userID",userIDEt.getText().toString());
                  startActivity(intent);
                  dialog.dismiss();
                }
                else {
                  Log.d("가입실패","실패입니다." );
                  AlertDialog.Builder builder = new AlertDialog.Builder(CertMailActivity.this);
                  builder.setMessage("중복된 아이디 입니다.")
                      .setNegativeButton("다시 시도", null)
                      .create()
                      .show();
                  dialog.dismiss();
                }
              }
              catch (JSONException e)
              {
                Log.d("가입실패","중복된 아이디입니다." );
                dialog.dismiss();
                e.printStackTrace();
              }
              dialog.dismiss();
            }
          };
          RegisterRequest registerRequest = new RegisterRequest(userID, userPW, responseListener);
          RequestQueue queue = Volley.newRequestQueue(CertMailActivity.this);
          Log.d("가입시도","시도입니다." );
          queue.add(registerRequest);
          Log.d("가입시도","시도입니다.2" );
        } else {
          Toast.makeText(CertMailActivity.this, "인증 확인 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }
}
