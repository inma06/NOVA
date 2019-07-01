package chat.friendList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.teamnova.nova.R;

import java.util.ArrayList;

public class FriendListMainActivity extends AppCompatActivity {

  /*
  * TODO: item long click to Item Delete -> Dialog Popup
  *
  * dialog view menu
  * ----------------
  *
  * 채팅하기 ( 1:1채팅으로 채팅방 개설됨 이후 다른 친구 초대 가능 )
  * 페이스톡 ( 영상통화 )
  * -------
  * 즐겨찾기 등록
  * 프로필 보기
  * 숨김 ( 친구 목록에서 제거 )
  * 차단 ( 친구 목록에서 제거 + 메시지 수신 거부 )
  *
  *
  * // 본 어플리케이션에서 '친구'는, 회원 아이디를 직접 입력 하여 추가한 것을 말한다.
  * // 안드로이드에 내장된 전화번호부 어플리케이션을 참조하여 자동 친구등록 되는 것은 추가 하지 않았다.
  * */



  private ArrayList<MainData> arrayList_friends;
  private Adapter_friends adapter_friends_friends;
  private RecyclerView rvFriends;
  private RecyclerView rvFavorites;

  private LinearLayoutManager linearLayoutManager;




  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_friend_list_main);


    // 친구목록 리사이클러뷰
    rvFriends = (RecyclerView) findViewById(R.id.rvFriends);
    linearLayoutManager = new LinearLayoutManager(this);
    rvFriends.setLayoutManager(linearLayoutManager);
    arrayList_friends = new ArrayList<>();
    adapter_friends_friends = new Adapter_friends(arrayList_friends);
    rvFriends.setAdapter(adapter_friends_friends);

    //친구 검색 ( SearchView )
    SearchView sv_friendSearch = (SearchView) findViewById(R.id.friend_sv);
    sv_friendSearch.setQueryHint("이름 검색");


    //친구 추가 버튼
    Button btn_add = (Button) findViewById(R.id.btn_add);
    btn_add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MainData mainData = new MainData(R.mipmap.ic_launcher, "아타나시오", "리사이클러뷰!@@#");
        arrayList_friends.add(mainData);
        adapter_friends_friends.notifyDataSetChanged();
      }
    });


  }
}
