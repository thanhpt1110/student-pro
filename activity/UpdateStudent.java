package com.example.testfirebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testfirebase.AddStudentCallBack;
import com.example.testfirebase.R;
import com.example.testfirebase.dao.StudentDAO;

public class UpdateStudent extends AppCompatActivity implements AddStudentCallBack {
    EditText updateName, updateAge, updateClassId;
    Button updateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);
        updateName = findViewById(R.id.updateStudentName);
        updateAge = findViewById(R.id.updateStudentAge);
        updateClassId = findViewById(R.id.updateStudentClass);
        updateInfo = findViewById(R.id.btnUpdateStudent);

        Bundle bundle = getIntent().getBundleExtra("studentData");

        String studentId = bundle.getString("studentId");
        String studentName = bundle.getString("studentName");
        String studentAge = bundle.getString("studentAge");
        String oldClassId = bundle.getString("classId");

        updateName.setText(studentName);
        updateAge.setText(studentAge);
        updateClassId.setText(oldClassId);

        updateInfo.setOnClickListener(v -> {
            String newStudentName = updateName.getText().toString();
            String newStudentAge = updateAge.getText().toString();
            String newClassId = updateClassId.getText().toString();
            if (newStudentName.isEmpty() || newStudentAge.isEmpty() || newClassId.isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            StudentDAO studentDAO = new StudentDAO();
            studentDAO.updateStudent(studentId, newStudentName, newStudentAge, oldClassId, newClassId, this);
        });
    }

    @Override
    public void onSuccess() {
        Toast.makeText(this, "Cập nhật thông tin sinh viên thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, "Lỗi: " + message, Toast.LENGTH_LONG).show();
    }
}