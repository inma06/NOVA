package friendList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.teamnova.nova.R;

import java.util.ArrayList;

public class FriendListMainActivity extends AppCompatActivity {



  private ArrayList<MainData> arrayList;
  private MainAdapter mainAdapter;
  private RecyclerView recyclerView;
  private LinearLayoutManager linearLayoutManager;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_friend_list_main);



    recyclerView = (RecyclerView) findViewById(R.id.rv);
    linearLayoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(linearLayoutManager);

    arrayList = new ArrayList<>();

    mainAdapter = new MainAdapter(arrayList);
    recyclerView.setAdapter(mainAdapter);


    //친구 추가 버튼
    Button btn_add = (Button) findViewById(R.id.btn_add);
    btn_add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MainData mainData = new MainData(R.mipmap.ic_launcher, "아타나시오", "리사이클러뷰!@@#");
        arrayList.add(mainData);
        mainAdapter.notifyDataSetChanged();
      }
    });


  }
}
