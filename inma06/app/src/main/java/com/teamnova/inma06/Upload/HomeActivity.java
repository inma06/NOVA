package com.teamnova.inma06.Upload;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.teamnova.nova.R;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {


  private static ImageView imageView;
  private static Button button;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);


    imageView = findViewById(R.id.iv1);
    button = findViewById(R.id.btn1);

    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Log.d("TEST", "로그인버튼 클릭 OK");
        final String imageName = "testImage";
        final Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        com.android.volley.Response.Listener<String> responseListener = new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try{
              JSONObject jsonResponse = new JSONObject(response);
              boolean success = jsonResponse.getBoolean("success");

              if(success == false){
                // 실패
                Toast.makeText(HomeActivity.this, response, Toast.LENGTH_SHORT).show();
                System.out.println("실패했습니다.");
              } else {
                // 성공
                Toast.makeText(HomeActivity.this, response, Toast.LENGTH_SHORT).show();
                System.out.println("성공했습니다.");
              }
            } catch (Exception e){
              Toast.makeText(HomeActivity.this, response, Toast.LENGTH_SHORT).show();
              e.printStackTrace();
            }
            Log.e("response -> 리스폰 결과값 출력 ", response.toString());
          }
        };
        ImageRequest ImageRequest = new ImageRequest(imageName, image, responseListener);
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        queue.add(ImageRequest);
      }
    });



  }

}
