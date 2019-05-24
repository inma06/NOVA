package ChatClient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teamnova.nova.R;

import java.util.ArrayList;

public class ChatViewActivity extends AppCompatActivity {

  private static String TAG = "ChatViewActivity";

  private ArrayList<Dictionary> mArrayList;
  private CustomAdapter mAdapter;
  private RecyclerView mRecyclerView;
  private LinearLayoutManager mLinearLayoutManager;
  private Button sendMsgBtn;
  private EditText inputMsgEt;

  @Override
  protected void onResume() {
    super.onResume();

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat_view);

    mRecyclerView = (RecyclerView) findViewById(R.id.chatview_main_list);
    mLinearLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLinearLayoutManager);

    sendMsgBtn = (Button) findViewById(R.id.send_Btn);
    inputMsgEt = (EditText) findViewById(R.id.inputMsg_Et);


    // ChatViewActivity 에서 RecyclerView 의 데이터에 접근시 사용됩니다.
    mArrayList = new ArrayList<>();

    // RecyclerView 를 위해 CustomAdapter 를 사용합니다.
    mAdapter = new CustomAdapter(mArrayList);
    mRecyclerView.setAdapter(mAdapter);

    // RecyclerView의 줄(row) 사이에 수평선을 넣기위해 사용됩니다.
    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
        mLinearLayoutManager.getOrientation());
    mRecyclerView.addItemDecoration(dividerItemDecoration);


    sendMsgBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(ChatViewActivity.this, inputMsgEt.getText().toString() + " 를 전송 한다.", Toast.LENGTH_SHORT).show();
      }
    });


    // Dictionary 생성자를 사용하여 ArrayList에 삽입할 데이터를 만듭니다.
    /*
    * 하단 에디트 텍스트에 채팅 메시지 입력후 전송 버튼 누를 시
    * 서버로 메시지 전송
    *
    * 서버에서 보내준 메시지 내용 리사이클러뷰에 출력.
    * */

//    Dictionary dict = new Dictionary("",1,"");
//    mArrayList.add(dict); // RecyclerView의 마지막 줄에 삽입
//
//    mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영



  }

}
