package chat.friendList;

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

public class Adapter_favorites extends RecyclerView.Adapter<Adapter_favorites.CustomViewHolder> {

  private ArrayList<FavoritesData> arrayList;

  public Adapter_favorites(ArrayList<FavoritesData> arrayList) {
    this.arrayList = arrayList;
  }

  @NonNull
  @Override
  public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_list, parent, false);
    CustomViewHolder holder = new CustomViewHolder(view);

    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull final Adapter_favorites.CustomViewHolder holder, int position) {

    holder.iv_profile.setImageResource(arrayList.get(position).getIv_profile());
    holder.tv_name.setText(arrayList.get(position).getTv_name());
    holder.tv_content.setText(arrayList.get(position).getTv_content());

    holder.itemView.setTag(position);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String curName = holder.tv_name.getText().toString(); // curName -> currentName ( 클릭한(현재) 아이템의 tv_name 값 )
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

  @Override
  public int getItemCount() {
    return (null != arrayList ? arrayList.size() : 0);
  }


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
    protected TextView tv_name;
    protected TextView tv_content;


    public CustomViewHolder(@NonNull View itemView) {
      super(itemView);
      this.iv_profile = (ImageView) itemView.findViewById(R.id.iv_profile_favorites);
      this.tv_name = (TextView) itemView.findViewById(R.id.tv_name_favorites);
      this.tv_content = (TextView) itemView.findViewById(R.id.tv_content_favorites);
    }
  }
}
