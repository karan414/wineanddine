package com.example.karan.winedine.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;

import com.example.karan.winedine.MainActivity;
import com.example.karan.winedine.R;
import com.example.karan.winedine.utils.AppLogger;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;


public class QRScannerActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private TextView text;
    private QREader qrEader;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr__scanner);

        surfaceView = (SurfaceView) findViewById(R.id.camera_view);

        text = (TextView) findViewById(R.id.text);


        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        scanner();
    }

    public void scanner() {
        qrEader = new QREader.Builder(this, surfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                AppLogger.LogD("QREader", "Value : " + data);
                text.post(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(data);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("qrresult", data);
                        startActivity(i);
                        finish();
                        if (qrEader.isCameraRunning()) {
                            qrEader.stop();
                        }
                    }
                });
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(surfaceView.getHeight())
                .width(surfaceView.getWidth())
                .build();
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Don't Allow",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Allow",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(QRScannerActivity.this,
                                new String[]{android.Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                });
        alertDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the related task you need to do.
                    scanner();
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    if (ActivityCompat.shouldShowRequestPermissionRationale (this, Manifest.permission.CAMERA)) {
                        showAlert();
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Init and Start with SurfaceView
        // -------------------------------
        qrEader.initAndStart(surfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Cleanup in onPause()
        // --------------------
        qrEader.releaseAndCleanup();
    }
}
