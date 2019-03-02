package com.teamnova.inma06.Register;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.teamnova.inma06.Home;
import com.teamnova.nova.R;




/* 프로필을 등록하는 액티비티 입니다.
*
*  이메일이 인증되었다면?
 *  회원가입된 이메일을 DB 에서 찾습니다.
*
*   찾은 이메일에 해당하는 테이블에 프로필 사진 칼럼에 프로필 사진을 등록합니다.
*
* */



public class Register_Third_Activity extends AppCompatActivity {

  private static String TAG = "프로필을 등록하는 액티비티";

  final Button profileSuccessBtn = (Button) findViewById(R.id.btnProfileSuccess);

  boolean isProfile = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.register_activity_third);

    Toast.makeText(this, "프로필을 등록하세요!", Toast.LENGTH_SHORT).show();

    profileSuccessBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(isProfile){
          Toast.makeText(Register_Third_Activity.this, "프로필 등록 성공!", Toast.LENGTH_SHORT).show();
          /* HOME 액티비티로 이동합니다. */
          Intent intent = new Intent(Register_Third_Activity.this, Home.class);
          Register_Third_Activity.this.startActivity(intent);

        }else {
          Log.d(TAG, " 프로필 등록 실패");
        }
      }
    });



  }
}
