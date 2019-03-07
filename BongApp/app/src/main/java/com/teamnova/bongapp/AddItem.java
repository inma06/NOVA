package com.teamnova.bongapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.senab.photoview.PhotoViewAttacher;

public class AddItem extends AppCompatActivity {

  protected static Bundle extras;
  private static String TAG = " 아이템추가하는 액티비티";
  protected static String strEditText;
  private final int CAMERA_CODE = 1111;
  private final int GALLERY_CODE = 1112;

  //&&&
  private static final int FROM_CAMERA = 0;
  private static final int FROM_ALBUM = 1;
  private static String mCurrentPhotoPath;


  protected static ImageView addImageView;
  public static String imagePath;
  private static Uri photoUri;
  private String currentPhotoPath;//실제 사진 파일 경로
  String mImageCaptureName;//이미지 이름
  private static EditText editText;
  private static Bitmap bitmap;
  public static String mTimeStamp;
  private static TextView lenText;
  protected static Bitmap imageBitmap;
  private static final int REQUEST_IMAGE_CAPTURE = 1;

  private Uri imgUri, photoURI, albumURI;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_item);
    editText = (EditText) findViewById(R.id.addText_addItem);

    lenText = (TextView) findViewById(R.id.textLen_addItem);

    editText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        lenText.setText(editText.getText().length()+"/200");
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    //작성 버튼
    findViewById(R.id.addBtn_addItem).setOnClickListener(new Button.OnClickListener() {
      @Override
      public void onClick(View v) {
        mTimeStamp = new SimpleDateFormat(("yyyy년MM월dd일HH시mm분ss초")).format(new Date());
//        Archive.items.add(0, new Item(imagePath, editText.getText().toString(), mTimeStamp +" 작성됨", imagePath));
        Archive.mAdapter.mItems.add(0, new Item(imagePath, editText.getText().toString(), mTimeStamp +" 작성됨", imagePath));
        Archive.mAdapter.notifyItemInserted(0);
        bitmap = null;
        Intent intent = new Intent(AddItem.this, Archive.class);
        startActivity(intent);
        AddItem.this.finish();
      }
    });

    //촬영 버튼
    findViewById(R.id.cameraBtn_addItem).setOnClickListener(new Button.OnClickListener() {
      @Override
      public void onClick(View v) {
        //카메라눌렀을때
        Log.v("알림", "다이얼로그 > 사진촬영 선택");
//        takePhoto();
//        selectPhoto();
//
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
          startActivityForResult(takePictureIntent, CAMERA_CODE);
        }

//        Toast.makeText(AddItemBackupsrc.this, "구현준비중", Toast.LENGTH_SHORT).show();
      }
    });


    //앨범 버튼
    findViewById(R.id.galleryBtn_addItem).setOnClickListener(new Button.OnClickListener() {
      @Override
      public void onClick(View v) {

//        selectAlbum();

//        //imageFindBtn 눌렀을 때 앨범에서 이미지 찾아서 받아오기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*.png");
        startActivityForResult(intent, GALLERY_CODE);
      }
    });
  }

  //앨범 선택 클릭
  public void selectAlbum(){
    //앨범에서 이미지 가져옴
    //앨범 열기
    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
    intent.setType("image/*");
    startActivityForResult(intent, FROM_ALBUM);
  }

  //사진 찍기 클릭
  public void takePhoto(){
    // 촬영 후 이미지 가져옴
    String state = Environment.getExternalStorageState();
    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if(Environment.MEDIA_MOUNTED.equals(state)){
      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      if(intent.resolveActivity(getPackageManager())!=null){
        File photoFile = null;
        try{
          photoFile = createImageFile();
        }catch (IOException e){
          e.printStackTrace();
        }
        if(photoFile!=null){
          Uri providerURI = FileProvider.getUriForFile(this,getPackageName(),photoFile);
          imgUri = providerURI;
          intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, providerURI);
          startActivityForResult(intent, FROM_CAMERA);
        }
      }
    }else{
      Log.v("알림", "저장공간에 접근 불가능");
      return;
    }
  }


  //AddItemBackupsrc 액티비티(현재 액티비티)에서 뒤로가기 누를 경우 이전 액티비티( Archive 액티비티 ) 로 이동
  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Intent intent = new Intent(AddItem.this, Archive.class);
    startActivity(intent);
    AddItem.this.finish();
  }


  public void galleryAddPic(){
    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    File f = new File(mCurrentPhotoPath);
    Uri contentUri = Uri.fromFile(f);
    mediaScanIntent.setData(contentUri);
    sendBroadcast(mediaScanIntent);
    Toast.makeText(this,"사진이 저장되었습니다",Toast.LENGTH_SHORT).show();
  }

  //앨범에서 사진 선택했을때
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    addImageView = (ImageView) findViewById(R.id.addImage_addItem);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case GALLERY_CODE:
          sendPicture(data.getData()); //갤러리에서 가져오기
          break;
        case CAMERA_CODE:
//          sendPicture(data.getData());
//          selectPhoto();
//          sendPicture(data.getData());

