package profile;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class StatusMsgRequest extends StringRequest {


  final static private String URL = "https://bongbong.ga/change_status_msg.php";
  private Map<String, String>  parameters;

  public StatusMsgRequest(String userID, String statusMsg, Response.Listener<String> listener) {
    super(Method.POST, URL, listener, null);
    parameters = new HashMap<>();
    parameters.put("userID", userID);
    parameters.put("statusMsg", statusMsg);
  }

  @Override
  public Map<String, String> getParams() {
    return parameters;
  }

}
