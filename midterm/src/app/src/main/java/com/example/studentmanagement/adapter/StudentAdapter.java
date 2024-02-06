package com.example.studentmanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanagement.R;
import com.example.studentmanagement.model.Student;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class StudentAdapter extends FirestoreRecyclerAdapter<Student, StudentAdapter.ViewHolder> {
    private final OnStudentClickListener studentClickListener;

    public StudentAdapter(@NonNull FirestoreRecyclerOptions<Student> options, OnStudentClickListener studentClickListener) {
        super(options);
        this.studentClickListener = studentClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_one_student, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Student model) {
        holder.bindData(position, model);
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentClickListener.onEditStudentClick(model.getId());
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentClickListener.onDeleteStudentClick(model.getId());
            }
        });

        holder.ivDetailedInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentClickListener.onSeeStudentDetailedInfoClick(model.getId());
            }
        });
    }

    @Override
    public void updateOptions(@NonNull FirestoreRecyclerOptions<Student> options) {
        super.updateOptions(options);
        notifyDataSetChanged(); // need this if not it will error when call this method
    }

    public interface OnStudentClickListener {
        void onEditStudentClick(String uid);

        void onDeleteStudentClick(String uid);

        void onSeeStudentDetailedInfoClick(String uid);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvEmail;
        TextView tvAge;
        TextView tvGpa;
        ImageView ivDetailedInfo;
        ImageView ivEdit;
        ImageView ivDelete;
        View viewContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewContainer = itemView;
            tvName = itemView.findViewById(R.id.tv_name);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvAge = itemView.findViewById(R.id.tv_age);
            tvGpa = itemView.findViewById(R.id.tv_gpa);
            ivDetailedInfo = itemView.findViewById(R.id.iv_detailed_info);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivDelete = itemView.findViewById(R.id.iv_delete);
        }

        public void bindData(int position, Student student) {
            tvName.setText(student.getName());
            tvEmail.setText(student.getEmail());
            tvAge.setText(String.valueOf(student.getAge()));
            tvGpa.setText(String.valueOf(student.getGpa()));
        }
    }
}
