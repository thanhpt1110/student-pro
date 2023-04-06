package com.example.testfirebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testfirebase.R;
import com.example.testfirebase.dao.ClassDAO;

public class AddClass extends AppCompatActivity {
    EditText inputName, inputId;
    Button btnSave;
    ClassDAO classDAO;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        inputName = findViewById(R.id.inputClassName);
        inputId = findViewById(R.id.inputClassId);
        btnSave = findViewById(R.id.btnSaveClass);
        classDAO = new ClassDAO();

        btnSave.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            String id = inputId.getText().toString().trim();

            if (name.isEmpty() || id.isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            classDAO.addNewClass(id, name);
            Toast.makeText(this, "Thêm thông tin lớp học thành công!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}