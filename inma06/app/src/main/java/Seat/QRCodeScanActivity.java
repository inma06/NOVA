package Seat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.teamnova.nova.R;

import org.json.JSONException;
import org.json.JSONObject;

import Main.HomeActivity;

public class QRCodeScanActivity extends AppCompatActivity {

  /*
  *
  * ====요구되는 정보====
  * 1. userID (사용자 정보).
  * -> 로그인 정보를 기준으로 전역변수에서 받는다.
  *
  * 2. sheetNumber (체크인 할 좌석 번호).
  * -> QR 코드 스캔, ... ... +@ AR(증강현실)
  *
  * userID, sheetNumber 를 CheckInRequest ( Volley )를 이용해 API 요청을 한다.
  * 서버에서 받아온 response 값으로 결과를 레이아웃에 반영하여 처리한다.
  *
  *
  * */
  private static String TAG = "QRCodeScanActivity.class";


  public static String mUserID = HomeActivity.mUserID;

  //view Objects
  private Button scanBtn, checkinBtn;
  private TextView seatNumTV, statusTV, userIDTV, resultTV;

  //qr code scanner object
  private IntentIntegrator qrScan;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_qrcode_scan);

    //View Objects
    checkinBtn = (Button) findViewById(R.id.checkinBtn);
    scanBtn = (Button) findViewById(R.id.buttonScan);
    seatNumTV = (TextView) findViewById(R.id.textViewSeatNum);
    statusTV = (TextView) findViewById(R.id.textViewStatus);
    resultTV = (TextView) findViewById(R.id.textViewResult);
    userIDTV = (TextView) findViewById(R.id.userIDTV);


    //내 아이디 텍스트뷰에 로그인한 userID 입력
    userIDTV.setText(mUserID.toString());

    //intializing scan object
    qrScan = new IntentIntegrator(this);

    //button onClick
    scanBtn.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        //scan option
        qrScan.setPrompt("스캔 중...");
        //qrScan.setOrientationLocked(false);
        qrScan.initiateScan();
      }
    });

    checkinBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /* 체크인 버튼을 누르면
        -> (sheetCheckIn.php) API 에 체크인을 요청한다
        -> API는 DB에 값을 UPDATE하고 결과를 반환한다.
        */

        final ProgressDialog dialog= ProgressDialog.show(QRCodeScanActivity.this,
            "체크인","체크인 하는 중...",true);
        Log.d("TEST", "체크인 버튼 클릭 OK");
        final String userID = userIDTV.getText().toString();
        final String seatNumber = seatNumTV.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try{
              JSONObject jsonResponse = new JSONObject(response);

              boolean success = jsonResponse.getBoolean("success");

              if(success == false){
                // 체크인 실패시 ( 다른 사용자가 이미 사용 중 일 경우 등... )
                Log.i(TAG, "onResponse: 체크인 실패");
                AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeScanActivity.this);
                builder.setMessage("체크인에 실패하였습니다.")
                    .setNegativeButton("다시 시도", null)
                    .create()
                    .show();
              } else {
                // 체크인 성공시
                Toast.makeText(QRCodeScanActivity.this, "체크인 성공! API동작 확인!", Toast.LENGTH_SHORT).show();
                resultTV.setText(response.toString());


              }
            } catch (Exception e){
              e.printStackTrace();
            }
            dialog.dismiss();
            Log.e("response -> 리스폰 결과값 출력 ", response.toString());
          }
        };
        CheckInRequest checkInRequest = new CheckInRequest(userID, seatNumber, responseListener);
        RequestQueue queue = Volley.newRequestQueue(QRCodeScanActivity.this);
        queue.add(checkInRequest);

      }
    });
  }



  //Getting the scan results
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data){
    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
    if (result != null) {
      //qrcode 가 없으면
      if (result.getContents() == null) {
        Toast.makeText(QRCodeScanActivity.this, "취소!", Toast.LENGTH_SHORT).show();
      } else {
        //qrcode 결과가 있으면
        Toast.makeText(QRCodeScanActivity.this, "스캔완료!", Toast.LENGTH_SHORT).show();
        try {
          //data를 json으로 변환
          JSONObject obj = new JSONObject(result.getContents());
          Log.d("TEST", "체크인 버튼 클릭 OK");
          final String seatNumber = obj.getString("seatNumber");

          Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              try{
                JSONObject jsonResponse = new JSONObject(response);

                boolean success = jsonResponse.getBoolean("success");

                if(success == false){
                  // 좌석 확인 실패시 ( 다른 사용자가 이미 사용 중 일 경우 등... )
                  Log.i(TAG, "onResponse: 체크인 실패");
                  AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeScanActivity.this);
                  builder.setMessage("해당 좌석은 사용중 입니다.")
                      .setNegativeButton("다시 시도", null)
                      .create()
                      .show();
                } else {
                  // 좌석 확인 성공시
                  Toast.makeText(QRCodeScanActivity.this, "체크인 성공! API동작 확인!", Toast.LENGTH_SHORT).show();
                  resultTV.setText(response.toString());
                  AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeScanActivity.this);
                  builder.setMessage(seatNumber+"번 좌석")
                      .setPositiveButton("사용 시작", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          // 좌석 사용을 시작
                          // sheetCheckIn.php API 에 요청합니다.
                          final ProgressDialog progressDialog = ProgressDialog.show(QRCodeScanActivity.this,
                              "체크인","체크인 하는 중...",true);
                          Log.d("TEST", "체크인 버튼 클릭 OK");

                          Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                              try{
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if(success == false){
                                  Log.i(TAG, "onResponse: 체크인 실패");
                                  AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeScanActivity.this);
                                  builder.setMessage("체크인에 실패하였습니다.")
                                      .setNegativeButton("다시 시도", null)
                                      .create()
                                      .show();
                                } else {
                                  // 체크인 성공시
                                  Toast.makeText(QRCodeScanActivity.this, "체크인 성공! API동작 확인!", Toast.LENGTH_SHORT).show();
                                  statusTV.setText( seatNumber + "번 좌석 사용을 시작하였습니다.");
                                }
                              } catch (Exception e){
                                e.printStackTrace();
                              }
                              progressDialog.dismiss();
                              Log.e("response -> 리스폰 결과값 출력 ", response.toString());
                            }
                          };
                          CheckInRequest checkInRequest = new CheckInRequest(mUserID, seatNumber, responseListener);
                          RequestQueue queue = Volley.newRequestQueue(QRCodeScanActivity.this);
                          queue.add(checkInRequest);


                        }
                      })
                      .setNegativeButton("취소", null)
                      .setNeutralButton("휴식(beta)", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          final ProgressDialog progressDialog = ProgressDialog.show(QRCodeScanActivity.this,
                              "휴식","휴식 시작...",true);

                          Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                              try{
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if(success == false){
                                  Log.i(TAG, "onResponse: 체크인 실패");
                                  AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeScanActivity.this);
                                  builder.setMessage("체크인에 실패하였습니다.")
                                      .setNegativeButton("다시 시도", null)
                                      .create()
                                      .show();
                                } else {
                                  // 체크인 성공시
                                  Toast.makeText(QRCodeScanActivity.this, "휴식 성공! API동작 확인!", Toast.LENGTH_SHORT).show();
                                  statusTV.setText( seatNumber + "번 좌석에서 휴식을 시작하였습니다.");
                                }
                              } catch (Exception e){
                                e.printStackTrace();
                              }
                              progressDialog.dismiss();
                              Log.e("response -> 리스폰 결과값 출력 ", response.toString());
                            }
                          };
                          SeatRestRequest seatRestRequest = new SeatRestRequest(mUserID, seatNumber, responseListener);
                          RequestQueue queue = Volley.newRequestQueue(QRCodeScanActivity.this);
                          queue.add(seatRestRequest);
                        }
                      })
                      .create()
                      .show();


                }
              } catch (Exception e){
                e.printStackTrace();
              }
              Log.e("response -> 리스폰 결과값 출력 ", response.toString());
            }
          };
          SeatLookupRequest seatLookupRequest = new SeatLookupRequest(seatNumber, responseListener);
          RequestQueue queue = Volley.newRequestQueue(QRCodeScanActivity.this);
          queue.add(seatLookupRequest);

        } catch (JSONException e) {
          e.printStackTrace();
          resultTV.setText(result.getContents());
        }
      }

    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }
}
