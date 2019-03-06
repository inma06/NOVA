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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ModifyItem extends AppCompatActivity {

  private final int CAMERA_CODE = 1113;
  private final int GALLERY_CODE = 1114;
  private String currentPhotoPath;
  private static Uri photoUri;
  private static String imagePath;
  private static String mTimeStamp;
  private static int exifDegree;
  String mImageCaptureName;

  private final String TAG = "마더파이 아이템";
  private ImageView modifyImg;
  private static EditText modifyTextView;
  private Button modifyBtn;
  private Button findImgBtn;
  private Button exitBtn;
  private static Bitmap modifyImgBitmap;
  private static Bitmap bitmap;
  private static Item modifyItem;
  private static String timeStamp;
  private static String modifyImgPath;
  private static final int REQUEST_IMAGE_CAPTURE = 1;


  //앨범에서 사진 선택했을때

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);


    modifyImg = (ImageView) findViewById(R.id.modifyImage);

    if (resultCode == RESULT_OK) {

      switch (requestCode) {

        case GALLERY_CODE:
          sendPicture(data.getData()); //갤러리에서 가져오기
          break;
        case CAMERA_CODE:
          getPictureForPhoto();
          break;

        default:
          break;
      }
    }
  }

  //카메라에서 사진가져오기
  public void getPictureForPhoto() {
    bitmap = BitmapFactory.decodeFile(currentPhotoPath);
    ExifInterface exif = null;
    try {
      exif = new ExifInterface(currentPhotoPath);
    } catch (IOException e) {
      e.printStackTrace();
    }
    int exifOrientation;

    if (exif != null) {
      exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
      exifDegree = exifOrientationToDegrees(exifOrientation);
    } else {
      exifDegree = 0;
    }
    modifyImg.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기

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

  //파일생성

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

  }

  //선택한 사진처리
  public void sendPicture(Uri imgUri) {

    imagePath = getRealPathFromURI(imgUri); // 이미지 path 경로
    Glide.with(this)
        .load(imagePath)
        .into(modifyImg);

//    ExifInterface exif = null;
/*    try {
      exif = new ExifInterface(imagePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
    int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
    int exifDegree = exifOrientationToDegrees(exifOrientation);


    // 이미지 선택한것 이미지뷰에 보여지기



    PhotoViewAttacher viewAttacher = new PhotoViewAttacher(modifyImg);
    bitmap = rotate(BitmapFactory.decodeFile(imagePath), exifDegree);
//    Glide.with(this)
//        .load(bitmap)
//        .into(modifyImg);
    modifyImg.setImageBitmap(bitmap);

    viewAttacher.update();*/
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


  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Intent intent = new Intent(this, com.teamnova.bongapp.Archive.class);
    startActivity(intent);
    ModifyItem.this.finish();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_modify_item);
    mTimeStamp = new SimpleDateFormat(("yyyy년MM월dd일HH시mm분ss초")).format(new Date());
    modifyImg = (ImageView) findViewById(R.id.modifyImage);
    modifyTextView = (EditText) findViewById(R.id.modifyText);
    modifyTextView.setText(RecyclerViewAdapter.mItems.get(RecyclerViewAdapter.selectPosition).getTextView());
    Glide.with(this)
        .load(RecyclerViewAdapter.mItems.get(RecyclerViewAdapter.selectPosition).getThumbNail())
        .into(modifyImg);
//    modifyImg.setImageBitmap(rotate(RecyclerViewAdapter.mItems.get(RecyclerViewAdapter.selectPosition).getThumbNail(), exifDegree));

//    bitmap = RecyclerViewAdapter.mItems.get(RecyclerViewAdapter.selectPosition).getThumbNail();

//    modifyImgBitmap = ((BitmapDrawable)modifyImg.getDrawable()).getBitmap();


    modifyBtn = (Button) findViewById(R.id.modifyBtn);
    findImgBtn = (Button) findViewById(R.id.imageFindBtn);
    exitBtn = (Button) findViewById(R.id.exit);

    imagePath = com.teamnova.bongapp.Archive.mAdapter.mItems.get(RecyclerViewAdapter.selectPosition).imagePath;



    //수정 버튼 클릭
    modifyBtn.setOnClickListener(new Button.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(ModifyItem.this, "수정버튼눌렀다", Toast.LENGTH_SHORT).show();
        modifyItem = new Item(imagePath, modifyTextView.getText().toString(), mTimeStamp + "에 수정됨", imagePath);
        RecyclerViewAdapter.mItems.set(RecyclerViewAdapter.selectPosition, modifyItem);
        com.teamnova.bongapp.Archive.mAdapter.notifyItemChanged(RecyclerViewAdapter.selectPosition);
        bitmap = null;
        imagePath = null;
        Intent intent = new Intent(ModifyItem.this, com.teamnova.bongapp.Archive.class);
        startActivity(intent);
        ModifyItem.this.finish();
      }
    });


    exitBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ModifyItem.this, Archive.class);
        startActivity(intent);
        ModifyItem.this.finish();
      }
    });


    findImgBtn.setOnClickListener(new Button.OnClickListener() {
      @Override
      public void onClick(View v) {

        //imageFindBtn 눌렀을 때 앨범에서 이미지 찾아서 받아오기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*.png");
        startActivityForResult(intent, GALLERY_CODE);
      }
    });

  }
}
