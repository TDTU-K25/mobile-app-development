package com.example.ex4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex4.R;
import com.example.ex4.model.MyFile;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    private final OnFileClickListener onFileClickListener;
    private final Context context;
    public ArrayList<MyFile> dataSource;

    public FileAdapter(Context context, ArrayList<MyFile> dataSource, OnFileClickListener onFileClickListener) {
        this.context = context;
        this.dataSource = dataSource;
        this.onFileClickListener = onFileClickListener;
    }

    public void removeAll() throws IOException {
        for (MyFile myFile : dataSource) {
            File file = new File(myFile.getPath());
            FileUtils.deleteQuietly(file);
        }
        dataSource.clear();
    }

    public void removeSelected() {
        ArrayList<MyFile> filesNotSelected = new ArrayList<>();
        for (MyFile myFile : dataSource) {
            if (!myFile.isChecked()) {
                filesNotSelected.add(myFile);
            } else {
                File file = new File(myFile.getPath());
                FileUtils.deleteQuietly(file);
            }
        }
        dataSource = filesNotSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyFile file = dataSource.get(position);
        holder.viewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFileClickListener.onShowChild(file.getFileName());
            }
        });
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file.setChecked(!file.isChecked());
            }
        });
        holder.bindData(position, file);
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public interface OnFileClickListener {
        void onShowChild(String folder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View viewContainer;
        TextView tvFileName;
        CheckBox cb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewContainer = itemView;
            tvFileName = itemView.findViewById(R.id.tv_fileName);
            cb = itemView.findViewById(R.id.cb);
        }

        public void bindData(int position, MyFile file) {
            tvFileName.setText(file.getFileName());
        }
    }
}
