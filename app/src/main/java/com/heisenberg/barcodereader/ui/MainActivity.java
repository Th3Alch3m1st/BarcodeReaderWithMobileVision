package com.heisenberg.barcodereader.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.heisenberg.barcodereader.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    View.OnClickListener mOnClickListenerSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnStart)
    void onClick(){
        MainActivityPermissionsDispatcher.takeCameraPermissionWithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.CAMERA})
    public void takeCameraPermission() {
        //camera start
    }

    @OnPermissionDenied({Manifest.permission.CAMERA})
    void showDeniedForCall() {
        Toast.makeText(this,"Needed camera permission to run this application",Toast.LENGTH_LONG).show();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA})
    void showNeverAskForCall() {
        mOnClickListenerSettings = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingIntent();
            }
        };
        Snackbar.make(findViewById(android.R.id.content), "Please allow camera permission", Snackbar.LENGTH_LONG)
                .setAction("Permission Setting", mOnClickListenerSettings)
                .setActionTextColor(Color.CYAN)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }

    void showSettingIntent() {
        Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(i);
    }



}
