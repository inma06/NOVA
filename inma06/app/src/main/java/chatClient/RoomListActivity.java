package chatClient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.teamnova.nova.R;

public class RoomListActivity extends AppCompatActivity {

  private Button addBtn;
  private Button room1;
  private Button room2;
  private Button room3;
  private Button room4;
  private Button room5;
  private Button room6;
  private Button room7;

  public static String mRoomNo = "";




  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_room_list);

    addBtn = (Button) findViewById(R.id.addRoomBtn);
    room1 = (Button) findViewById(R.id.room1);
    room2 = (Button) findViewById(R.id.room2);
    room3 = (Button) findViewById(R.id.room3);
    room4 = (Button) findViewById(R.id.room4);
    room5 = (Button) findViewById(R.id.room5);
    room6 = (Button) findViewById(R.id.room6);
    room7 = (Button) findViewById(R.id.room7);

    addBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast toast = Toast.makeText(RoomListActivity.this,
            "아직 구현되지 않았습니다. SORRY :( ", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

      }
    });

    room1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast toast = Toast.makeText(RoomListActivity.this,
            room1.getText()+"방에 입장하였습니다.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        mRoomNo = "1";
        Intent intent = new Intent(RoomListActivity.this, ChatRoomMainActivity.class);
        startActivity(intent);
      }
    });

    room2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast toast = Toast.makeText(RoomListActivity.this,
            room2.getText()+"방에 입장하였습니다.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();


        mRoomNo = "2";
        Intent intent = new Intent(RoomListActivity.this, ChatRoomMainActivity.class);
        startActivity(intent);

      }
    });

    room3.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast toast = Toast.makeText(RoomListActivity.this,
            room3.getText()+"방에 입장하였습니다.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        mRoomNo = "3";

        Intent intent = new Intent(RoomListActivity.this, ChatRoomMainActivity.class);
        startActivity(intent);

      }
    });

    room4.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast toast = Toast.makeText(RoomListActivity.this,
            room4.getText()+"방에 입장하였습니다.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        mRoomNo = "4";
        Intent intent = new Intent(RoomListActivity.this, ChatRoomMainActivity.class);
        startActivity(intent);

      }
    });

    room5.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast toast = Toast.makeText(RoomListActivity.this,
            room5.getText()+"방에 입장하였습니다.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();


        mRoomNo = "5";
        Intent intent = new Intent(RoomListActivity.this, ChatRoomMainActivity.class);
        startActivity(intent);
      }
    });

    room6.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast toast = Toast.makeText(RoomListActivity.this,
            room6.getText()+"방에 입장하였습니다.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();


        mRoomNo = "6";
        Intent intent = new Intent(RoomListActivity.this, ChatRoomMainActivity.class);
        startActivity(intent);
      }
    });

    room7.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast toast = Toast.makeText(RoomListActivity.this,
            room7.getText()+"방에 입장하였습니다.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();


        mRoomNo = "7";
        Intent intent = new Intent(RoomListActivity.this, ChatRoomMainActivity.class);
        startActivity(intent);

      }
    });

  }
}
