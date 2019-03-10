package com.teamnova.inma06.Register;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CheckRequest extends StringRequest {
/*
* Volley 라이브러리를 이용하여 서버로 요청(Request)를 보내고
* 리턴값을 Json 형태로 받아옵니다.
*
* -By inma06
* */


  final static private String URL = "https://bongbong.ga/check.php";
  private Map<String, String>  parameters;


  /*   생성자 ->
     인자(parameters) 순서
     (ID,
     Listener)
   */
  public CheckRequest(String userID, Response.Listener<String> listener) {
    super(Method.POST, URL, listener, null);
    parameters = new HashMap<>();
    parameters.put("userID", userID);
  }

  @Override
  public Map<String, String> getParams() {
    return parameters;
  }
}
