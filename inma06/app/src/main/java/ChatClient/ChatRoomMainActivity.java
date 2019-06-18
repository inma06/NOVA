package ChatClient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamnova.nova.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;

import Main.HomeActivity;

/*
* TODO : 채팅방 수정할 사항
*
* 1. 채팅 내용 ---> 페이징 처리,
* 2. 스크롤뷰 -> 리사이클러뷰로 변경
* 3. 레이아웃 개선 생각하기.
*
*
*
* */
public class ChatRoomMainActivity extends Activity {

  private static String TAG = "ChatRoomMainActivity";
  public static int port = 9999;

  public static String ipText = "13.124.10.133"; // (AWS 채팅전용 서버 java Server ) *IP지정으로 사용시에 쓸 코드
//  public static final String ipText = "192.168.0.26"; // (7사무실 KT_GiGA_2G_Wave2_4108 IP) *IP지정으로 사용시에 쓸 코드
  public static String userNo = HomeActivity.mNickName; // 보내는 유저의 고유번호
  public static String roomNo = RoomListActivity.mRoomNo; // 방의 고유번호
  public static String targetUserNo = ""; // 받는 유저 번호 (특정 유저에게 보낼때)

//  String streammsg = ""; // streammsg ???
  TextView showText;
  ScrollView scrollView;

//  Button connectBtn;
  Button Button_send; // 채팅 보내기 버튼
  Button Button_exit; // 채팅방 나가기 버튼
//  EditText ip_EditText;
//  EditText port_EditText;
  EditText editText_massage;
  Handler msgHandler;

  SocketClient client = new SocketClient(roomNo, userNo);
  ReceiveThread receive;
  SendThread send;
  Socket socket;

  private DataInputStream dis = null;
  private DataOutputStream dos = null;

  PipedInputStream sendstream = null;
  PipedOutputStream receivestream = null;

  LinkedList<SocketClient> threadList; // 클라이언트를 리스트에 담는다.
  ChatRoomMainActivity context;



  private long pressedTime;
  // 뒤로가기 버튼 눌렀을 때

  @Override
  public void onBackPressed() {
//    super.onBackPressed();
    if ( pressedTime == 0 ) {
      Toast.makeText(this, "한번 더 누르면 채팅방에서 나갑니다. \n이전 대화는 로컬 저장소에 저장됩니다.", Toast.LENGTH_SHORT).show();
      pressedTime = System.currentTimeMillis();
    }

    else {
      int seconds = (int) (System.currentTimeMillis() - pressedTime);

      if ( seconds > 2000 ) {
        pressedTime = 0;
      }
      else {
        finish();
        /* 대화를 저장하는 로직 -> SPF 이용 하기
         *
          * 대화를 리스트에 담고 쉐어드 프리퍼런스에 키값으로 저장한다.
          *
          *
          *
          * */
      }
    }

  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat_room_main);

    showText = (TextView) findViewById(R.id.showText_TextView);
    scrollView = (ScrollView) findViewById(R.id.showScrollView);
    editText_massage = (EditText) findViewById(R.id.editText_massage);
    Button_send = (Button) findViewById(R.id.Button_send);
    Button_exit = (Button) findViewById(R.id.Button_exit);
    threadList = new LinkedList<ChatRoomMainActivity.SocketClient>();


    //Client 연결부
    threadList.add(client);
    client.start();
    // ( 메시지가 오면 처리하는 핸들러 )
    msgHandler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
        if (msg.what == 1111) { // 여러 핸들러들을 구분하기 위한 고유번호
          // 메시지가 왔다면
          Log.e("받은 메시지", msg.obj.toString());
          showText.append(msg.obj.toString() + "\n");
          scrollView.fullScroll(View.FOCUS_DOWN);
          // 수신 1
//          messageContent = new chattingMessageContent(1, msgFilter[0], targetNickName, msgFilter[1], msgFilter[2]);
        }
      }
    };


    Button_exit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 방 나가기 버튼클릭 ( 소켓 연결 해제 )

        try {
          threadList.remove(client);

          socket.close();
          receivestream.close();
          sendstream.close();
//          dis.close();
//          dos.close();
        } catch (IOException e) {
          e.printStackTrace();
          Log.e(TAG, "onClick: 방나가기 실패!!!!!!!!!!!!!!!!");
        }

        Toast.makeText(ChatRoomMainActivity.this, "채팅방에서 퇴장했습니다. (미구현)", Toast.LENGTH_SHORT).show();
        finish();
