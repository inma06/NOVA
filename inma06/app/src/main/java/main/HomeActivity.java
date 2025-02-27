package main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import chat.chatRoom.ChatRoomMainActivity;
import chat.friendList.FriendListMainActivity;
import seat.QRCodeScanActivity;
import profile.ProfileMainActivity;
import seat.SeatHomeActivity;
import seat.SeatLookupRequest;
import timer.TimerActivity;

import com.teamnova.nova.R;

import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

  private static String TAG = "HomeActivity";

  /* 로그인시 가져온 유저의 DB 정보를 맴버변수에 담아서 사용한다. */
  public static String mProfileImageDir = "";
  public static String mProfileBgImageDir = "";
  public static String mUserID = "";
  public static String mStatusMsg = "";
  public static String mNickName = "";
  public static String mUserNo = "";


  private Button profileBtn;
  private Button friendBtn;
  private Button checkBtn;
  private Button newsBtn;
  private Button streamingBtn;
  private Button timerBtn;
  private Button test01Btn;
  private Button qrcodeBtn;
  private TextView userID_TV;
  private Button chatRoomJoinBtn;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    Intent intent = getIntent();
    mProfileImageDir = intent.getStringExtra("profileImageDir");
    mProfileBgImageDir = intent.getStringExtra("profileBgImageDir");
    mStatusMsg = intent.getStringExtra("statusMsg");
    mNickName = intent.getStringExtra("nickName");
    mUserID = intent.getStringExtra("userID");
    mUserNo = intent.getStringExtra("userNo");


    qrcodeBtn = (Button) findViewById(R.id.qrCodeScanBtn);
    profileBtn = (Button) findViewById(R.id.myProfileBtn);
    test01Btn = (Button) findViewById(R.id.test01Btn);
    friendBtn = (Button) findViewById(R.id.friendBtn);
    checkBtn = (Button) findViewById(R.id.checkinBtn);
    newsBtn = (Button) findViewById(R.id.newsBtn);
    streamingBtn = (Button) findViewById(R.id.streamingBtn);
    timerBtn = (Button) findViewById(R.id.timerBtn);
    userID_TV = (TextView) findViewById(R.id.userID_TV);
    chatRoomJoinBtn = (Button) findViewById(R.id.chatRoomJoinBtn);



    userID_TV.setText(mUserID);




    profileBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* 프로필 버튼 클릭
         *
         * Login -> Home -> ProfilePage
         *
         * */
        Toast.makeText(HomeActivity.this, "프로필 버튼을 클릭했다!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeActivity.this, ProfileMainActivity.class);
        startActivity(intent);
      }
    });


    friendBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* 채팅 버튼 클릭 */

        Intent intent = new Intent(HomeActivity.this, ChatRoomMainActivity.class);
        startActivity(intent);
        Toast.makeText(HomeActivity.this, "채팅방 목록으로!", Toast.LENGTH_SHORT).show();

      }
    });

    friendBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* 채팅 버튼 클릭 */

        Intent intent = new Intent(HomeActivity.this, ChatRoomMainActivity.class);
        startActivity(intent);
        Toast.makeText(HomeActivity.this, "채팅방 목록으로!", Toast.LENGTH_SHORT).show();

      }
    });



    checkBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* 좌석현황보기 버튼 클릭 */
        final ProgressDialog progressDialog = ProgressDialog.show(HomeActivity.this,
            "좌석 보기","불러오는 중...",true);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {

            try{
              JSONObject jsonResponse = new JSONObject(response);
              boolean success = jsonResponse.getBoolean("success");
              String responseData = jsonResponse.getString("response");

              JsonParser jsonParser =new JsonParser();
              JsonArray jsonArray = (JsonArray) jsonParser.parse(responseData);



              String[] seatNumber = new String[55];
              String[] seatStatus = new String[55];
              String[] startTime = new String[55];
              String[] endTime = new String[55];
              String[] userID = new String[55];

              for(int i = 0; i < 50; i++){
                JsonObject object = (JsonObject) jsonArray.get(i);
                seatNumber[i] = object.get("seatNumber").getAsString();
                seatStatus[i] = object.get("seatStatus").getAsString();
                startTime[i] = object.get("startTime").getAsString();
                endTime[i] = object.get("endTime").getAsString();
                userID[i] = object.get("userID").getAsString();
              }


              if(success == false){
                Log.i(TAG, "onResponse: 체크인 실패");
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("체크인에 실패하였습니다.")
                    .setNegativeButton("다시 시도", null)
                    .create()
                    .show();
              } else {
                // 체크인 성공시
                Log.e(TAG, "------->");
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, "체크인 성공! API동작 확인!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, SeatHomeActivity.class);
                for(int i = 0; i < 50; i++) {
                  intent.putExtra("seatNumber"+ Integer.toString(i), seatNumber[i]);
                  intent.putExtra("seatStatus"+ Integer.toString(i), seatStatus[i]);
                  intent.putExtra("startTime"+ Integer.toString(i), startTime[i]);
                  intent.putExtra("endTime"+ Integer.toString(i), endTime[i]);
                  intent.putExtra("userID"+ Integer.toString(i), userID[i]);
                }
                startActivity(intent);
              }
            } catch (Exception e){
              e.printStackTrace();
            }
            progressDialog.dismiss();
//            Log.e("response -> 리스폰 결과값 출력 ", response);
          }
        };
        SeatLookupRequest seatLookupRequest = new SeatLookupRequest("50", responseListener);
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        queue.add(seatLookupRequest);


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
        Intent intent = new Intent(HomeActivity.this, TimerActivity.class);
        startActivity(intent);
      }
    });



    test01Btn.setText("친구목록 테스트");
    test01Btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* 기능 테스트 버튼 클릭 */
        Toast.makeText(HomeActivity.this, "친구목록 버튼을 클릭했다!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeActivity.this, FriendListMainActivity.class);
        startActivity(intent);
      }
    });


    chatRoomJoinBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* 기능 테스트 버튼 클릭 */
        Toast.makeText(HomeActivity.this, "채팅방에 조인", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeActivity.this, ChatRoomMainActivity.class);
        startActivity(intent);
      }
    });



    qrcodeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* QR 코드 스캔 버튼*/
        Toast.makeText(HomeActivity.this, "QR 코드 스캔 버튼을 클릭했다!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeActivity.this, QRCodeScanActivity.class);
        startActivity(intent);
      }
    });

  }

}
