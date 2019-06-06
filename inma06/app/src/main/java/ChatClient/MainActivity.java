package ChatClient;


import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class MainActivity extends Activity {

  private static int port = 9999;
  private static final String ipText = "13.124.10.133"; // (AWS 채팅전용 서버 java Server ) *IP지정으로 사용시에 쓸 코드
  String streammsg = ""; // 스트림 msg
  TextView showText;
  Button connectBtn;
  Button Button_send;
  EditText ip_EditText;
  EditText port_EditText;
  EditText editText_massage;
  Handler msghandler;

  SocketClient client;
  ReceiveThread receive;
  SendThread send;
  Socket socket;

  PipedInputStream sendstream = null;
  PipedOutputStream receivestream = null;

  LinkedList<SocketClient> threadList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ip_EditText = (EditText) findViewById(R.id.ip_EditText);
    port_EditText = (EditText) findViewById(R.id.port_EditText);
    connectBtn = (Button) findViewById(R.id.connect_Button);
    showText = (TextView) findViewById(R.id.showText_TextView);
    editText_massage = (EditText) findViewById(R.id.editText_massage);
    Button_send = (Button) findViewById(R.id.Button_send);
    threadList = new LinkedList<MainActivity.SocketClient>();

    ip_EditText.setText(ipText);
    port_EditText.setText("9999");

    // ReceiveThread를통해서 받은 메세지를 Handler로 MainThread에서 처리(외부Thread에서는 UI변경이불가)
    msghandler = new Handler() {
      @Override
      public void handleMessage(Message hdmsg) {
        if (hdmsg.what == 1111) { // TODO: what is "1111" ??
          showText.append(hdmsg.obj.toString() + "\n");
        }
      }
    };

    // 연결버튼 클릭 이벤트
    connectBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View arg0) {
        //Client 연결부
        client = new SocketClient(ip_EditText.getText().toString(),
            port_EditText.getText().toString());
        threadList.add(client);
        client.start();
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
    String mac;

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
        mac = info.getMacAddress();*/

        mac = HomeActivity.mUserID;

        //mac 전송
        output.writeUTF(mac);
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

            Message hdmsg = msghandler.obtainMessage();
            hdmsg.what = 1111;
            hdmsg.obj = msg;
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

    public void run() {

      try {

        // 메세지 전송부 (누군지 식별하기위한 방법으로 mac를 사용)
        Log.d(ACTIVITY_SERVICE, "11111");
        String mac = null;
        WifiManager mng = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo info = mng.getConnectionInfo();
//        mac = info.getMacAddress();
        //TODO: 사무실 wifi 맥주소 못가져옴
        mac = HomeActivity.mUserID.toString();

        if (output != null) {
          output.writeUTF(mac + "  :  " +sendmsg);
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


