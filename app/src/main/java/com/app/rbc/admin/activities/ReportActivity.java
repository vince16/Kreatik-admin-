package com.app.rbc.admin.activities;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.rbc.admin.Manifest;
import com.app.rbc.admin.R;
import com.app.rbc.admin.fragments.RecievedVehicle;
import com.orm.SugarContext;

public class ReportActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecievedVehicle recievedVehicle;
    public static final int ACTIVITY = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SugarContext.init(this);

        setFragment(1);
    }


    private void setFragment(int code) {
        switch (code) {
            case 1:
                recievedVehicle = new RecievedVehicle();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        recievedVehicle).commit();
                break;
        }
    }

    public void askPermission(int code) {
        switch (code) {
            case RecievedVehicle.RESULT_LOAD_IMG :
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            RecievedVehicle.RESULT_LOAD_IMG);

                }
                else {
                    recievedVehicle.loadImagefromGallery();
                }
                break;
            case RecievedVehicle.CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            RecievedVehicle.RESULT_LOAD_IMG);

                }
                else {
                    recievedVehicle.captureImage();

                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RecievedVehicle.RESULT_LOAD_IMG: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    recievedVehicle.loadImagefromGallery();

                } else {

                    Toast.makeText(this,
                            "Permission Denied!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case RecievedVehicle.CAMERA_CAPTURE_IMAGE_REQUEST_CODE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    recievedVehicle.captureImage();

                } else {

                    Toast.makeText(this,
                            "Permission Denied!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }

    public void publishAPIResponse(int status,int code,String... message) {
        switch (code) {
            case RecievedVehicle.FETCH_VEHICLE_API :
            case RecievedVehicle.ONRECEIVE_VEHICLE_API:
                recievedVehicle.publishAPIResponse(status,code,message);
                break;

        }
    }
}
