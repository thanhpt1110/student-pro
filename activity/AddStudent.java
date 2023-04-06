package com.example.testfirebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.testfirebase.AddStudentCallBack;
import com.example.testfirebase.R;
import com.example.testfirebase.dao.StudentDAO;

public class AddStudent extends AppCompatActivity implements AddStudentCallBack {
    EditText inputName, inputAge, inputClass;
    Button btnSave;
    StudentDAO studentDAO;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        inputName = findViewById(R.id.inputStudentName);
        inputAge = findViewById(R.id.inputStudentAge);
        inputClass = findViewById(R.id.inputStudentClass);
        btnSave = findViewById(R.id.btnSaveStudent);
        studentDAO = new StudentDAO();

        btnSave.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            String age = inputAge.getText().toString().trim();
            String classId = inputClass.getText().toString().trim();

            if (name.isEmpty() || age.isEmpty() || classId.isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            studentDAO.addNewStudent(name, age, classId, this);
            //Toast.makeText(this, "Save information successfully", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onSuccess() {
        Toast.makeText(this, "Thêm thông tin sinh viên thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, "Lỗi: " + message, Toast.LENGTH_LONG).show();
    }
}