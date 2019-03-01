package com.teamnova.inma06.Register;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.register_activity_third);

    Toast.makeText(this, "프로필을 등록하세요!", Toast.LENGTH_SHORT).show();

  }
}
