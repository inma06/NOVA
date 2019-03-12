package com.teamnova.inma06.Profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.teamnova.nova.R;

public class HomeActivity extends AppCompatActivity {

  private Button profileBtn;
  private Button friendBtn;
  private Button checkBtn;
  private Button newsBtn;
  private Button streamingBtn;
  private Button timerBtn;
  private TextView userID_TV;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    Intent intent = getIntent();
    String userID = intent.getStringExtra("userID");

    profileBtn = (Button) findViewById(R.id.myProfileBtn);
    friendBtn = (Button) findViewById(R.id.friendBtn);
    checkBtn = (Button) findViewById(R.id.checkinBtn);
    newsBtn = (Button) findViewById(R.id.newsBtn);
    streamingBtn = (Button) findViewById(R.id.streamingBtn);
    timerBtn = (Button) findViewById(R.id.timerBtn);
    userID_TV = (TextView) findViewById(R.id.userID_TV);

    userID_TV.setText(userID);

    profileBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* 프로필 버튼 클릭
         *
         * Login -> Home -> ProfilePage
         *
         * */
        Toast.makeText(HomeActivity.this, "프로필 버튼을 클릭했다!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeActivity.this, ProfilePageActivity.class);
        startActivity(intent);
      }
    });


    friendBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* 친구목록 버튼 클릭 */

        Toast.makeText(HomeActivity.this, "친구목록 버튼을 클릭했다!", Toast.LENGTH_SHORT).show();

      }
    });


    checkBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* 출석체크 버튼 클릭 */
        Toast.makeText(HomeActivity.this, "출석체크 버튼을 클릭했다!", Toast.LENGTH_SHORT).show();

      }
    });


    newsBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* 뉴스 버튼 클릭 */
        Toast.makeText(HomeActivity.this, "뉴스 버튼을 클릭했다!", Toast.LENGTH_SHORT).show();

      }
    });


    streamingBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* 스트리밍 버튼 클릭 */
        Toast.makeText(HomeActivity.this, "스트리밍 버튼을 클릭했다!", Toast.LENGTH_SHORT).show();
      }
    });


    timerBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* 타이머 버튼 클릭 */
        Toast.makeText(HomeActivity.this, "타이머 버튼을 클릭했다!", Toast.LENGTH_SHORT).show();
      }
    });





  }

}
