package com.teamnova.inma06.Profile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.teamnova.nova.R;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ProfileModifyActivity extends AppCompatActivity {

  private static final String TAG = "Bongho";

  private Boolean isPermission = true;

  private static ImageView profileIV;

  private static final int PICK_FROM_ALBUM = 1;
  private static final int PICK_FROM_CAMERA = 2;

  private File tempFile;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile_modify);

    tedPermission();

    profileIV = findViewById(R.id.profileIV);


    /* 처음 보이는 프로필 사진은 마지막으로 수정한 사진이다.
    TODO:ProfilePageActivity 에서 인텐트로 넘겨주는 값 이용하는것이 좋을듯*/
    Glide.with(this)
        .load("https://bongbong.ga/uploads/f3367172-44bb-11e9-b49a-0a2f019140a8.jpg")
        .into(profileIV);

    findViewById(R.id.btnApply).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 적용 버튼을 눌렀을 경우 이미지를 서버에 등록합니다.
        // 유저의 아이디를 DB에서 찾아서 해당 유저의 프로필을 수정합니다.
        // 쿼리 -> UPDATE USER SET profileImage = '$resultFileDir' WHERE userID = '$userID'; (profileImage 칼럼 수정)


        final ProgressDialog dialog= ProgressDialog.show(ProfileModifyActivity.this,
            "프로필 사진 등록","사진을 등록하고 있습니다...",true);


        //TODO: 로그인시 받아온 유저의 아이디값을(맴버변수) 참조하여 사용한다.
        final String userID = "inma06@naver.com"; //TODO: 수정해야함.

        final Bitmap image = ((BitmapDrawable) profileIV.getDrawable()).getBitmap();

        com.android.volley.Response.Listener<String> responseListener = new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try{
              JSONObject jsonResponse = new JSONObject(response);
              boolean success = jsonResponse.getBoolean("success");
              if(success == false){
                // 실패
                Log.e("response -> 리스폰 결과값 출력 ", response.toString());
                System.out.println("실패했습니다.");
                Toast.makeText(ProfileModifyActivity.this, "시스템 오류입니다. 관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
              } else {
                // 성공
                Log.e("response -> 리스폰 결과값 출력 ", response.toString());
                Toast.makeText(ProfileModifyActivity.this, "프로필 사진이 적용되었습니다.", Toast.LENGTH_SHORT).show();
                System.out.println("성공했습니다.");
              }
            } catch (Exception e){
              e.printStackTrace();
              Log.e("response -> 리스폰 결과값 출력 ", response.toString());
            }
            dialog.dismiss();
            Log.e("response -> 리스폰 결과값 출력 ", response.toString());
          }
        };
        ImageRequest ImageRequest = new ImageRequest(userID, image, responseListener);
        RequestQueue queue = Volley.newRequestQueue(ProfileModifyActivity.this);
        queue.add(ImageRequest);
      }
    });




    findViewById(R.id.btnGallery).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
        if(isPermission) goToAlbum();
        else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
      }
    });

    findViewById(R.id.btnCamera).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
        if(isPermission)  takePhoto();
        else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
      }
    });

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != Activity.RESULT_OK) {
      Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

      if(tempFile != null) {
        if (tempFile.exists()) {
          if (tempFile.delete()) {
            Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
            tempFile = null;
          }
        }
      }

      return;
    }

    if (requestCode == PICK_FROM_ALBUM) {

      Uri photoUri = data.getData();
      Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

      Cursor cursor = null;

      try {

        /*
         *  Uri 스키마를
         *  content:/// 에서 file:/// 로  변경한다.
         */
        String[] proj = { MediaStore.Images.Media.DATA };

        assert photoUri != null;
        cursor = getContentResolver().query(photoUri, proj, null, null, null);

        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        tempFile = new File(cursor.getString(column_index));

        Log.d(TAG, "tempFile Uri : " + Uri.fromFile(tempFile));

      } finally {
        if (cursor != null) {
          cursor.close();
        }
      }

      setImage();

    } else if (requestCode == PICK_FROM_CAMERA) {

      setImage();

    }
  }

  /**
   *  앨범에서 이미지 가져오기
   */
  private void goToAlbum() {

    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
    startActivityForResult(intent, PICK_FROM_ALBUM);
  }


  /**
   *  카메라에서 이미지 가져오기
   */
  private void takePhoto() {

    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    try {
      tempFile = createImageFile();
    } catch (IOException e) {
      Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
      finish();
      e.printStackTrace();
    }
    if (tempFile != null) {

      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

        Uri photoUri = FileProvider.getUriForFile(this,
            "com.teamnova.inma06.provider", tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);

      } else {

        Uri photoUri = Uri.fromFile(tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);

      }
    }
  }

  /**
   *  폴더 및 파일 만들기
   */
  private File createImageFile() throws IOException {

    // 이미지 파일 이름 ( bongho_{시간}_ )
    String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
    String imageFileName = "bongho" + timeStamp + "_";

    // 이미지가 저장될 폴더 이름 ( bongho )
    File storageDir = new File(Environment.getExternalStorageDirectory() + "/bongho/");
    if (!storageDir.exists()) storageDir.mkdirs();

    // 파일 생성
    File image = File.createTempFile(imageFileName, ".jpg", storageDir);
    Log.d(TAG, "createImageFile : " + image.getAbsolutePath());

    return image;
  }

  /**
   *  tempFile 을 bitmap 으로 변환 후 profileIV(ImageView) 에 설정한다.
   */
  private void setImage() {

    BitmapFactory.Options options = new BitmapFactory.Options();
    Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
    Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());

    Glide.with(this)
        .load(originalBm)
        .into(profileIV);

    /**
     *  tempFile 사용 후 null 처리를 해줘야 합니다.
     *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
     *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
     */
    tempFile = null;

  }

  /**
   *  권한 설정
   */
  private void tedPermission() {

    PermissionListener permissionListener = new PermissionListener() {
      @Override
      public void onPermissionGranted() {
        // 권한 요청 성공
        isPermission = true;

      }

      @Override
      public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        // 권한 요청 실패
        isPermission = false;

      }
    };

    TedPermission.with(this)
        .setPermissionListener(permissionListener)
        .setRationaleMessage(getResources().getString(R.string.permission_2))
        .setDeniedMessage(getResources().getString(R.string.permission_1))
        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        .check();

  }
}
