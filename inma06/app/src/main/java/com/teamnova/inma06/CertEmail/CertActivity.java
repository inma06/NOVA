package com.teamnova.inma06.CertEmail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.teamnova.nova.R;

public class CertActivity extends AppCompatActivity {

  TextView userIdTv; //유저 아이디 텍스트 뷰

  Button sendMailBtn; //인증메일 발송버튼
  Button certBtn; // 인증하기 버튼

  EditText certNumberEt;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cert);


    userIdTv = findViewById(R.id.userID_TV);
    certNumberEt = findViewById(R.id.certNumber_ET);
    sendMailBtn = findViewById(R.id.sendMail_Btn);
    certBtn = findViewById(R.id.certBtn);

    Intent intent = getIntent();
    String userID = intent.getStringExtra("userID");
    userIdTv.setText(userID);
    //인텐트로 아이디를 받아와서 텍스트뷰에 뿌려줍니다.

    certBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String userEmail = userIdTv.getText().toString(); //메일을 발송할 userEmail입니다.



        //TODO: 이메일 인증부분
        /* 이메일 발송 -> 랜덤한 코드 4자리 발송 */
        /* 이메일 확인후 랜덤 코드 입력하면 인증 성공 */

      }
    });







  }
}
