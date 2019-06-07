package ChatClient;


import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamnova.nova.R;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
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

  private static final String port = "9999";
  private static final String ipText = "13.124.10.133"; // (AWS 채팅전용 서버 java Server ) *IP지정으로 사용시에 쓸 코드
//  String streammsg = ""; // streammsg ???
  TextView showText;
  ScrollView scrollView;
//  Button connectBtn;
  Button Button_send; // 채팅 보내기 버튼
  Button Button_exit; // 채팅방 나가기 버튼
//  EditText ip_EditText;
//  EditText port_EditText;
  EditText editText_massage;
  Handler msghandler;

  SocketClient client;
  ReceiveThread receive;
  SendThread send;
  Socket socket;

  PipedInputStream sendstream = null;
  PipedOutputStream receivestream = null;

  LinkedList<SocketClient> threadList;



/*  // 소켓 설정
  public void setSocket() {
    try {
      client = new SocketClient(ipText,
          Integer.toString(port)); // 접속할 소켓 서버의 주소를 입력합니다. (ip, port)
      threadList.add(client); // 'LinkedList'에 'SocketClient'를 추가합니다.
      client.start(); // 'SocketClient'를 시작합니다.
    } catch (Exception e) {
      e.printStackTrace();
      // 소켓연결 실패시
      Log.e(TAG, "setSocket: FAIL!!!!!!!!!! 소켓연결 실패!!");

    }


  }*/
/*


  @Override
  protected void onPause() {
    super.onPause();
    // onPause -> "Other Activity" up on "This Activity" ( 현재액티비티에서 다른 액티비티로 전환될 때 )
    Log.e(TAG, "onPause: OK" );
    //TODO : 소켓 연결 disconnect 해야함.
  }

  @Override
  protected void onResume() {
    super.onResume();
    // Resume OK
    Log.e(TAG, "onResume: OK");

//    setSocket(); // 소켓 연결

    Log.e(TAG, "소켓연결 OK" );

  }
*/

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat_room_main);

//    ip_EditText = (EditText) findViewById(R.id.ip_EditText);
//    port_EditText = (EditText) findViewById(R.id.port_EditText);
//    connectBtn = (Button) findViewById(R.id.connect_Button);
    showText = (TextView) findViewById(R.id.showText_TextView);
    scrollView = (ScrollView) findViewById(R.id.showScrollView);

    editText_massage = (EditText) findViewById(R.id.editText_massage);
    Button_send = (Button) findViewById(R.id.Button_send);
    Button_exit = (Button) findViewById(R.id.Button_exit);
    threadList = new LinkedList<ChatRoomMainActivity.SocketClient>();

//    ip_EditText.setText(ipText);
//    port_EditText.setText("9999");

    /*
    *
    * 소켓 연결부분
    *
    * */

    //Client 연결부
    client = new SocketClient(ipText, port);
    threadList.add(client);
    client.start();

    Toast toast = Toast.makeText(getApplicationContext(), "채팅방에 입장하였습니다.", Toast.LENGTH_LONG);
    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
    toast.show();
//    Toast.makeText(ChatRoomMainActivity.this, "채팅방에 입장하였습니다.", Toast.LENGTH_SHORT).show();




    // ReceiveThread를통해서 받은 메세지를 Handler로 MainThread에서 처리(외부Thread에서는 UI변경이불가)
    msghandler = new Handler() {
      @Override
      public void handleMessage(Message hdmsg) {
        if (hdmsg.what == 1111) { // 여러 핸들러들을 구분하기 위한 고유번호
          
          showText.append(hdmsg.obj.toString() + "\n");
          scrollView.fullScroll(View.FOCUS_DOWN);

          // showText -> 어떤 사용자가 접속했는지
          // ... 사용자 접속을 알립니다.
          Log.e(TAG, "handleMessage: " +  hdmsg.obj + "| append OK");
          System.out.println("=======================");
          System.out.println("handleMessage hashCode ---> " + hdmsg.obj.hashCode());
          System.out.println("handleMessage object ---> " + hdmsg.obj);
          System.out.println("=======================");
        }
      }
    };



