package com.teamnova.nova.Login;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;



/*
* Login.php 파일에 userID, userPW 를 매개변수로 보낸뒤
* 결과값을 가져오는 역활을 수행한다.
*
*  */
public class LoginRequest extends StringRequest {

  final static private String URL = "https://bongbong.ga/Login.php";
  private Map<String, String>  parameters;

  public LoginRequest(String userID, String userPW, Response.Listener<String> listener) {
    super(Method.POST, URL, listener, null);
    parameters = new HashMap<>();
    parameters.put("userID", userID);
    parameters.put("userPW", userPW);
  }

  @Override
  public Map<String, String> getParams() {
    return parameters;
  }
}
