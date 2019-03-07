package com.teamnova.inma06.CertEmail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teamnova.nova.R;

public class CertActivity extends AppCompatActivity {

  private static TextView userIdTv; //유저 아이디 텍스트 뷰

  private static Button sendMailBtn; //인증메일 발송버튼
  private static Button certBtn; // 인증하기 버튼

  private static String userID;

  private EditText certNumberEt;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cert);


    userIdTv = findViewById(R.id.userID_TV);
    certNumberEt = findViewById(R.id.certNumber_ET);
    sendMailBtn = findViewById(R.id.sendMail_Btn);
    certBtn = findViewById(R.id.certBtn);

    Intent intent = getIntent();
    userID = intent.getStringExtra("userID");
    userIdTv.setText(userID);
    //인텐트로 아이디를 받아와서 텍스트뷰에 뿌려줍니다.

    sendMailBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /*final String userEmail = userIdTv.getText().toString(); //메일을 발송할 userEmail입니다.

        GMailSender sender = new GMailSender("pakbongho@gmail.com", "elqkfel0608<?>");
        try {
          sender.sendMail( getPackageName()+"에서 보낸 회원정보 찾기 메일입니다",
              "인증번호는 -> 3423 입니다.",
              "pakbongho@gmail.com",
              "inma06@naver.com");
          Toast.makeText(CertActivity.this, "이메일이 정상적으로 발송되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
          e.printStackTrace();
        }*/
        if(userID.isEmpty()==false){
          GMailSender sender = new GMailSender("pakbongho@gmail.com", "elqkfel0608<?>");
          try {
            sender.sendMail( getPackageName()+"에서 보낸 회원정보 찾기 메일입니다",
                "아이디 : " + "\n" +
                    "비밀번호: " + "\n" +
                    "감사합니다",
                "pakbongho@gmail.com",
                userID);
            Toast.makeText(CertActivity.this, "이메일이 정상적으로 발송되었습니다.", Toast.LENGTH_SHORT).show();
          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          Toast.makeText(CertActivity.this, "이메일을 확인 하세요", Toast.LENGTH_SHORT).show();
        }








        //TODO: 이메일 인증부분
        /* 이메일 발송 -> 랜덤한 코드 4자리 발송 */
        /* 이메일 확인후 랜덤 코드 입력하면 인증 성공 */
      }
    });




    certBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /*인증 번호와 발송된 인증번호를 비교하여 맞으면 인증처리합니다
        *
        * 인증처리는 (DataBase) BONG_DB -> USER -> isCertMail -> N에서 Y로 UPDATE 합니다.
        *
        * Retrofit2 사용해 봅시다.
        *
        * */

        Toast.makeText(CertActivity.this, "인증버튼을 눌렀다.", Toast.LENGTH_SHORT).show();



      }
    });







  }
}
