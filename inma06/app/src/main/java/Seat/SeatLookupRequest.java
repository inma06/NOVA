package Seat;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SeatLookupRequest extends StringRequest {
/*


* 좌석 휴식 리퀘스트입니다.
*
* 요청 전달 인자는
* 좌석 번호, 좌석 이용자의 정보(ID, 닉네임, 연락처, 기수 등), 현재 시각(UTC) 입니다.
*
* DB에 저장이 성공할 경우 response['success'] = true 를 리턴합니다.
*
* -By inma06
* */


  final static private String URL = "https://bongbong.ga/seatLookup.php";
  private Map<String, String>  parameters;


  /*   생성자 ->
     인자(parameters) 순서
     (s
   */
  public SeatLookupRequest(String seatNumber, Response.Listener<String> listener) {
    super(Method.POST, URL, listener, null);
    parameters = new HashMap<>();
    parameters.put("seatNumber", seatNumber);
  }

  @Override
  public Map<String, String> getParams() {
    return parameters;
  }
}
