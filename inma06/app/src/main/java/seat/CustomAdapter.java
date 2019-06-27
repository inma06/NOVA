package seat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamnova.nova.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

  private ArrayList<Dictionary> mList;

  public class CustomViewHolder extends RecyclerView.ViewHolder {
    protected TextView seatNumberTv;
    protected TextView seatStatusTv;
    protected ImageView seatStatusIv;


    public CustomViewHolder(View view) {
      super(view);
      this.seatNumberTv = (TextView) view.findViewById(R.id.seatNumberTv);
      this.seatStatusTv = (TextView) view.findViewById(R.id.seatStatusTv);
      this.seatStatusIv = (ImageView) view.findViewById(R.id.seatStatusIv);
    }
  }


  public CustomAdapter(ArrayList<Dictionary> list) {
    this.mList = list;
  }




  // RecyclerView에 새로운 데이터를 보여주기 위해 필요한 ViewHolder를 생성해야 할 때 호출됩니다.
  @Override
  public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    // View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list1, null);
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.seat_main_item_list, viewGroup, false);

    CustomViewHolder viewHolder = new CustomViewHolder(view);

    return viewHolder;
  }



  // Adapter의 특정 위치(position)에 있는 데이터를 보여줘야 할때 호출됩니다.
  @Override
  public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

    viewholder.seatNumberTv.setText(mList.get(position).getSeatNumberTv());
    if(mList.get(position).getSeatStatusIv() == 1) {
      // 1 -> 사용 가능 (빈자리)
      viewholder.seatStatusIv.setImageResource(R.drawable.empty);
//      viewholder.seatStatusTv.setText("사용 가능");
    } else if(mList.get(position).getSeatStatusIv() == 2) {
      // 2 -> 사용 중
      viewholder.seatStatusIv.setImageResource(R.drawable.use);
//      viewholder.seatStatusTv.setText("사용 중");
    } else if(mList.get(position).getSeatStatusIv() == 3) {
      // 3 -> 수리중 (기타 사용불가능)
      viewholder.seatStatusIv.setImageResource(R.drawable.repair);
//      viewholder.seatStatusTv.setText("수리 중");
    } else if(mList.get(position).getSeatStatusIv() == 4) {
      viewholder.seatStatusIv.setImageResource(R.drawable.rest);
//      viewholder.seatStatusTv.setText("휴식중");
    }
  }

  @Override
  public int getItemCount() {
    return (null != mList ? mList.size() : 0);
  }

}