package com.example.testfirebase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.testfirebase.R;
import com.example.testfirebase.model.Class;
import com.example.testfirebase.model.Student;

import java.util.ArrayList;

public class ClassAdapter extends ArrayAdapter {
    private ArrayList<Class> classes;
    private int resource;
    private Context context;

    public ClassAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Class> classes) {
        super(context, resource, classes);
        this.resource = resource; // Layout dùng để custom
        this.classes = classes; // Dữ liệu
        this.context = context; // Activity cần tạo listview
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convertView là cái dòng view mới tự custom (1 dòng có 2 txtView)
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(this.getContext()); // Muốn custom đc view (hay là file .xml)
            // thì phải có context chứa nó
            v = vi.inflate(resource, null);  // Lấy layout cần custom qua View để lát return
        }

        Class c = classes.get(position); // Vị trí của student trong mảng

        if (c != null) {
            TextView tvClassName = v.findViewById(R.id.tvClassName);
            TextView tvNumsOfStudent = v.findViewById(R.id.tvClassNumsStudent);
            tvClassName.setText(c.getName());
            tvNumsOfStudent.setText("Số lượng: " + c.getNumberOfStudent());
        }
        return v;
    }
}
