package com.teamnova.inma06.Checkin;

import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.teamnova.nova.R;

import org.json.JSONObject;

public class SheetHomeActivity extends AppCompatActivity {


  /*
  * 좌석 현황도를 보여주고
  * 빈 좌석을 예약할 수 있는 액티비티 입니다.
  *
  * TODO: 버튼으로 구성한것 -> 그리드 뷰로 처리해야함
  * */

  private Button sheet1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sheet_home);

    sheet1 = findViewById(R.id.sheet1_btn);


    findViewById(R.id.resetBtn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String sheetNumber = "3";

        Toast.makeText(SheetHomeActivity.this, "리셋버튼 클릭!", Toast.LENGTH_SHORT).show();
        final ProgressDialog dialog= ProgressDialog.show(SheetHomeActivity.this,
            "좌석 현황","체크 중...",true);
        Log.d("TEST", "리셋버튼 클릭 OK");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try{
              JSONObject jsonResponse = new JSONObject(response);

              boolean success = jsonResponse.getBoolean("success");
              String sheetNumber = jsonResponse.getString("sheetNumber");
              String userID = jsonResponse.getString("userID");
              String isUse = jsonResponse.getString("isUse");
              String startTime = jsonResponse.getString("startTime");
              String endTime = jsonResponse.getString("endTime");

              if(success == false){
                // 체크인 실패시 ( 다른 사용자가 이미 사용 중 일 경우 등... )
                Log.i("SheetHomeActivity", "onResponse: 체크인 실패");
                AlertDialog.Builder builder = new AlertDialog.Builder(SheetHomeActivity.this);
                builder.setMessage("좌석 상태 조회를 실패하였습니다.")
                    .setNegativeButton("다시 시도", null)
                    .create()
                    .show();
              } else {
                // 체크인 성공시
                Toast.makeText(SheetHomeActivity.this, "체크인 성공! API동작 확인!", Toast.LENGTH_SHORT).show();
                if(isUse.equals("N")){
                  findViewById(R.id.sheet1_btn).setBackgroundColor(
                      getResources().getColor(R.color.enableButton));
                  sheet1.setText("사용가능");
                } else {
                  findViewById(R.id.sheet1_btn).setBackgroundColor(
                      getResources().getColor(R.color.disableButton));
                  sheet1.setText( userID + "사용중");
                  Log.e("SheetHomeActivity", "onResponse:" + response);
                }
              }
            } catch (Exception e){
              e.printStackTrace();
            }
            dialog.dismiss();
            Log.e("response -> 리스폰 결과값 출력 ", response.toString());
          }
        };
        StatusLookupRequest statusLookupRequest = new StatusLookupRequest(sheetNumber, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SheetHomeActivity.this);
        queue.add(statusLookupRequest);

      }
    });


    findViewById(R.id.sheet1_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Toast.makeText(SheetHomeActivity.this, "1번좌석 클릭", Toast.LENGTH_SHORT).show();
      }
    });

    findViewById(R.id.sheet2_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Toast.makeText(SheetHomeActivity.this, "2번좌석 클릭", Toast.LENGTH_SHORT).show();
      }
    });

    findViewById(R.id.sheet3_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Toast.makeText(SheetHomeActivity.this, "3번좌석 클릭", Toast.LENGTH_SHORT).show();
      }
    });

    findViewById(R.id.sheet4_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Toast.makeText(SheetHomeActivity.this, "4번좌석 클릭", Toast.LENGTH_SHORT).show();
      }
    });

    findViewById(R.id.sheet5_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Toast.makeText(SheetHomeActivity.this, "5번좌석 클릭", Toast.LENGTH_SHORT).show();
      }
    });

    findViewById(R.id.sheet6_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Toast.makeText(SheetHomeActivity.this, "6번좌석 클릭", Toast.LENGTH_SHORT).show();
      }
    });

    findViewById(R.id.sheet7_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Toast.makeText(SheetHomeActivity.this, "7번좌석 클릭", Toast.LENGTH_SHORT).show();
      }
    });

    findViewById(R.id.sheet8_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Toast.makeText(SheetHomeActivity.this, "8번좌석 클릭", Toast.LENGTH_SHORT).show();
      }
    });


  }
}
