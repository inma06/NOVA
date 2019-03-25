package Main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.teamnova.inma06.Checkin.CheckInHomeActivity;
import com.teamnova.inma06.Checkin.QRCodeScanActivity;
import com.teamnova.inma06.Profile.ProfileMainActivity;
import com.teamnova.nova.R;

public class HomeActivity extends AppCompatActivity {

  /* 로그인시 가져온 유저의 DB 정보를 맴버변수에 담아서 사용한다. */
  public static String mProfileImageDir = "";
  public static String mProfileBgImageDir = "";
  public static String mUserID = "";
  public static String mStatusMsg = "";
  public static String mNickName = "";


  private Button profileBtn;
  private Button friendBtn;
  private Button checkBtn;
  private Button newsBtn;
  private Button streamingBtn;
  private Button timerBtn;
  private Button qrcodeBtn;
  private TextView userID_TV;


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



    qrcodeBtn = (Button) findViewById(R.id.qrCodeScanBtn);
    profileBtn = (Button) findViewById(R.id.myProfileBtn);
    friendBtn = (Button) findViewById(R.id.friendBtn);
    checkBtn = (Button) findViewById(R.id.checkinBtn);
    newsBtn = (Button) findViewById(R.id.newsBtn);
    streamingBtn = (Button) findViewById(R.id.streamingBtn);
    timerBtn = (Button) findViewById(R.id.timerBtn);
    userID_TV = (TextView) findViewById(R.id.userID_TV);


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
        /* 친구목록 버튼 클릭 */

        Toast.makeText(HomeActivity.this, "친구목록 버튼을 클릭했다!", Toast.LENGTH_SHORT).show();

      }
    });


    checkBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* 좌석예약 버튼 클릭 */
        Toast.makeText(HomeActivity.this, "좌석예약 버튼을 클릭했다!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeActivity.this, CheckInHomeActivity.class);
        startActivity(intent);

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
