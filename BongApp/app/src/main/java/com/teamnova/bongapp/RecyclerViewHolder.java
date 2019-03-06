package com.teamnova.bongapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
  public ImageView mThumbNail;
  public TextView mTextView;
  public TextView mDateText;
  public Button mDeleteBtn;
  public Button mViewBtn;
  public Button mModifyBtn;


  public RecyclerViewHolder(View itemView) {
    super(itemView);
    //activity_item.xml에 연결해 준다.
    mDateText = (TextView) itemView.findViewById(R.id.dateText);
    mThumbNail = (ImageView) itemView.findViewById(R.id.item_ThumbNail);
    mTextView = (TextView) itemView.findViewById(R.id.item_textView);
    mDeleteBtn = (Button) itemView.findViewById(R.id.deleteBtn_item);
//    mViewBtn = (Button) itemView.findViewById(R.id.viewBtn_item);
    mModifyBtn = (Button) itemView.findViewById(R.id.modifyBtn_item);


  }
}