/*    // 연결버튼 클릭 이벤트
    connectBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View arg0) {
        //Client 연결부
        client = new SocketClient(ip_EditText.getText().toString(),
            port_EditText.getText().toString());
        threadList.add(client);
        client.start();
        Toast.makeText(ChatRoomMainActivity.this, "채팅방에 입장하였습니다.", Toast.LENGTH_SHORT).show();
      }
    });*/


    Button_exit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Client 연결 해제
        onDestroy();

        Toast.makeText(ChatRoomMainActivity.this, "채팅방에서 퇴장했습니다. (미구현)", Toast.LENGTH_SHORT).show();
      }
    });


    //전송 버튼 클릭 이벤트
    Button_send.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View arg0) {

        //SendThread 시작
        if (editText_massage.getText().toString() != null) {
          send = new SendThread(socket);
          send.start();

          //시작후 edittext 초기화
          editText_massage.setText("");
        }
      }
    });
  }

  class SocketClient extends Thread {
    boolean threadAlive;
    String ip;
    String port;
    String userUUID;

    //InputStream inputStream = null;
    OutputStream outputStream = null;
    BufferedReader br = null;

    private DataOutputStream output = null;

    public SocketClient(String ip, String port) {
      threadAlive = true;
      this.ip = ip;
      this.port = port;
    }

    @Override
    public void run() {

      try {
        // 연결후 바로 ReceiveThread 시작
//        socket = new Socket(ip, Integer.parseInt(port));

        Log.e("소켓연결부분", "run: 111111111111111");
        socket = new Socket(ip, 9999);
        Log.e("소켓연결부분", "run: 22222222222222222");
        //inputStream = socket.getInputStream();
        output = new DataOutputStream(socket.getOutputStream());
        Log.e("소켓연결부분", "run: 333333333333333333");
        receive = new ReceiveThread(socket);
        Log.e("소켓연결부분", "run: 444444444444444444");
        receive.start();
        Log.e("소켓연결부분", "run: 555555555555555555");

        //wifi -> mac주소를 받아오기위해 설정  ( TODO: *팀노바 사무실 wifi 접근 권한으로 못받아 올 수 있음 )
        /*WifiManager mng = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo info = mng.getConnectionInfo();
        userUUID = info.getMacAddress();*/


        //TODO: 사용자의 고유번호를 전송해야함. 지금은 닉네임으로 해서 겹칠 우려가 있음. 이것을 나중에 처리해주자 2019-06-07
//        userUUID = HomeActivity.mUserID;
//        userUUID = HomeActivity.mUserNo;
        userUUID = HomeActivity.mNickName;

        //userUUID 전송
        output.writeUTF(userUUID);
        Log.e("맥주소 전송 완료", "9999999999999999999999");

      } catch (IOException e) {
        e.printStackTrace();
        Log.e("실패","ReceiveThread 시작 실패");
      }
    }
  }

  class ReceiveThread extends Thread {
    private Socket socket = null;
    DataInputStream input;

    public ReceiveThread(Socket socket) {
      this.socket = socket;
      try{
        input = new DataInputStream(socket.getInputStream());
      }catch(Exception e){
      }
    }
    // 메세지 수신후 Handler로 전달
    public void run() {
      try {
        while (input != null) {

          String msg = input.readUTF();
          if (msg != null) {
            Log.d(ACTIVITY_SERVICE, "test");

            Message hdmsg = msghandler.obtainMessage(); // hdmsg -> 핸들러 메시지
            hdmsg.what = 1111; // 메시지큐를 구분하는 값(사용자 정의 값)
            hdmsg.obj = msg;
            Log.e(TAG, "run: =============안되냐 되냐");
            msghandler.sendMessage(hdmsg);
            Log.d(ACTIVITY_SERVICE,hdmsg.obj.toString());
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  class SendThread extends Thread {
    private Socket socket;
    String sendmsg = editText_massage.getText().toString();
    DataOutputStream output;

    public SendThread(Socket socket) {
      this.socket = socket;
      try {
        output = new DataOutputStream(socket.getOutputStream());
      } catch (Exception e) {
      }
    }







    // 채팅창 부분 ( 본문 )
    public void run() {

      try {

        // 메세지 전송부 (누군지 식별하기위한 방법으로 mac를 사용)
        Log.d(ACTIVITY_SERVICE, "1111");
        String userNickName = null;
        WifiManager mng = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo info = mng.getConnectionInfo();
//        userUUID = info.getMacAddress();
        //TODO: 사무실 wifi 맥주소 못가져옴

        //TODO : 채팅메시지 입력시 누가 입력했는지 나타내는 부분 -------->   "봉호짱"( 이부분 ) : 안녕바보야!!
        userNickName = HomeActivity.mNickName.toString();
//        userUUID = HomeActivity.mUserID.toString();

        if (output != null) {
          Log.e(TAG, "mac ---------->" + userNickName.toString());
          Log.e(TAG, "111111111111111111111111111111111111112222222222222222333333333333333");
//          output.writeUTF(userNickName + "  :  " + sendmsg);
          output.writeUTF(sendmsg);

          if (sendmsg != null) {
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


