package com.teamnova.inma06.Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.teamnova.inma06.Utils.BlurTransformation;
import com.teamnova.nova.R;

import org.json.JSONObject;


public class ProfileMainActivity extends AppCompatActivity {

  private static String TAG = "ProfileMainActivity";
/*
  전역 맴버 변수로 선언 TODO: 필요할 때 쓸것.
  public static ImageView mProfileIV;
  public static TextView mNickNameTV;
  public static TextView mMessageTV;
  public static ImageView mProfileBackgroundIV;
*/

  private ImageView profileIV;
  private TextView nickNameTV;
  private TextView statusMsgTV;
  private ImageView profileBackgroundIV;

  public static Context mContext;

  private ImageView statMsgBtn;
  private ImageView nickBtn;
  private ImageView changeBgBtn;
  private ImageView changeProfileImageBtn;

  @Override
  protected void onResume() {
    super.onResume();
    // 화면에 보여주기 전에 준비를 끝냄
    // 임시로 저장된 화면의 상태를 불러온다

    Glide.with(this)
        .load(HomeActivity.mProfileImageDir)
        .into(profileIV);

    Picasso.get()
        .load(HomeActivity.mProfileBgImageDir)
        .fit()
        .transform(new BlurTransformation(this, 2))
        .into(profileBackgroundIV);

    statusMsgTV.setText(HomeActivity.mStatusMsg);
    nickNameTV.setText(HomeActivity.mNickName);
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile_main);

    profileBackgroundIV = (ImageView) findViewById(R.id.profileBackgroundIV);

    profileIV = (ImageView) findViewById(R.id.userProfileIV);
    nickNameTV = (TextView) findViewById(R.id.userNickTV);
    statusMsgTV = (TextView) findViewById(R.id.userMsgTV);

    statusMsgTV.setText(HomeActivity.mStatusMsg);
    nickNameTV.setText(HomeActivity.mNickName);

    statMsgBtn = findViewById(R.id.editStatMsgBtn);
    nickBtn = findViewById(R.id.editNickBtn);

    changeBgBtn = findViewById(R.id.changeBgBtn);
    changeProfileImageBtn = findViewById(R.id.changeProfileImageBtn);



    changeProfileImageBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ProfileMainActivity.this, ProfileModifyActivity.class);
        startActivity(intent);
      }
    });

    profileIV.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ProfileMainActivity.this, ProfileModifyActivity.class);
        startActivity(intent);
      }
    });

    changeBgBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ProfileMainActivity.this, ProfileBgModifyActivity.class);
        startActivity(intent);
      }
    });

    profileBackgroundIV.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ProfileMainActivity.this, ProfileBgModifyActivity.class);
        startActivity(intent);
      }
    });

    nickBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder ad = new AlertDialog.Builder(ProfileMainActivity.this);
        ad.setTitle("닉네임");       // 제목 설정
        ad.setMessage("15자 이내로 작성하세요");   // 내용 설정

        // EditText 삽입하기
        final EditText et = new EditText(ProfileMainActivity.this);
        //TODO: 예외처리해야함
        et.setMaxLines(1); // 닉네임은 줄바꿈(개행)을 하지 않는다.
        et.setText(nickNameTV.getText()); // nickName TextView 의 내용을 초기값으로 셋팅한다.

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(15);
        et.setFilters(FilterArray);

        ad.setView(et);

        // 적용 버튼 설정
        ad.setPositiveButton("적용", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

            if(et.getText().toString().isEmpty()) {
              Toast.makeText(ProfileMainActivity.this, "닉네임은 공백일 수 없습니다.", Toast.LENGTH_SHORT).show();
              et.setFocusable(true);
            } else {
              Log.v(TAG, "적용 버튼 클릭");
              // Text 값 받아서 로그 남기기
              final String nickName = et.getText().toString();
              Log.v(TAG, nickName);


              // 적용 버튼을 눌렀을 경우 상태메시지를 서버에 등록합니다.
              // 유저의 아이디를 DB에서 찾아서 해당 유저의 프로필을 수정합니다.
              // 쿼리 -> UPDATE USER SET ststusMsg = '$statusMsg' WHERE userID = '$userID'; (statusMsg 칼럼 수정)
              final ProgressDialog progressDialog = ProgressDialog.show(ProfileMainActivity.this,
                  "닉네임 등록","닉네임을 등록하고 있습니다...",true);

              //TODO: 로그인시 받아온 유저의 아이디값을(맴버변수) 참조하여 사용한다.
              final String userID = HomeActivity.mUserID; //TODO: 수정해야함 -> 수정완료.

              com.android.volley.Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                  try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    String nickName = jsonResponse.getString("nickName");

                    if(success == false){
                      // 실패
                      Log.e("response -> 리스폰 결과값 출력 ", response.toString());
                      System.out.println("실패했습니다.");
                      Toast.makeText(ProfileMainActivity.this, "시스템 오류입니다. 관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
                    } else {
                      // 성공시 닉네임을 변수에 저장합니다.
                      HomeActivity.mNickName = nickName;
                      nickNameTV.setText(nickName);

                      Log.e("response -> 리스폰 결과값 출력 ", response.toString());

                      Toast.makeText(ProfileMainActivity.this, "상태 메시지가 적용되었습니다.", Toast.LENGTH_SHORT).show();
                      System.out.println("성공했습니다.");
                    }
                  } catch (Exception e){
                    e.printStackTrace();
                    Log.e("response -> 리스폰 결과값 출력 ", response.toString());
                  }
                  progressDialog.dismiss();
                  Log.e("response -> 리스폰 결과값 출력 ", response.toString());
                }
              };
              NickNameRequest nickNameRequest = new NickNameRequest(userID, nickName, responseListener);
              RequestQueue queue = Volley.newRequestQueue(ProfileMainActivity.this);
              queue.add(nickNameRequest);

              dialog.dismiss();     //닫기
              // Event
            }
          }
        });

        // 취소 버튼 설정
        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            Log.v(TAG,"No Btn Click");
            dialog.dismiss();     //닫기
            // Event
          }
        });

        // 창 띄우기
        ad.show();
      }
    });



    statMsgBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        AlertDialog.Builder ad = new AlertDialog.Builder(ProfileMainActivity.this);
        ad.setTitle("상태메시지");       // 제목 설정
        ad.setMessage("60자 이내로 작성하세요");   // 내용 설정

        // EditText 삽입하기
        final EditText et = new EditText(ProfileMainActivity.this);

        et.setMaxLines(5);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(60);
        et.setFilters(FilterArray);
        et.setText(statusMsgTV.getText());
        ad.setView(et);


        // 적용 버튼 설정
        ad.setPositiveButton("적용", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            Log.v(TAG, "적용 버튼 클릭");

            // Text 값 받아서 로그 남기기
            final String statusMsg = et.getText().toString();
            Log.v(TAG, statusMsg);

            // 적용 버튼을 눌렀을 경우 상태메시지를 서버에 등록합니다.
            // 유저의 아이디를 DB에서 찾아서 해당 유저의 프로필을 수정합니다.
            // 쿼리 -> UPDATE USER SET ststusMsg = '$statusMsg' WHERE userID = '$userID'; (statusMsg 칼럼 수정)
            final ProgressDialog progressDialog = ProgressDialog.show(ProfileMainActivity.this,
                "상태 메시지 등록","메시지를 등록하고 있습니다...",true);

            //TODO: 로그인시 받아온 유저의 아이디값을(맴버변수) 참조하여 사용한다.
            final String userID = HomeActivity.mUserID; //TODO: 수정해야함 -> 수정완료.

            com.android.volley.Response.Listener<String> responseListener = new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                try{
                  JSONObject jsonResponse = new JSONObject(response);
                  boolean success = jsonResponse.getBoolean("success");
                  String statusMsg = jsonResponse.getString("statusMsg");

                  if(success == false){
                    // 실패
                    Log.e("response -> 리스폰 결과값 출력 ", response.toString());
                    System.out.println("실패했습니다.");
                    Toast.makeText(ProfileMainActivity.this, "시스템 오류입니다. 관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
                  } else {
                    // 성공시 이미지 경로를 변수에 저장합니다.
                    HomeActivity.mStatusMsg = statusMsg;
                    statusMsgTV.setText(statusMsg);

                    Log.e("response -> 리스폰 결과값 출력 ", response.toString());

                    Toast.makeText(ProfileMainActivity.this, "상태 메시지가 적용되었습니다.", Toast.LENGTH_SHORT).show();
                    System.out.println("성공했습니다.");
                  }
                } catch (Exception e){
                  e.printStackTrace();
                  Log.e("response -> 리스폰 결과값 출력 ", response.toString());
                }
                progressDialog.dismiss();
                Log.e("response -> 리스폰 결과값 출력 ", response.toString());
              }
            };
            StatusMsgRequest statusMsgRequest = new StatusMsgRequest(userID, statusMsg, responseListener);
            RequestQueue queue = Volley.newRequestQueue(ProfileMainActivity.this);
            queue.add(statusMsgRequest);

            dialog.dismiss();     //닫기
            // Event
          }
        });

        // 취소 버튼 설정
        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            Log.v(TAG,"No Btn Click");
            dialog.dismiss();     //닫기
            // Event
          }
        });

        // 창 띄우기
        ad.show();
      }
    });

  }
}
