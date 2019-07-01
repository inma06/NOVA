package chat.roomList;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.teamnova.nova.R;

import java.util.ArrayList;

public class RoomListMainActivity extends AppCompatActivity {



  private ArrayList<MainData> arrayList;
  private MainAdapter mainAdapter;
  private RecyclerView recyclerView;
  private LinearLayoutManager linearLayoutManager;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_room_list_main);



    recyclerView = (RecyclerView) findViewById(R.id.rv);
    linearLayoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(linearLayoutManager);

    arrayList = new ArrayList<>();

    mainAdapter = new MainAdapter(arrayList);
    recyclerView.setAdapter(mainAdapter);


    //방 추가버튼 CASE1
    Button btn_add1 = (Button) findViewById(R.id.btn_add1);
    btn_add1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        MainData mainData = new MainData(R.drawable.profile,
            "아타나시오",
            "1:1대화일때 입니다1:1대화일때 입니다1:1대화일때 입니다1:1대화일때 입니다1:1대화일때 입니다1:1대화일때 입니다",
            "어제",
            "");

        arrayList.add(mainData);
        mainAdapter.notifyDataSetChanged();
      }
    });


    //방 추가버튼 CASE2
    Button btn_add2 = (Button) findViewById(R.id.btn_add2);
    btn_add2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        MainData mainData = new MainData(R.drawable.profile,
            "아타나시오아타나시오아타나시오아타나시오아타나시오아타나시오아타나시오아타나시오아타나시오아타나시오",
            "1:1대화일때 입니화일때 입니화일때 입니화일때 입니화일때 입니화일때 입니화일때 입니화일때 입니화일때 입니화일때 입니다1:1대화일때 입니다1:1대화일때 입니다1:1대화일때 입니다1:1대화일때 입니다1:1대화일때 입니다",
            "23:59",
            "3");

        arrayList.add(mainData);
        mainAdapter.notifyDataSetChanged();
      }
    });


    //방 추가버튼 CASE3
    Button btn_add3 = (Button) findViewById(R.id.btn_add3);
    btn_add3.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        MainData mainData = new MainData(R.drawable.profile,
            "아타나시오아타나시오아타나시오아타나시오시오",
            "1:1대화일때 입니다1:1대화일때 입니다1:1대화일때 입니다1:1화일때 입니다1:1화일때 입니다1:1화일때 입니다1:1화일때 입니다1:1화일때 입니다1:1화일때 입니다1:1화일때 입니다1:1대화일때 입니다1:1대화일때 입니다1:1대화일때 입니다",
            "6월 17일",
            "253");

        arrayList.add(mainData);
        mainAdapter.notifyDataSetChanged();
      }
    });

  }
}
