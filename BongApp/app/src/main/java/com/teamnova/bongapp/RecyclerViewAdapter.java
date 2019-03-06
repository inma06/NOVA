package com.teamnova.bongapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {


  private final String TAG = "리사이클러뷰 아답터";
  public static ArrayList<Item> mItems;
  public static Context mContext;
  public static int selectPosition;
  public static ImageView selectImgView;
  public static TextView selectTextView;
  public RecyclerViewAdapter(ArrayList itemList) {
    mItems = itemList;
  }
  //last item show on screen
  private int lastPosition = -1;

  //필수 오버라이드 : View 생성, ViewHolder 호출 *activity_item.xml 내용 가져와서 담기
  @Override
  public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
    mContext = parent.getContext();
    RecyclerViewHolder holder = new RecyclerViewHolder(v);
    return holder;
  }

  //필수 오버라이드 : 재활용되는 View 가 호출, Adapter 가 해당 position 에 해당하는 데이터를 결합
  @Override
  public void onBindViewHolder(RecyclerViewHolder holder, final int position) {

    // 해당 position에 해당하는 데이터 결합

    Log.d(TAG, "섬네일" + mItems.get(position).getThumbNail());
    Log.d(TAG, "이미지경로" + mItems.get(position).getThumbNail());
    Glide.with(mContext)
        .load(mItems.get(position).getImagePath())
        .into(holder.mThumbNail);
//    holder.mThumbNail.setImageBitmap(mItems.get(position).getThumbNail());
    holder.mTextView.setText(mItems.get(position).getTextView());
    holder.mDateText.setText(mItems.get(position).getDateText());

    holder.mDeleteBtn.setOnClickListener(new Button.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(mContext, "삭제버튼클릭", Toast.LENGTH_SHORT).show();
        mItems.remove(position);
        com.teamnova.bongapp.Archive.mAdapter.notifyDataSetChanged();
      }
    });

    holder.mModifyBtn.setOnClickListener(new Button.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(mContext, "수정버튼클릭", Toast.LENGTH_SHORT).show();
        selectPosition = position;
        Intent intent = new Intent(mContext, ModifyItem.class);
        mContext.startActivity(intent);
      }
    });

    //애니메이션
    setAnimation(holder.itemView, position);
    // 이벤트처리 : 생성된 List 중 선택된 인덱스번호를 Toast 로 출력
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

//                selectImgView.setImageBitmap(mItems.get(position + 1).thumbNail);
//                selectTextView.setText(mItems.get(position + 1).textView);
        Toast.makeText(mContext, String.format("%d 선택", position + 1), Toast.LENGTH_SHORT).show();
      }
    });
  }



  //새로 보여지는 아이템 뷰라면 애니메이션을 해줍니다.
  private void setAnimation(View viewToAnimation, int position) {
    if(position > lastPosition) {
      Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
      viewToAnimation.startAnimation(animation);
      lastPosition = position;
    }
  }

  //필수 오버라이드 : 데이터 갯수 반환
  @Override
  public int getItemCount() {
    return mItems.size();
  }
}
