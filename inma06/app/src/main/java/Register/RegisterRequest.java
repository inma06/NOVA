package Register;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
/*
* Volley 라이브러리를 이용하여 서버로 요청(Request)를 보내고
* 리턴값을 Json 형태로 받아옵니다.
*
* 요청 전달 인자는
* 아이디(이메일), 비밀번호, 이름, 닉네임, 연락처, 기수 입니다.
*
* DB에 저장이 성공할 경우 response['success'] = true 를 리턴합니다.
*
* -By inma06
* */


  final static private String URL = "https://bongbong.ga/Register.php";
  private Map<String, String>  parameters;


  /*   생성자 ->
     인자(parameters) 순서
     (ID, PW, Name,
     NickName, PhoneNum, Grade,
     Listener)
   */
  public RegisterRequest(String userID, String userPW, Response.Listener<String> listener) {
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
