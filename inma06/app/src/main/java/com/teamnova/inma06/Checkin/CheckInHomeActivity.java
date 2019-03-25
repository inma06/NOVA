package com.teamnova.inma06.Checkin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.teamnova.nova.R;

public class CheckInHomeActivity extends AppCompatActivity {


  /*
  * 좌석 현황도를 보여주고
  * 빈 좌석을 예약할 수 있는 액티비티 입니다.
  *
  * TODO: 버튼으로 구성한것 -> 그리드 뷰로 처리해야함
  * */

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_check_in_home);


    findViewById(R.id.sheet1_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Toast.makeText(CheckInHomeActivity.this, "1번좌석 클릭", Toast.LENGTH_SHORT).show();
      }
    });

    findViewById(R.id.sheet2_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Toast.makeText(CheckInHomeActivity.this, "2번좌석 클릭", Toast.LENGTH_SHORT).show();
      }
    });

    findViewById(R.id.sheet3_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Toast.makeText(CheckInHomeActivity.this, "3번좌석 클릭", Toast.LENGTH_SHORT).show();
      }
    });

    findViewById(R.id.sheet4_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Toast.makeText(CheckInHomeActivity.this, "4번좌석 클릭", Toast.LENGTH_SHORT).show();
      }
    });

    findViewById(R.id.sheet5_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Toast.makeText(CheckInHomeActivity.this, "5번좌석 클릭", Toast.LENGTH_SHORT).show();
      }
    });

    findViewById(R.id.sheet6_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Toast.makeText(CheckInHomeActivity.this, "6번좌석 클릭", Toast.LENGTH_SHORT).show();
      }
    });

    findViewById(R.id.sheet7_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Toast.makeText(CheckInHomeActivity.this, "7번좌석 클릭", Toast.LENGTH_SHORT).show();
      }
    });

    findViewById(R.id.sheet8_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Toast.makeText(CheckInHomeActivity.this, "8번좌석 클릭", Toast.LENGTH_SHORT).show();
      }
    });


  }
}
