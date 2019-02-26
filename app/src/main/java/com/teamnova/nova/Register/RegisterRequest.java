package com.teamnova.nova.Register;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

  final static private String URL = "https://bongbong.ga/Register.php";
  private Map<String, String>  parameters;

  public RegisterRequest(String userID, String userPW, String userName, String userEmail, Response.Listener<String> listener) {
    super(Method.POST, URL, listener, null);
    parameters = new HashMap<>();
    parameters.put("userID", userID);
    parameters.put("userPW", userPW);
    parameters.put("userName", userName);
    parameters.put("userEmail", userEmail);
  }

  @Override
  public Map<String, String> getParams() {
    return parameters;
  }
}
