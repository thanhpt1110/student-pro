package com.example.testfirebase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testfirebase.R;
import com.example.testfirebase.model.Student;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter{
    private ArrayList<Student> students;
    private int resource;
    private Context context;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Student> students) {
        super(context, resource, students);
        this.resource = resource; // Layout dùng để custom
        this.students = students; // Dữ liệu
        this.context = context; // Activity cần tạo listview
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        // convertView là cái dòng view mới tự custom (1 dòng có 2 txtView)
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(this.getContext()); // Muốn custom đc view (hay là file .xml)
            // thì phải có context chứa nó
            v = vi.inflate(resource, null);  // Lấy layout cần custom qua View để lát return
        }

        Student s = students.get(position); // Vị trí của student trong mảng

        if (s != null) {
            TextView tvStudentName = v.findViewById(R.id.tvStudentName);
            TextView tvStudentAge = v.findViewById(R.id.tvStudentAge);
            TextView tvStudentClass = v.findViewById(R.id.tvStudentClassId);
            tvStudentName.setText(s.getName());
            tvStudentAge.setText("Tuổi: " + s.getAge());
            tvStudentClass.setText("Lớp: " + s.getClassId());
        }
        return v;
    }
}