//        Intent intent = new Intent(ChatRoomMainActivity.this, HomeActivity.class);
        //TODO: 액티비티를 종료시키고 기존 액티비티를 새로 생성된 액티비티로 교체하는 플래그 ** 공부요망.
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);

      }
    });


    //전송 버튼 클릭 이벤트
    Button_send.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String message = editText_massage.getText().toString();
        if (message == null || TextUtils.isEmpty(message) || message.equals("")) {
          Toast.makeText(ChatRoomMainActivity.this, "메시지를 입력해주세요", Toast.LENGTH_SHORT).show();
        } else {
          send = new SendThread(socket, message);
          send.start();
          editText_massage.setText("");
          showText.append("나 : " + message + "\n");
          scrollView.fullScroll(View.FOCUS_DOWN);

        }
      }
    });
  }



  // 아웃풋 스트림 ( 메시지 Send 보내기 부분. )
  class SocketClient extends Thread {
    DataInputStream in = null;
    DataOutputStream out = null;
    String roomNo;
    String userNo;

    public SocketClient(String roomNo, String userNo) {
      this.roomNo = roomNo;
      this.userNo = userNo;
    }

    @Override
    public void run() {

      try {
        // 채팅 서버에 접속 ( 연결 ) ( 서버쪽 ip와 포트 )
        socket = new Socket(ipText, port);
        // 메시지를 서버에 전달 할 수 있는 통로
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        // 서버에 초기 데이터 전송 ( 방번호와 유저번호가 담겨서 간다 ) - 식별자
        out.writeUTF(roomNo+"&"+userNo);

        receive = new ReceiveThread(socket);
        receive.start();

      } catch (IOException e) {
        e.printStackTrace();
        Log.e("실패","ReceiveThread 시작 실패");
      }
    }
  }


  // 리시브 쓰레드 ( 메시지 수신부 )
  class ReceiveThread extends Thread {
    Socket socket = null;
    DataInputStream input = null;

    public ReceiveThread(Socket socket) {
      this.socket = socket;

      try{
        // 채팅서버로부터 메시지 받기 위한 스트림 생성.
        input = new DataInputStream(socket.getInputStream());
      }catch(Exception e){
      }
    }
    // 메세지 수신후 Handler로 전달
    public void run() {
      try {
        while (input != null) {
          // 채팅 서버로 부터 받은 메시지
          String msg = input.readUTF();

          // JSON String 으로 받은 메시지를 알맞게 Parsing 한다.

          try {

            } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

          try {
          }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
          }

//          String showMsg = filter[1].toString() + ": " + filter[3] + "보낸시각" + filter[4];

          if (msg != null) {
            // 핸들러에게 전달할 메시지 객체
            Message hdmsg = msgHandler.obtainMessage(); // hdmsg -> 핸들러 메시지
            // 핸들러에게 전달할 메시지의 식별자
            hdmsg.what = 1111; // 메시지큐를 구분하는 값(사용자 정의 값)
            // 메시지의 본문
//            hdmsg.obj = filter[0].concat(": "+filter[1]).concat("  |"+filter[2
            hdmsg.obj = msg;
            // 핸들러에게 메시지 전달 ( 화면 처리 )
            msgHandler.sendMessage(hdmsg);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  // 서버로 발신하는 쓰레드
  class SendThread extends Thread {
    Socket socket;
    String sendMsg;
    DataOutputStream output;

    public SendThread(Socket socket, String sendMsg) {
      this.socket = socket;
      this.sendMsg = sendMsg;
      try {
        //채팅 서버로 메시지를 보내기 위한 스트림 생성 ---------------------------------> 여기부터 다시
        output = new DataOutputStream(socket.getOutputStream());
      } catch (Exception e) {
      }
    }

    // 서버로 메시지 전송 ( 서버의 temp 로 전달된다. )
    public void run() {
      try {
        if ( output != null ) {
          if (sendMsg != null) {

            // 서버로 메시지 전송하는 부분
            // TODO: Json 으로 변환하여 보내기
            // mRoomNo( 방번호 ) , userNo( 유저 번호 ), targetUserNo ( 상대방 번호 ), sendMsg (보내는 메시지)
            // 구분자 "&"


            output.writeUTF(roomNo+"&"+userNo+"&"+targetUserNo+"&"+ sendMsg);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      } catch (NullPointerException npe) {
        npe.printStackTrace();

      }
    }
  }
}


