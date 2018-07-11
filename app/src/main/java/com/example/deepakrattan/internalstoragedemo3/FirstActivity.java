package com.example.deepakrattan.internalstoragedemo3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtName, edtPassword;
    private Button btnSaveInternal, btnSaveCache, btnSaveExternal, btnNext;
    private String nam, name, password;
    private TextView txtResult;
    FileOutputStream fout;
    public static final int RequestPermissionCode = 999;
    public static final String TAG = "TEST";
    public static final String PRE_MARSHMALLOW = "PreMarshMallow";
    public static final String MARSHMALLOW = "Marshmallow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        edtName = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPassword);
        btnSaveInternal = findViewById(R.id.btnSaveInternal);
        btnSaveCache = findViewById(R.id.btnSaveCache);
        btnNext = findViewById(R.id.btnNext);
        btnSaveExternal = findViewById(R.id.btnSaveExternale);
        txtResult = findViewById(R.id.txtResult);

        btnNext.setOnClickListener(this);
        btnSaveCache.setOnClickListener(this);
        btnSaveInternal.setOnClickListener(this);
        btnSaveExternal.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveInternal:
                //Saving data in internal storage
                nam = edtName.getText().toString().trim();
                password = edtPassword.getText().toString().trim();
                name = nam + " ";

                try {
                    fout = openFileOutput("MyFile.txt", MODE_PRIVATE);
                    fout.write(name.getBytes());
                    fout.write(password.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fout.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(this, "data written successfully to " + getFilesDir() + "/MyFile.txt", Toast.LENGTH_SHORT).show();
                txtResult.setText("data written successfully to " + getFilesDir() + "/MyFile.txt");

                break;
            case R.id.btnSaveCache:
                //Writing data to cache directory
                String fileName = "MyCache";
                String data = "Welcome to android programming";
                File file = new File(getCacheDir(), fileName);
                try {
                    FileOutputStream fout = new FileOutputStream(file);
                    fout.write(data.getBytes());
                    fout.close();
                    Toast.makeText(this, "Data written to " + getCacheDir() + "/" + fileName, Toast.LENGTH_SHORT).show();
                    txtResult.setText("Data written to " + getCacheDir() + "/" + fileName);
                } catch (FileNotFoundException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btnNext:
                startActivity(new Intent(FirstActivity.this, SecondActivity.class));
                break;

            case R.id.btnSaveExternale:
                //1.check the platform
                String platform = checkPlatform();
                if (platform.equals("Marshmallow")) {
                    Log.d(TAG, "Runtime permission required");
                    //2. check the permission
                    boolean permissionStatus = checkPermission();
                    if (permissionStatus) {
                        Log.d(TAG, "onClick: permission already granted");
                    } else {
                        //3. explain permission
                        Log.d(TAG, "onClick: Explain permission");
                        explainPermission();
                        //4. request permission
                        Log.d(TAG, "onClick: Request permission");
                        requestPermission();
                    }

                } else {
                    Log.d(TAG, "onClick: Runtime permission not required.Permission already granted");
                }


                //we need to check if the external storage is available and is not read only.
                if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
                    //disable the button
                    btnSaveExternal.setEnabled(false);
                }


                fileName = "MyExternalFile";
                String text = "This data is written to External storage";
                // Creating a file named MyExternalFile in Downloads folder
                //Data will be stored in /storage/emulated/0/Download/MyExternalFile
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File myFile = new File(dir, fileName);
                try {
                    fout = new FileOutputStream(myFile);
                    fout.write(text.getBytes());
                    fout.close();
                    Toast.makeText(this, "Data is written to " + myFile, Toast.LENGTH_SHORT).show();
                    txtResult.setText("Data is written to " + myFile);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

    }

    //1. Check the platfrom
    public String checkPlatform() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return MARSHMALLOW;

        } else {
            return PRE_MARSHMALLOW;
        }
    }

    //2. Checking the read external storage and write external storage permission
    public boolean checkPermission() {
        int cameraPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int readContactPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return cameraPermissionResult == PackageManager.PERMISSION_GRANTED && readContactPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    //3. Explain Permission required
    public void explainPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(FirstActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Log.d(TAG, "explainPermission:Camera Permission required ");
            Toast.makeText(FirstActivity.this, "Read External Storage Permission required", Toast.LENGTH_SHORT).show();
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(FirstActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Log.d(TAG, "explainPermission:Read Contacts Permission Required ");
            Toast.makeText(FirstActivity.this, "Write external storage Permission Required", Toast.LENGTH_SHORT).show();
        }
    }

    //4. Requesting the read external storage and write external storage permission
    public void requestPermission() {
        ActivityCompat.requestPermissions(FirstActivity.this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, RequestPermissionCode);
    }

    //5. Handle the respone
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission was granted
                    Log.d(TAG, "onRequestPermissionsResult: Permission Granted");
                    Toast.makeText(FirstActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    //Permission Denied
                    Log.d(TAG, "onRequestPermissionsResult: Permission Denied");
                    Toast.makeText(FirstActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }


    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
