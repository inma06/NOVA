package com.teamnova.inma06.Login;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;



/*
* Login.php 파일에 userID, userPW 를 매개변수로 보낸뒤
* 결과값을 가져오는 역활을 수행한다.
*
*  */
public class LoginRequest extends StringRequest {

  //TODO: 서버연동해야함
  final static private String URL = "http://49.247.130.125/Login.php";
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