package com.example.summerproject2024;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {
    // 권한 요청 코드
    public static final int REQUEST_PERMISSION_CODE = 100;

    // 필요한 권한 배열
    private static final String[] PERMISSIONS = {
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    // 권한 요청 메서드
    public static void requestPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i("Permission", " permission request check start.");
            // 권한 체크
            boolean allPermissionsGranted = true;
            for (String permission : PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission", " permission check.");
                    allPermissionsGranted = false;
                    break;
                }
            }

            // 권한이 없으면 요청
            if (!allPermissionsGranted) {
                Log.i("Permission", " permission request.");
                ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_PERMISSION_CODE);
            } else {
                // 모든 권한이 이미 부여된 경우
                Log.i("Permission", "All permissions are already granted.");
            }
        }
    }

    // 권한 요청 결과 처리
    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission", permissions[i] + " permission granted.");
                } else {
                    Log.i("Permission", permissions[i] + " permission denied.");
                }
            }
        }
    }
}
