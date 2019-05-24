package ChatClient;   //패키지는 본인 것으로 수정하셔요



import java.io.BufferedReader;  //우와 많다 ㅎㅎ..
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.teamnova.nova.R;

public class ChatTest extends Activity {    //메인 activity 시작!

  BufferedReader in;      //서버로부터 온 데이터를 읽는다.
  PrintWriter out;        //서버에 데이터를 전송한다.

  Socket socket = null;
  DataInputStream inputStream = null;
  DataOutputStream outputStream = null;


  EditText input;         //화면구성
  Button button;          //화면구성
  TextView output;        //화면구성
  String data;            //

  @Override
  protected void onCreate(Bundle savedInstanceState) {   //앱 시작시  초기화설정
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat_test);
//start
    input = (EditText) findViewById(R.id.input); // 글자입력칸을 찾는다.
    button = (Button) findViewById(R.id.button); // 버튼을 찾는다.
    output = (TextView) findViewById(R.id.output); // 글자출력칸을 찾는다.
// 버튼을 누르는 이벤트 발생, 이벤트 제어문이기 때문에 이벤트 발생 때마다 발동된다. 시스템이 처리하는 부분이 무한루프문에
//있더라도 이벤트가 발생하면 자동으로 실행된다.
    button.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
//버튼이 클릭되면 소켓에 데이터를 출력한다.
        Log.w("NETWORK", " " + data);
         // 데이터가 널이 아닐때
        Thread thread = new Thread(new Send(outputStream, input.getText().toString()));
        thread.start();
      }
    });
  }

  @Override
  protected void onStop() {  //앱 종료시
    super.onStop();
    try {
      socket.close(); //소켓을 닫는다.
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}