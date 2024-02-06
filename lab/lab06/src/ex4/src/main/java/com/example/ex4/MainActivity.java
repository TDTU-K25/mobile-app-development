package com.example.ex4;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex4.adapter.FileAdapter;
import com.example.ex4.model.MyFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FileAdapter.OnFileClickListener {
    RecyclerView rv;
    Button btnBack;
    FileAdapter adapter;

    File rootFolder;
    String currentPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rv);
        btnBack = findViewById(R.id.btn_back);

        rootFolder = Environment.getExternalStorageDirectory();
        currentPath = rootFolder.getAbsolutePath();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentPath.equals(rootFolder.getPath())) {
                    Path path = Paths.get(currentPath);
                    Path parentPath = path.getParent();
                    currentPath = parentPath.toString();
                    ArrayList<MyFile> children = getChildOfFolder(new File(currentPath));
                    if (!children.isEmpty()) {
                        adapter.dataSource = children;
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "You are at root folder", Toast.LENGTH_LONG).show();
                }
            }
        });

        // set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        // set adapter
        ArrayList<MyFile> dataSource = getChildOfFolder(rootFolder);
        adapter = new FileAdapter(this, dataSource, this);
        rv.setAdapter(adapter);
    }

    public ArrayList<MyFile> getChildOfFolder(File folder) {
        ArrayList<MyFile> result = new ArrayList<>();
        File[] children = folder.listFiles();
        if (children != null) {
            for (final File file : children) {
                if (file.isDirectory()) {
                    MyFile myFile = new MyFile(file.getName(), "folder", file.getPath());
                    result.add(myFile);
                } else {
                    MyFile myFile = new MyFile(file.getName(), "file", file.getPath());
                    result.add(myFile);
                }
            }
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_file_management, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.add_new_file) {
            addNewFile();
        } else if (itemId == R.id.create_new_folder) {
            showAddNewFolderDialog();
        } else if (itemId == R.id.remove_all) {
            showDeleteChildrenOfFolderDialog();
        } else if (itemId == R.id.remove_selected) {
            showDeleteSelectedChildrenOfFolderDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteSelectedChildrenOfFolderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove selected?").setMessage("Are you sure to remove those selected?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                adapter.removeSelected();
                adapter.notifyDataSetChanged();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.create().show();
    }

    private void showDeleteChildrenOfFolderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove all?").setMessage("Are you sure to remove all?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    adapter.removeAll();
                    adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.create().show();
    }

    private AlertDialog createCustomAlertDialog(String title, int layoutId, DialogInterface.OnClickListener onClickPositiveBtnListener, DialogInterface.OnClickListener onClickNegativeBtnListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setTitle(title);
        builder.setView(inflater.inflate(layoutId, null))
                // Add action buttons
                .setPositiveButton("OK", onClickPositiveBtnListener).setNegativeButton("CANCEL", onClickNegativeBtnListener);
        return builder.create();
    }

    private void showAddNewFolderDialog() {
        DialogInterface.OnClickListener onClickPositiveBtnListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (currentPath.equals(rootFolder.getPath())) {
                    Toast.makeText(MainActivity.this, "Can not create folder at root folder", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog alertDialog = (AlertDialog) dialog;
                    EditText etFolderName = alertDialog.findViewById(R.id.et_folderName);
                    String folderName = etFolderName.getText().toString();
                    File newFolder = new File(currentPath, folderName);
                    boolean isSuccess = newFolder.mkdirs();
                    if (isSuccess) {
                        adapter.dataSource = getChildOfFolder(new File(currentPath));
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, folderName + " is created successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Created failed or can not create folder here", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        DialogInterface.OnClickListener onClickNegativeBtnListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };
        createCustomAlertDialog("Create a folder", R.layout.dialog_create_new_folder, onClickPositiveBtnListener, onClickNegativeBtnListener).show();
    }

    private void addNewFile() {
        DialogInterface.OnClickListener onClickPositiveBtnListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (currentPath.equals(rootFolder.getPath())) {
                    Toast.makeText(MainActivity.this, "Can not create file at root folder", Toast.LENGTH_LONG).show();
                } else {
                    String[] pathComponents = currentPath.split("/");
                    File path = Environment.getExternalStoragePublicDirectory(pathComponents[pathComponents.length - 1]);
                    AlertDialog alertDialog = (AlertDialog) dialog;
                    EditText etFileName = alertDialog.findViewById(R.id.et_fileName);
                    EditText etContent = alertDialog.findViewById(R.id.et_fileContent);
                    String fileName = etFileName.getText().toString() + ".txt";
                    File newFile = new File(path, fileName);
                    try {
                        FileWriter writer = new FileWriter(newFile);
                        writer.write(etContent.getText().toString());
                        writer.close();
                        adapter.dataSource = getChildOfFolder(path);
                        adapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        DialogInterface.OnClickListener onClickNegativeBtnListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };
        createCustomAlertDialog("Create a file", R.layout.dialog_create_new_file, onClickPositiveBtnListener, onClickNegativeBtnListener).show();
    }

    @Override
    public void onShowChild(String folderName) {
        File folder = new File(currentPath + "/" + folderName);
        ArrayList<MyFile> children = getChildOfFolder(folder);
        if (!children.isEmpty()) {
            currentPath += "/" + folderName;
            adapter.dataSource = children;
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "This folder has no children or can not read folder", Toast.LENGTH_LONG).show();
        }
    }
}