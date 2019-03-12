package com.teamnova.inma06.Upload;

import android.graphics.Bitmap;
import android.util.Base64;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageRequest extends StringRequest {


  final static private String URL = "https://bongbong.ga/image_upload.php";
  private Map<String, String>  parameters;

  public ImageRequest(String imageName, Bitmap image, Response.Listener<String> listener) {
    super(Method.POST, URL, listener, null);
    parameters = new HashMap<>();
    parameters.put("name", imageName);
    parameters.put("image", imageToString(image));
  }

  @Override
  public Map<String, String> getParams() {
    return parameters;
  }

  private String imageToString(Bitmap bitmap) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
    byte[] imgBytes = byteArrayOutputStream.toByteArray();
    return android.util.Base64.encodeToString(imgBytes, Base64.DEFAULT);
  }

}
