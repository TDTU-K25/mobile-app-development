package com.example.ex2;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class MainActivity extends AppCompatActivity {

    EditText etContent;
    Button btnReadInternal;
    Button btnWriteInternal;
    Button btnReadExternal;
    Button btnWriteExternal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etContent = findViewById(R.id.et_content);
        btnReadInternal = findViewById(R.id.btn_read_internal);
        btnWriteInternal = findViewById(R.id.btn_write_internal);
        btnReadExternal = findViewById(R.id.btn_read_external);
        btnWriteExternal = findViewById(R.id.btn_write_external);

        btnReadInternal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etContent.setText(readFileOnInternalStorage("internal.txt"));
                Toast.makeText(MainActivity.this, "Read internal success", Toast.LENGTH_LONG).show();
            }
        });

        btnWriteInternal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeFileOnInternalStorage("internal.txt", etContent.getText().toString());
                Toast.makeText(MainActivity.this, "Write internal success", Toast.LENGTH_LONG).show();
            }
        });

        btnWriteExternal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeFileOnExternalStorage("lab06_ex2", "external.txt", etContent.getText().toString());
                Toast.makeText(MainActivity.this, "Write external success", Toast.LENGTH_LONG).show();
            }
        });

        btnReadExternal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    etContent.setText(readFileOnExternalStorage("lab06_ex2", "external.txt"));
                    Toast.makeText(MainActivity.this, "Read external success", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void writeFileOnInternalStorage(String fileName, String content) {
        File path = this.getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(path, fileName));
            writer.write(content.getBytes(StandardCharsets.UTF_8));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFileOnExternalStorage(String subDirName, String fileName, String content) {
        File fileWantToWrite = new File(getExternalFilesDir(subDirName), fileName);
        try {
            FileOutputStream writer = new FileOutputStream(fileWantToWrite);
            writer.write(content.getBytes(StandardCharsets.UTF_8));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isExternalStorageAvaiable() {
        String externalStorageState = Environment.getExternalStorageState();
        return externalStorageState.equals(Environment.MEDIA_MOUNTED);
    }

    private String readFileOnInternalStorage(String fileName) {
        String result = null;
        File path = this.getFilesDir();
        File fileWantToRead = new File(path, fileName);
        byte[] content = new byte[(int) fileWantToRead.length()];
        try {
            FileInputStream reader = new FileInputStream(fileWantToRead);
            reader.read(content);
            result = new String(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String readFileOnExternalStorage(String subDirName, String fileName) throws IOException {
        String result = null;
        File fileWantToRead = new File(getExternalFilesDir(subDirName), fileName);
        StringBuilder sb = new StringBuilder();
        try {
            FileReader reader = new FileReader(fileWantToRead);
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null) {
                sb.append(line).append("\n");
                line = br.readLine();
            }
            result = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}