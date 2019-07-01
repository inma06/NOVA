package chat.roomList;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamnova.nova.R;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {

  private ArrayList<MainData> arrayList;

  public MainAdapter(ArrayList<MainData> arrayList) {
    this.arrayList = arrayList;
  }

  @NonNull
  @Override
  public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_list, parent, false);
    CustomViewHolder holder = new CustomViewHolder(view);

    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull final MainAdapter.CustomViewHolder holder, int position) {

    holder.iv_profile.setImageResource(arrayList.get(position).getIv_profile()); // 방이미지
    holder.tv_roomName.setText(arrayList.get(position).getTv_roomName()); // 방이름
    holder.tv_date.setText(arrayList.get(position).getTv_date()); // 시간 ( 오늘-> 시간, 어제, 그외 ( 날짜 : 6월 13일 )
    holder.tv_talkerCount.setText(arrayList.get(position).getTv_talkerCount()); // 방의 총인원수
    holder.tv_content.setText(arrayList.get(position).getTv_content()); // 메시지 내용
    holder.itemView.setTag(position);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String curName = holder.tv_roomName.getText().toString(); // curName -> currentName ( 클릭한(현재) 아이템의 tv_name 값 )
        Toast.makeText(v.getContext(), curName, Toast.LENGTH_SHORT).show();
      }
    });

    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {

        remove(holder.getAdapterPosition());
        return true;
      }
    });

  }


  // 아이템 총 개수
  @Override
  public int getItemCount() {
    return (null != arrayList ? arrayList.size() : 0);
  }



  // 아이템 삭제 메서드
  public void remove(int position) {
    try {
      arrayList.remove(position);
      notifyItemRemoved(position);

    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
    }

  }


  public class CustomViewHolder extends RecyclerView.ViewHolder {

    protected ImageView iv_profile;
    protected TextView tv_roomName;
    protected TextView tv_content;
    protected TextView tv_date;
    protected TextView tv_talkerCount;



    public CustomViewHolder(@NonNull View itemView) {
      super(itemView);

      this.iv_profile = (ImageView) itemView.findViewById(R.id.iv_profile);
      this.tv_roomName = (TextView) itemView.findViewById(R.id.tv_roomName);
      this.tv_content = (TextView) itemView.findViewById(R.id.tv_content);
      this.tv_date = (TextView) itemView.findViewById(R.id.tv_date);
      this.tv_talkerCount = (TextView) itemView.findViewById(R.id.tv_talkerCount);
    }
  }
}
