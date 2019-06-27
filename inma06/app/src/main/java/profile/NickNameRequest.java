package profile;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NickNameRequest extends StringRequest {


  final static private String URL = "https://bongbong.ga/change_nick_name.php";
  private Map<String, String>  parameters;

  public NickNameRequest(String userID, String nickName, Response.Listener<String> listener) {
    super(Method.POST, URL, listener, null);
    parameters = new HashMap<>();
    parameters.put("userID", userID);
    parameters.put("nickName", nickName);
  }

  @Override
  public Map<String, String> getParams() {
    return parameters;
  }


}
