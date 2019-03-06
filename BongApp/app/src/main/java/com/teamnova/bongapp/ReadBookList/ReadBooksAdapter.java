package com.teamnova.bongapp.ReadBookList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.teamnova.bongapp.FetchBooks.bookListView;
import com.teamnova.bongapp.R;
import com.teamnova.bongapp.Timer.BooksVideo;
import com.teamnova.bongapp.Timer.ReadBook;
import com.teamnova.bongapp.Timer.ReadingTimer;

import java.util.List;

public class ReadBooksAdapter extends RecyclerView.Adapter<ReadBooksAdapter.ReadBooksHolder> {
  private Context mContext;
  private List<ReadBookItem> readBookItemList;

  //last item show on screen
  private int lastPosition = -1;

  public class ReadBooksHolder extends RecyclerView.ViewHolder {
    public TextView title, author,
        publisher, lastTime, totalTime;

    public ImageView thumbnail;

    public Button readAgain, readDelete;

    public ReadBooksHolder(View view) {
      super(view);
      thumbnail = (ImageView) view.findViewById(R.id.save_thumbnail);
      title = (TextView) view.findViewById(R.id.save_title);
      author = (TextView) view.findViewById(R.id.save_author);
      publisher = (TextView) view.findViewById(R.id.save_publisher);
      lastTime = (TextView) view.findViewById(R.id.save_lastTime);
      totalTime = (TextView) view.findViewById(R.id.save_totalTime);

      readAgain = (Button) view.findViewById(R.id.reading_again);
      readDelete = (Button) view.findViewById(R.id.reading_delete);
    }
  }

  public ReadBooksAdapter(Context mContext, List<ReadBookItem> readBookItemList) {
    this.mContext = mContext;
    this.readBookItemList = readBookItemList;
  }

  @Override
  public ReadBooksHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.read_book_list_card, parent, false);


    return new ReadBooksHolder(itemView);
  }

  @Override
  public void onBindViewHolder(final ReadBooksHolder holder, final int position) {
    final ReadBookItem readBookItem = readBookItemList.get(position);
    /*
    * 썸네일 saveThumbNail
    * 제목 getSaveTitle
    * 저자 getSaveAuthor
    * 출판사 getSavePublisher
    * 마지막 독서 시간 getSaveLastTime
    * 총 독서 시간 getSaveTotalTime
    * 마지막 읽은 페이지 getSaveLastPage
    * 총 페이지 수 getSaveTotalPage
    * */
    Glide.with(mContext)
        .load(readBookItem.getSaveThumbNail())
        .into(holder.thumbnail);
    holder.title.setText(readBookItemList.get(position).getSaveTitle());
    holder.author.setText(readBookItemList.get(position).getSaveAuthor());
    holder.publisher.setText(readBookItemList.get(position).getSavePublisher());
    holder.lastTime.setText("마지막 읽은 날 : " + readBookItem.getSaveLastTime());
    holder.totalTime.setText("최근 독서 량(시간) : "+ readBookItem.getSaveTotalTime());
//    holder.lastPage.setText(readBookItem.getSaveLastPage());
//    holder.totalPage.setText(readBookItem.getSaveTotalPage());

    //버튼처리
    holder.readAgain.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(mContext, "이어서 읽기", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(mContext, ReadingTimer.class);

        String totalTime;
        totalTime = ReadBookArchive.readBooksList.get(position).getSaveTotalTime();
        intent.putExtra("isAgain", true);
        intent.putExtra("pos", position);
        mContext.startActivity(intent);
      }
    });

    holder.readDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(mContext, "목록이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
        ReadBookArchive.readBooksList.remove(position);
        notifyDataSetChanged();
      }
    });

    //아이템 생성 될때 애니메이션 처리
    setAnimation(holder.itemView, position);

  }

  @Override
  public int getItemCount() {
    return readBookItemList.size();
  }

  //새로 보여지는 아이템 뷰라면 애니메이션을 해줍니다.
  private void setAnimation(View viewToAnimation, int position) {
    if(position > lastPosition) {
      Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
      viewToAnimation.startAnimation(animation);
      lastPosition = position;
    }
  }

}
