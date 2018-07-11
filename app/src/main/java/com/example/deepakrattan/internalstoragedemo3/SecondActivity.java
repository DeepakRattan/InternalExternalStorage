package com.example.deepakrattan.internalstoragedemo3;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtName, txtPassword, txtData;
    private Button btnLoadInternal, btnLoadCache, btnReadExternal;
    StringBuffer buffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        txtName = findViewById(R.id.txtName);
        txtPassword = findViewById(R.id.txtPassword);
        txtData = findViewById(R.id.txtData);
        btnLoadInternal = findViewById(R.id.btnLoadInternal);
        btnLoadCache = findViewById(R.id.btnLoadCache);
        btnReadExternal = findViewById(R.id.btnReadExternal);

        btnLoadInternal.setOnClickListener(this);
        btnLoadCache.setOnClickListener(this);
        btnReadExternal.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoadInternal:
                try {
                    //Creating file MyFile.txt in files directory
                    FileInputStream fin = openFileInput("MyFile.txt");
                    int ch;
                    buffer = new StringBuffer();
                    while ((ch = fin.read()) != -1) {
                        buffer.append((char) ch);
                    }
                    String name = buffer.substring(0, buffer.indexOf(" "));
                    String password = buffer.substring(buffer.indexOf(" ") + 1);
                    txtName.setText(name);
                    txtPassword.setText(password);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btnLoadCache:
                //Reading data from cache directory
                File file = new File(getCacheDir(), "MyCache");
                String line;
                buffer = new StringBuffer();
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    while ((line = br.readLine()) != null) {
                        buffer.append(line);
                    }
                    txtData.setText(buffer.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnReadExternal:
                String fileName = "MyExternalFile";
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                file = new File(dir, fileName);
                try {
                    FileInputStream fin = new FileInputStream(file);
                    int ch;
                    buffer = new StringBuffer();
                    while ((ch = fin.read()) != -1) {
                        buffer.append((char) ch);
                    }
                    txtData.setText(buffer.toString());
                    fin.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
    }

}

