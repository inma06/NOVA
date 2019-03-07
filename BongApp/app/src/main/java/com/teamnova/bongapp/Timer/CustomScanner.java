package com.teamnova.bongapp.Timer;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.teamnova.bongapp.R;

public class CustomScanner extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {


  protected final String TAG = "CustomScanner";
  private CaptureManager capture;
  private DecoratedBarcodeView barcodeScannerView;
  private Button btnSetting, btnSwitchFlash;
  private Boolean switchFlashlightButtonCheck;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_custom_scanner);

    switchFlashlightButtonCheck = true;

    btnSetting = findViewById(R.id.btn_setting);
    btnSwitchFlash = findViewById(R.id.btn_switch_flash);
    barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);


    if (!hasFlash()) {
      btnSwitchFlash.setVisibility(View.GONE);
    }

    barcodeScannerView.setTorchListener(this);
    capture = new CaptureManager(this, barcodeScannerView);
    capture.initializeFromIntent(getIntent(), savedInstanceState);
    capture.decode();

    btnSwitchFlash.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(switchFlashlightButtonCheck) {
          barcodeScannerView.setTorchOn();
        } else {
          barcodeScannerView.setTorchOff();
        }
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    capture.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    capture.onPause();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    capture.onDestroy();
  }


  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    capture.onSaveInstanceState(outState);
  }

  private boolean hasFlash() {
    return getApplicationContext().getPackageManager()
        .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
  }

  /**
   * TorchListener
   */
  @Override
  public void onTorchOn() {
    switchFlashlightButtonCheck = false;
  }

  @Override
  public void onTorchOff() {
    switchFlashlightButtonCheck = true;
  }

}


