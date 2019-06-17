package com.teamnova.inma06;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.teamnova.nova.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import Main.HomeActivity;

public class MainActivity extends AppCompatActivity {

  private RecyclerView mRecyclerView;
  private ImageView mButtonSend;
  private EditText mEditTextMessage;
  private ImageView mImageView;
  private ChatMessageAdapter mAdapter;


  /* TEST CODE start */
  public static int PORT = 9999;
  public static String IP_TEXT = "13.124.10.133";
  public static String USER_ID = HomeActivity.mNickName;
  public static String ROOM_ID = "10";
  public static String TARGET_ID = "";
  Socket socket;
  ReceiveThread receive;
  SocketClient client = new SocketClient(ROOM_ID, USER_ID);
  Handler msgHandler;
  SendThread sendToServer;

  LinkedList<SocketClient> threadList;
  /* TEST CODE end */

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    mButtonSend = (ImageView) findViewById(R.id.btn_send);
    mEditTextMessage = (EditText) findViewById(R.id.et_message);
    mImageView = (ImageView) findViewById(R.id.iv_image);

    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
    mRecyclerView.setAdapter(mAdapter);

    threadList = new LinkedList<MainActivity.SocketClient>();

    // 소켓 연결
    threadList.add(client);
    client.start();

    // 메시지 수신 핸들러
    msgHandler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
        if (msg.what == 1111) { // 여러 핸들러들을 구분하기 위한 고유번호
          // 메시지가 수신되면 리사이클러뷰에 아이템을 추가한다.
          Log.e("받은 메시지", msg.obj.toString());
          mimicOtherMessage(msg.obj.toString(), null, false); //메시지 수신시 아이템 추가 메서드
        }
      }
    };


    // 메시지 보내기 버튼 리스너
    mButtonSend.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String message = mEditTextMessage.getText().toString();
        if (TextUtils.isEmpty(message)) {
          return;
        }
        //클라이언트에 먼저 보이게 하고, 서버로 채팅메시지를 보낸다.
        sendMessage(message, null, false);
        sendToServer = new SendThread(socket, message, false);
        sendToServer.start();
        mEditTextMessage.setText("");
      }
    });

    // 이미지 보내기 버튼 리스너
    // 앨범 액티비티로 이동한다. TODO : 다중이미지 전송 가능하게
    mImageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        sendMessage(); 테스트용
        String image = ""; // BASE64 to String Converting
        if(TextUtils.isEmpty(image)) {
          return;
        }
        sendMessage(null, image, true);
        image = "";

        Toast.makeText(MainActivity.this, "이미지전송 버튼, 앨범으로 이동합니다.", Toast.LENGTH_SHORT).show();
      }
    });
  }

  // 메시지 전송(텍스트 or 이미지)
  // 보낼텍스트( 없으면 null ), 보낼이미지( 없으면 null ), isImage (이미지 전송시 true)
  // 먼저 클라이언트의 뷰에 출력하는 메서드
  private void sendMessage(String message, String image, Boolean isImage) {
    ChatMessage chatMessage = new ChatMessage(message, image, true, isImage);
    mAdapter.add(chatMessage);
    mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
  }

  // 메시지 수신(텍스트 or 이미지)
  // 뷰에 출력하는 메서드
  private void mimicOtherMessage(String message, String image, Boolean isImage) {
    ChatMessage chatMessage = new ChatMessage(message, image, false, isImage);
    mAdapter.add(chatMessage);
    mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
  }

  // SocketClient 클래스
  /*
   * 소켓연결을 하기 위한 클래스
   * 방번호와 유저번호로 서버로 연결을 한다.
   * */
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
        socket = new Socket(IP_TEXT, PORT);
        // 메시지를 서버에 전달 할 수 있는 통로
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        Log.e("성공", "데이터 전송 성공11");
        // 서버에 초기 데이터 전송 ( 방번호와 유저번호가 담겨서 간다 ) - 식별자
        out.writeUTF(roomNo + "&" + userNo);
        receive = new ReceiveThread(socket);
        receive.start();
        Log.e("성공", "데이터 전송 성공22");
        Log.e("성공", "방번호 : " + roomNo);
        Log.e("성공", "유저번호 : " + userNo);
      } catch (IOException e) {
        e.printStackTrace();
        Log.e("실패", "ReceiveThread 시작 실패");
      }
    }
  } // SocketClient 클래스 끝

  //메시지 수신 하는 ReceiveThread 클래스 시작
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
    // 메세지 수신후 Handler 로 전달
    public void run() {
      try {
        while (input != null) {
          // 채팅 서버로 부터 받은 메시지
          String msg = input.readUTF();
          // & 구분자를 기준으로 잘라주어야함.
          // 방번호, 보낸사람, 받는사람, 메시지, 타임
          String[] filter;
          filter = msg.split("&");
          try {
            System.out.println("filter[0] -> " + filter[0]); // 보낸사람 userNo
            System.out.println("filter[1] -> " + filter[1]); // 메시지 내용
            System.out.println("filter[2] -> " + filter[2]); // 보낸 시각 ( 오전 01:18 )
          }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
          }
          String showMsg = msg;

          if (msg != null) {
            // 핸들러에게 전달할 메시지 객체
            Message hdmsg = msgHandler.obtainMessage(); // hdmsg -> 핸들러 메시지
            // 핸들러에게 전달할 메시지의 식별자
            hdmsg.what = 1111; // 메시지큐를 구분하는 값(사용자 정의 값)
            // 메시지의 본문
            hdmsg.obj = showMsg;
            // 핸들러에게 메시지 전달 ( 화면 처리 )
            msgHandler.sendMessage(hdmsg);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  } // ReceiveThread 클래스 끝


  // SendThread 클래스 시작
  /*
   * 서버로 메시지를 발신하는 쓰레드
   *
   * */

  // 서버로 발신하는 쓰레드
  class SendThread extends Thread {
    Socket socket;
    String sendMsg;
    Boolean isImage;
    DataOutputStream output;

    public SendThread(Socket socket, String sendMsg, Boolean isImage) {
      this.socket = socket;
      this.sendMsg = sendMsg;
      this.isImage = isImage;
      try {
        //채팅 서버로 메시지를 보내기 위한 스트림 생성
        output = new DataOutputStream(socket.getOutputStream());
      } catch (Exception e) {
      }
    }

    // 서버로 메시지 전송 ( 서버의 temp 변수로 전달된다. )
    public void run() {
      try {
        if ( output != null ) {
          if (sendMsg != null) {

            // 서버로 메시지 전송하는 부분
            // TODO: Json 으로 변환하여 보내기

            // mRoomNo( 방번호 ) , userNo( 유저 번호 ), targetUserNo ( 상대방 번호 ),
            // sendMsg (보내는 메시지) , isImage( 이미지인지 텍스트인지 구분)

            // 구분 문자 "&"
            output.writeUTF(ROOM_ID+"&"+USER_ID+"&"+TARGET_ID+"&"+ sendMsg + "&" + isImage.toString());
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
