package profile;

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

  public ImageRequest(String userID, Bitmap image, Response.Listener<String> listener) {
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
    return android.util.Base64.encodeToString(imgBytes, Base64.DEFAULT);
  }

}
