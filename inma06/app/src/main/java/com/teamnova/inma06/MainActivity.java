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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamnova.nova.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.ParseException;
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
//  public static String IP_TEXT = "192.168.0.26"; // Test Server ( local )
  public static String IP_TEXT = "13.124.10.133"; // Real Server
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
          /*
          * 서버에서 보낸 Json Data 예시
          * {
              "isImage" : false,
              "imageStr" : "BASE64 IMAGE String TEST...",
              "serverTime" : "오후 11:49",
              "userMsg" : "123",
              "userId" : "223",
              "roomId" : "10"
             }
          *
          * */
          try {
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObj = (JsonObject) jsonParser.parse(msg.obj.toString());
            String userMsg = jsonObj.get("userMsg").getAsString();
            String serverTime = jsonObj.get("serverTime").getAsString();
            String userId = jsonObj.get("userId").getAsString();
            String roomId = jsonObj.get("roomId").getAsString();
            Boolean isImage =  jsonObj.get("isImage").getAsBoolean();
//            String imageStr = jsonObj.get("imageStr").getAsString();
            mimicOtherMessage(userMsg, null, isImage); //메시지 수신시 아이템 추가 메서드
            System.out.println("userMsg >> "+ userMsg);
            System.out.println("imageStr >> " );
            System.out.println("isImage >> " + isImage);


          } catch(Exception e){
            e.printStackTrace();
          }
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
        sendToServer = new SendThread(socket, message, null, false);
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
        // 이미지를 BASE64로 변환하고 String image 에 담는다.

        String image = ""; // BASE64 to String Converting
        image = "data:image/png;base64,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";
        if(TextUtils.isEmpty(image)) {
          return;
        } else {
          // 클라이언트에 보이는 이미지
          sendMessage(null, image, true);
          // 서버로 보내지는 이미지
          sendToServer = new SendThread(socket, null, image, true);
          sendToServer.start();

        }



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
        JSONObject jsonObject = new JSONObject();
        try {
          jsonObject.put("roomNo", ROOM_ID);
          jsonObject.put("userNo", USER_ID);
        } catch (JSONException e) {
          e.printStackTrace();
        }

        out.writeUTF(jsonObject.toString());
//        out.writeUTF(roomNo + "&" + userNo);


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
          if (msg != null) {
            // 핸들러에게 전달할 큐 객체
            Message hdmsg = msgHandler.obtainMessage(); // hdmsg -> 핸들러 메시지
            hdmsg.what = 1111; // 핸들러 큐를 구분하는 값(사용자 정의 값)
            // 서버에서 받은 메시지
            hdmsg.obj = msg;
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
    String base64Image;
    Boolean isImage;

    DataOutputStream output;

    public SendThread(Socket socket, String sendMsg, String base64Image, Boolean isImage) {
      this.socket = socket;
      this.sendMsg = sendMsg;
      this.isImage = isImage;
      this.base64Image = base64Image;
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
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("roomId", ROOM_ID);
            jsonObject.put("userId", USER_ID);
            jsonObject.put("targetId", TARGET_ID);
            jsonObject.put("userMsg", sendMsg);
            jsonObject.put("isImage", isImage);
            jsonObject.put("userImage", base64Image);

            output.writeUTF(jsonObject.toString());
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      } catch (NullPointerException npe) {
        npe.printStackTrace();
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }



}
