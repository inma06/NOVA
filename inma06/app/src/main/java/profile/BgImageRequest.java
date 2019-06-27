package profile;

import android.graphics.Bitmap;
import android.util.Base64;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class BgImageRequest extends StringRequest {


  final static private String URL = "https://bongbong.ga/bg_image_upload.php";
  private Map<String, String>  parameters;

  public BgImageRequest(String userID, Bitmap image, Response.Listener<String> listener) {
    super(Method.POST, URL, listener, null);
    parameters = new HashMap<>();
    parameters.put("userID", userID);
    parameters.put("image", imageToString(image));
  }

  @Override
  public Map<String, String> getParams() {
    return parameters;
  }


  //TODO:> 프로필 사진 퀄리티 30로 하향 조정함. 봐줄만함.
  private String imageToString(Bitmap bitmap) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
    byte[] imgBytes = byteArrayOutputStream.toByteArray();
    return Base64.encodeToString(imgBytes, Base64.DEFAULT);
  }

}
