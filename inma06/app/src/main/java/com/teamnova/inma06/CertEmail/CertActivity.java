package com.teamnova.inma06.CertEmail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamnova.nova.R;

public class CertActivity extends AppCompatActivity {

  TextView userEmailTv;
  TextView isCertEmailTv;

  Button certBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cert);

    userEmailTv = findViewById(R.id.email_TV);
    isCertEmailTv = findViewById(R.id.isCertTV);
    certBtn = findViewById(R.id.certBtn);

    Intent intent = getIntent();
    String userID = intent.getStringExtra("userID");
    userEmailTv.setText(userID);

    certBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String userEmail = userEmailTv.getText().toString();

        //TODO: 이메일 인증부분
        /* 이메일 발송 -> 랜덤한 코드 4자리 발송 */
        /* 이메일 확인후 랜덤 코드 입력하면 인증 성공 */

      }
    });





  }
}
