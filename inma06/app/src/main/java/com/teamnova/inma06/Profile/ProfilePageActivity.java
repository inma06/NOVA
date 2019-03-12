package com.teamnova.inma06.Profile;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.Transformation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.squareup.picasso.Picasso;
import com.teamnova.inma06.Utils.BlurTransformation;
import com.teamnova.nova.R;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ProfilePageActivity extends AppCompatActivity {


  private ImageView profileIV;
  private TextView nickNameTV;
  private TextView messageTV;
  private ImageView profileBackgroundIV;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile_page);

    profileBackgroundIV = (ImageView) findViewById(R.id.profileBackgroundIV);

    profileIV = (ImageView) findViewById(R.id.userProfileIV);
    nickNameTV = (TextView) findViewById(R.id.userNickTV);
    messageTV = (TextView) findViewById(R.id.userMsgTV);

    //TODO: 서버에서 최신 프로필 받아와서 뿌려주어야 함
    Glide.with(this)
        .load("https://bongbong.ga/uploads/f3367172-44bb-11e9-b49a-0a2f019140a8.jpg")
        .into(profileIV);

    Picasso.get()
        .load("https://bongbong.ga/uploads/f3367172-44bb-11e9-b49a-0a2f019140a8.jpg")
        .resize(357,333)
        .transform(new BlurTransformation(this, 10))
        .into(profileBackgroundIV);

    nickNameTV.setText("4기 박봉호");
    messageTV.setText("오늘 할일 끄읏~! 뿌듯뿌듯!");


    profileIV.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ProfilePageActivity.this, ProfileModifyActivity.class);
        startActivity(intent);
      }
    });

  }
}