//          imagePath =data.getData();
//          Uri currImageURI = data.getData();
          extras = data.getExtras();
          bitmap = (Bitmap) extras.get("data");
          Glide.with(this)
              .load(bitmap)
              .into(addImageView);

          break;

        default:
          break;
      }
    }
  }

/*  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode != RESULT_OK){
      return;
    }
    switch (requestCode){
      case FROM_ALBUM : {
        //앨범에서 가져오기
        if(data.getData()!=null){
          try{
            File albumFile = null;
            albumFile = createImageFile();
            photoURI = data.getData();
            albumURI = Uri.fromFile(albumFile);
            galleryAddPic();
            Log.e(TAG, "onActivityResult: 찍히냐 로그");
            addImageView.setImageURI(photoURI);
//            Glide.with(this)
//                .load(albumURI)
//                .into(addImageView);

//            cropImage();
          }catch (Exception e){
            e.printStackTrace();
            Log.v("알림","앨범에서 가져오기 에러");
          }
        }
        break;
      }
      case FROM_CAMERA : {
        //카메라 촬영
        try{
          Log.v("알림", "FROM_CAMERA 처리");
          galleryAddPic();
//          addImageView.setImageURI(imgUri);
          Glide.with(this)
              .load(imgUri)
              .into(addImageView);


        }catch (Exception e){
          e.printStackTrace();
        }
        break;
      }
    }
  }*/

  //카메라에서 사진가져오기
  public void getPictureForPhoto() {
    Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
    ExifInterface exif = null;
    try {
      exif = new ExifInterface(currentPhotoPath);
    } catch (IOException e) {
      e.printStackTrace();
    }
    int exifOrientation;
    int exifDegree;

    if (exif != null) {
      exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
      exifDegree = exifOrientationToDegrees(exifOrientation);
    } else {
      exifDegree = 0;
    }
//    addImageView.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
    Glide.with(this)
        .load(imagePath)
        .into(addImageView);
  }



  //
  public void selectPhoto() {
    Log.e(TAG, "selectPhoto: 안되냐1");
    String state = Environment.getExternalStorageState();
    Log.e(TAG, "selectPhoto: 안되냐2");
    if (Environment.MEDIA_MOUNTED.equals(state)) {
      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      if (intent.resolveActivity(getPackageManager()) != null) {
        Log.e(TAG, "selectPhoto: 안되냐3");
        File photoFile = null;
        try {
          Log.e(TAG, "selectPhoto: 안되냐4");
          photoFile = createImageFile();
        } catch (IOException ex) {
          Toast.makeText(this, "이미지 불러오기 오류", Toast.LENGTH_SHORT).show();

        }
        if (photoFile != null) {
          Log.e(TAG, "selectPhoto: 안되냐5");
          photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
          intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
          startActivityForResult(intent, CAMERA_CODE);
        }
      }

    }
  }

/*  //파일생성
  public File createImageFile() throws IOException {
    File dir = new File(Environment.getExternalStorageDirectory() + "/path/");
    if (!dir.exists()) {
      dir.mkdirs();
    }
    mImageCaptureName = mTimeStamp;
    File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/path/"
        + mImageCaptureName);
    currentPhotoPath = storageDir.getAbsolutePath();
    return storageDir;
  }*/

  public File createImageFile() throws IOException{
    String imgFileName = System.currentTimeMillis() + ".jpg";
    File imageFile= null;
    File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "ireh");
    if(!storageDir.exists()){
      Log.v("알림","storageDir 존재 x " + storageDir.toString());
      storageDir.mkdirs();
    }
    Log.v("알림","storageDir 존재함 " + storageDir.toString());
    imageFile = new File(storageDir,imgFileName);
    mCurrentPhotoPath = imageFile.getAbsolutePath();
    return imageFile;
  }


  //선택한 사진처리
  public void sendPicture(Uri imgUri) {

    imagePath = getRealPathFromURI(imgUri); // 이미지 path 경로
    ExifInterface exif = null;
    try {
      exif = new ExifInterface(imagePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
    int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
    int exifDegree = exifOrientationToDegrees(exifOrientation);

    // 이미지 선택한것 이미지뷰에 보여지기
    PhotoViewAttacher viewAttacher = new PhotoViewAttacher(addImageView);
    bitmap = rotate(BitmapFactory.decodeFile(imagePath), exifDegree);
    Glide.with(this)
        .load(bitmap)
        .into(addImageView);
    viewAttacher.update();
  }

  //사진의 회전값 가져오기
  private int exifOrientationToDegrees(int exifOrientation) {
    if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
      return 90;
    } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
      return 180;
    } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
      return 270;
    }
    return 0;
  }

  //사진 정방향대로 회전하기
  private Bitmap rotate(Bitmap src, float degree) {

    // Matrix 객체 생성
    Matrix matrix = new Matrix();
    // 회전 각도 셋팅
    matrix.postRotate(degree);
    // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
    return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
        src.getHeight(), matrix, true);
  }

  //절대 경로 구하기
  private String getRealPathFromURI(Uri contentUri) {
    int column_index = 0;
    String[] proj = {MediaStore.Images.Media.DATA};
    Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
    if(cursor.moveToFirst()) {
      column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    }
    return cursor.getString(column_index);
  }

}
