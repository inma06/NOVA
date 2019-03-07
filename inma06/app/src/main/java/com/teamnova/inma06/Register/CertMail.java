package com.teamnova.inma06.Register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teamnova.nova.R;

public class CertMail extends AppCompatActivity {

  private static Button btnSendEmail;
  private static String userID;
  private static String certCode;
  private static Boolean reSend;
  private static TextView userID_TV;
  private static Button btnCertMail;
  private static EditText certNumber_ET;

  private static InputMethodManager imm;
  private static ConstraintLayout forgetLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cert);

    forgetLayout = findViewById(R.id.layout);
    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .permitDiskReads()
        .permitDiskWrites()
        .permitNetwork().build());
    reSend = true;
    btnSendEmail = (Button) findViewById(R.id.sendMail_Btn);

    certNumber_ET = findViewById(R.id.certNumber_ET);

    btnCertMail = findViewById(R.id.certBtn);

    Intent intent = getIntent();
    userID = intent.getStringExtra("userID");
    certCode = intent.getStringExtra("certCode");

    /* 인증문자 5자리 대소문자 숫자포함 */
    certCode = certCode.substring(10,15);


    userID_TV = findViewById(R.id.userID_TV);

    userID_TV.setText(userID);


    //레이아웃 눌렀을 때 키보드 사라지게
    forgetLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        imm.hideSoftInputFromWindow(forgetLayout.getWindowToken(), 0);
      }
    });

    //이메일 발송 버튼 눌렀을 때
    btnSendEmail.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(reSend){
          GMailSender sender = new GMailSender("pakbongho@gmail.com", "elqkfel0608<?>");
          try {
            sender.sendMail( "회원가입을 축하 드립니다. 인증코드를 입력하세요.",
                "인증코드 : " + certCode + "\n" +
                    "감사합니다",
                "pakbongho@gmail.com",
                userID);
            reSend = false;
            Toast.makeText(CertMail.this, "인증 메일이 발송되었습니다.", Toast.LENGTH_SHORT).show();
          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          Toast.makeText(CertMail.this, "이미 발송 되었습니다.", Toast.LENGTH_SHORT).show();
        }
      }
    });


    btnCertMail.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(certNumber_ET.getText().toString().equals(certCode)) {
          Toast.makeText(CertMail.this, "인증 되었습니다.", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(CertMail.this, "입력이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        }
      }
    });


  }
}
